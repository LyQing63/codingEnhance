package proxy;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;

public class MyClassFactory {

    private static final AtomicInteger atomicInteger = new AtomicInteger();

    private static File createJavaFile(String className, MyHandler myHandler) throws IOException {
        String function1 = myHandler.functionBody("function1");
        String function2 = myHandler.functionBody("function2");
        String content = "package proxy;\n" +
                "\n" +
                "public class " + className + " implements MyInterface{\n" +
                "\n" +
                "private MyInterface myInterface;" +
                "    @Override\n" +
                "    public void function1() {\n" +
                "        "+ function1 +"\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public void function2() {\n" +
                "        "+ function2 +"\n" +
                "    }\n" +
                "}\n";
        File file = new File(className+".java") ;
        Files.writeString(file.toPath(), content);
        return file;
    }

    private static String getClassName() {
        return "MyInterface$proxy" + atomicInteger.incrementAndGet();
    }

    private static MyInterface newInstance(String className, MyHandler myHandler) throws Exception {
        ClassLoader classLoader = MyClassFactory.class.getClassLoader();
        Class<?> aClass = classLoader.loadClass(className);
        Constructor<?> constructor = aClass.getConstructor();
        MyInterface proxy = (MyInterface) constructor.newInstance();
        myHandler.setProxy(proxy);
        return proxy;
    }

    public static MyInterface createProxyObject(MyHandler myHandler) throws Exception {
        String className = getClassName();
        File javaFile = createJavaFile(className, myHandler);
        JavaCompilerUtil.compile(javaFile);
        return newInstance("proxy."+className, myHandler);
    }

}
