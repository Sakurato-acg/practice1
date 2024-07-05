package org.example.thread.threadLocal;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InheritableThreadLocalDemo3 {

    /**
     * 业务线程池，service 中执行异步任务的线程池
     */
    private static ExecutorService businessExecutors = Executors.newFixedThreadPool(5);

    /**
     * 线程上下文环境，在service中设置环境变量，
     * 然后在这里提交一个异步任务，模拟在子线程（执行异步任务的线程）中，是否可以访问到刚设置的环境变量值。
     */
    private static InheritableThreadLocal<Integer> requestIdThreadLocal = new InheritableThreadLocal<>();

    public static void main(String[] args) {
        // 模式10个请求，每个请求执行ControlThread的逻辑，其具体实现就是，先输出父线程的名称，
        for (int i = 0; i < 10; i++) {
            // 然后设置本地环境变量，并将父线程名称传入到子线程中，在子线程中尝试获取在父线程中的设置的环境变量
            new Thread(new ServiceThread(i)).start();
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //关闭线程池
        businessExecutors.shutdown();
    }

    /**
     * 模拟Service业务代码
     */
    @Slf4j
    static class ServiceThread implements Runnable {
        private int i;

        public ServiceThread(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            requestIdThreadLocal.set(i);
            log.info("执行service方法==>在{}中存储变量副本==>{}", Thread.currentThread().getName(), i);
            // 异步编程 CompletableFuture.runAsync()创建无返回值的简单异步任务，businessExecutors 表示线程池~
            CompletableFuture<Void> runAsync = CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 模拟执行时间
                        Thread.sleep(500L);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage());
                    }
                    log.info("执行异步任务，在执行异步任务的线程中，获取父线程（service）中存储的值：{}", requestIdThreadLocal.get());
                }
            }, businessExecutors);

            requestIdThreadLocal.remove();
        }
    }
}