package proxy;

import java.lang.reflect.Field;

public class Main {
    public static void main(String[] args) throws Exception {
        MyInterface proxyObject1 = MyClassFactory.createProxyObject(new PrintMethodName() );
        proxyObject1.function1();
        proxyObject1.function2();
        System.out.println("--------------");
        MyInterface proxyObject2 = MyClassFactory.createProxyObject(new MyAop(proxyObject1));
        proxyObject2.function1();
        proxyObject2.function2();
    }

    static class PrintMethodName implements MyHandler {

        @Override
        public String functionBody(String methodName) {
            return "System.out.println(\"" + methodName + "\");\n";
        }
    }

    static class MyAop implements MyHandler {

        private MyInterface myInterface;

        public MyAop(MyInterface myInterface) {
            this.myInterface = myInterface;
        }

        @Override
        public String functionBody(String methodName) {
            StringBuilder stringBuilder = new StringBuilder();
             stringBuilder
                     .append("System.out.println(\"before\");\n")
                     .append("myInterface." + methodName + "();\n")
                     .append("System.out.println(\"after\");\n");
            return stringBuilder.toString();
        }

        @Override
        public void setProxy(MyInterface proxy){
            Class<? extends MyInterface> aClass = proxy.getClass();
            Field field = null;
            try {
                field = aClass.getDeclaredField("myInterface");
                field.setAccessible(true);
                field.set(proxy, myInterface);
            } catch (Exception e ) {
                throw new RuntimeException(e);
            }

        }
    }
}
