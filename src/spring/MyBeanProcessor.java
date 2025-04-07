package spring;

@Component
public class MyBeanProcessor implements BeanPostProcessor {
    @Override
    public Object beforeInitializeBean(Object bean, String beanName) {
        System.out.println("post " + beanName);
        return bean;
    }

    @Override
    public Object afterInitializeBean(Object bean, String beanName) {
        System.out.println("after " + beanName);
        return bean;
    }
}
