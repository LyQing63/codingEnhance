package ThreadPool;

import java.lang.ref.WeakReference;
import java.util.concurrent.*;

/**
 * 问题：线程池会不会被gc，或者说线程池什么时候会被回收，为什么
 */
public class ThreadPoolGCTest {

    public static void main(String[] args) {
        ThreadPoolExecutor executor =
                new ThreadPoolExecutor(2, 4, 1, TimeUnit.SECONDS,
                        new ArrayBlockingQueue<>(1024),
                        new MyThreadFactory());
        executor.execute(() -> System.out.println(Thread.currentThread().getName() + ": 任务开始"));
        // 添加若引用
        WeakReference<Executor> weakReference = new WeakReference<>(executor);
        // 原来的引用置空
        executor = null;
        // 开始gc
        System.gc();
        if (weakReference.get() == null) {
            System.out.println("线程池被回收了");
        } else {
            System.out.println("线程池没有被回收");
        }
    }

    static class MyThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            Runnable newRunnable = () -> {
                System.out.println("任务真正开始执行");
                // 打上断点可以看到r是一个ThreadPoolExecutor内Worker类的一个引用
                // 非内部静态类会持有主类的对象，因此，只要Worker还在，就一直会有对Executor的引用(当然可以将其改为静态内部类 )
                // r.run执行后，线程会内旋等待下一个任务，因此只要有一个线程被创建，只要这个线程不被回收，这个引用就会一直存在，不被gc
                // r.run不执行之后，线程会被直接回收，打破了Runnable与线程池的关联，于是executor就会被gc掉
                // 于是有了这个引用链     Thread -> Worker -> ThreadPoolExecutor
//                r.run();
                System.out.println("线程执行结束，等待回收 ");
            };
            Thread thread = new Thread(newRunnable, "线程1");
            System.out.println("线程创建了");
            return thread;
        }
    }

}
