package proxy;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class JavaCompilerUtil {

    public static void compile(File sourceFile) {
        if (sourceFile == null || !sourceFile.exists() || !sourceFile.getName().endsWith(".java")) {
            throw new IllegalArgumentException("Invalid Java source file.");
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try (StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(null, null, null)) {
            Iterable<? extends JavaFileObject> javaFileObjectsFromFiles =
                    standardFileManager.getJavaFileObjectsFromFiles(List.of(sourceFile));

            List<String> options = Arrays.asList("-d", "./out/production/codingEnhance");

            JavaCompiler.CompilationTask task =
                    compiler.getTask(null,
                            standardFileManager,
                            null,
                            options, null,
                            javaFileObjectsFromFiles);
            Boolean call = task.call();
            if (call) {
                System.out.println("编译成功");
            } else{
                System.out.println("编译失败");
            }
        } catch (Exception e) {

        }
    }
}