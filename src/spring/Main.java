package spring;

public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new ApplicationContext("spring");
        Object cat = applicationContext.getBean("Cat");
        System.out.println(cat);
        Object mydog = applicationContext.getBean("mydog");
        System.out.println(mydog);
    }
}
