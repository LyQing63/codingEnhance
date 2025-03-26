package proxy;

/**
 * 动态代理的目标类
 */
public class MyClass implements MyInterface{

    @Override
    public void function1() {
        System.out.println("MyClass1");
    }

    @Override
    public void function2() {
        System.out.println("MyClass2");
    }
}
