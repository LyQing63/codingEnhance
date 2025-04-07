package spring;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class BeanDefinition {

    private final String name;
    private final Constructor<?> constructor;
    private final Method postConstructor;
    private final List<BeanProperty> autowiredFieldList;
    private final Class<?> beanType;

    public BeanDefinition(Class<?> type) {
        Component component = type.getDeclaredAnnotation(Component.class);
        this.name = component.name().isEmpty() ? type.getSimpleName() : component.name();
        try {
            this.constructor = type.getConstructor();
            this.postConstructor = Arrays.stream(type.getMethods())
                    .filter(m -> m.isAnnotationPresent(PostConstructor.class))
                    .findFirst()
                    .orElse(null);
            // 需要注入的属性
            this.autowiredFieldList = Arrays.stream(type.getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(Autowired.class))
                    .map(field -> {
                        Autowired autowired = field.getAnnotation(Autowired.class);
                        boolean required = autowired.required();
                        return new BeanProperty(field, required);
                    })
                    .toList();
            beanType = type;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return this.name;
    }

    public Constructor<?> getConstructor() {
        return this.constructor;
    }

    public Method getPostConstructor() {
        return this.postConstructor;
    }

    public List<BeanProperty> getAutowiredFieldList() {
        return this.autowiredFieldList;
    }

    public Class<?> getBeanType() {
        return beanType;
    }
}
