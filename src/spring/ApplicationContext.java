package spring;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationContext {

    private final Map<String, Object> ioc = new HashMap<>();
    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    private final Map<String, Object> loadingBeanMap = new HashMap<>();

    private final List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public ApplicationContext(String packagePath) throws Exception {
        // 初始化容器
        initContext(packagePath);
    }

    public void initContext(String packagePath) throws Exception {
        // 扫描包路径
        scanPackage(packagePath)
                .stream()
                // 过滤出来需要创建的bean
                .filter(this::scanCreate)
                // 初始化Bean配置信息
                .forEach(this::wrapper);
        initBeanPostProcessor();
        // 初始化BeanDefinition的时候一起初始化，而不是一个一个，防止创建Bean的时候其中属性的BeanDefinition还未创建
        beanDefinitionMap.values().forEach(this::createBean);
    }

    private void initBeanPostProcessor() {
        beanDefinitionMap.values().stream()
                .filter(bd -> BeanPostProcessor.class.isAssignableFrom(bd.getBeanType()))
                .map(this::createBean)
                .map(bean -> (BeanPostProcessor) bean)
                .forEach(beanPostProcessorList::add);
    }


    private List<Class<?>> scanPackage(String packagePath) throws Exception {
        List<Class<?>> classList = new ArrayList<>();
        // a.b.c -》 a/b/c
        URL resource = this.getClass().getClassLoader().getResource(packagePath.replace(".", File.separator));
        Path path = Path.of(resource.getFile());
        Files.walkFileTree(path, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path absolutePath = file.toAbsolutePath();
                if (absolutePath.toString().endsWith(".class")) {
                    String replace = absolutePath.toString().replace(File.separator, ".");
                    int packageIndex = replace.indexOf(packagePath);
                    String className = replace.substring(packageIndex, replace.length() - ".class".length());
                    try {
                        classList.add(Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return classList;
    }

    protected boolean scanCreate(Class<?> type) {
        return type.isAnnotationPresent(Component.class);
    }

    protected BeanDefinition wrapper(Class<?> type) {
        BeanDefinition beanDefinition = new BeanDefinition(type);
        if (beanDefinitionMap.containsValue(beanDefinition)) {
            throw new RuntimeException("bean名字重复");
        }
        beanDefinitionMap.put(beanDefinition.getName(), beanDefinition);
        return beanDefinition;
    }

    protected Object createBean(BeanDefinition beanDefinition) {
        String name = beanDefinition.getName();
        if (ioc.containsKey(name)) {
            return ioc.get(name);
        }
        if (loadingBeanMap.containsKey(name)) {
            return loadingBeanMap.get(name);
        }
        try {
            return doCreateBean(beanDefinition);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object doCreateBean(BeanDefinition beanDefinition) {
        Constructor<?> constructor = beanDefinition.getConstructor();
        Object bean = null;
        try {
            bean = constructor.newInstance();
            // 正在加载的bean
            loadingBeanMap.put(beanDefinition.getName(), bean);
            // 注入属性
            autowiredBean(bean, beanDefinition);
            bean = initializeBean(bean, beanDefinition);
            loadingBeanMap.remove(beanDefinition.getName());
            ioc.put(beanDefinition.getName(), bean);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bean;
    }

    private Object initializeBean(Object bean, BeanDefinition beanDefinition) throws Exception {
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
            bean = beanPostProcessor.beforeInitializeBean(bean, beanDefinition.getName());
        }

        Method postConstructor = beanDefinition.getPostConstructor();
        if (postConstructor != null) {
            postConstructor.invoke(bean);
        }

        for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
            bean = beanPostProcessor.afterInitializeBean(bean, beanDefinition.getName());
        }
        return bean;
    }

    private void autowiredBean(Object bean, BeanDefinition beanDefinition) {
        List<BeanProperty> autowiredFieldList = beanDefinition.getAutowiredFieldList();
        autowiredFieldList.forEach(beanProperty -> {
            Field field = beanProperty.getField();
            boolean required = beanProperty.isRequired();
            field.setAccessible(true);
            try {
                Object o = getBean(field.getType());
                if (o == null && required) {
                    throw new RuntimeException("缺少Bean：" + field.getName());
                }
                field.set(bean, o);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }


    public Object getBean(String name) {
        Object bean = ioc.get(name);
        if (bean != null) {
            return bean;
        }
        // 从BeanDefinition中看看有没有
        if (beanDefinitionMap.containsKey(name)) {
            return createBean(beanDefinitionMap.get(name));
        }
        return null;
    }

    public <T> T getBean(Class<T> beanType) {
        String beanName = this.beanDefinitionMap.values().stream()
                .filter(bd -> beanType.isAssignableFrom(bd.getBeanType()))
                .map(BeanDefinition::getName)
                .findAny()
                .orElse(null);
        return (T) getBean(beanName);
    }

    public <T> List<T> getBeans(Class<T> beanType) {
        return this.beanDefinitionMap.values().stream()
                .filter(bean -> beanType.isAssignableFrom(bean.getClass()))
                .map(BeanDefinition::getName)
                .map(this::getBean)
                .map(bean -> (T) bean)
                .toList();
    }

}
