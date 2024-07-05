package org.example.thread.threadLocal;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class TransmittableThreadLocalDemo1 {

    /**
     * 业务线程池，service 中执行异步任务的线程池
     * 使用 TtlExecutors.getTtlExecutorService() 包装一下我们自己的线程池，这样才可以 使用 TransmittableThreadLocal 解决在使用线程池等会缓存线程的组件情况下传递ThreadLocal的问题
     */
//    private static ExecutorService businessExecutors = TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(5));
    private static ExecutorService businessExecutors = Executors.newFixedThreadPool(5);

    /**
     * 线程上下文环境，改为使用 TransmittableThreadLocal 来保存
     * 然后在这里提交一个异步任务，模拟在子线程（执行异步任务的线程）中，是否可以访问到刚设置的环境变量值。
     */
    private static TransmittableThreadLocal<Integer> requestIdThreadLocal = new TransmittableThreadLocal<>();

    @SneakyThrows
    public static void main(String[] args) {
        // 模式10个请求，每个请求执行ControlThread的逻辑，其具体实现就是，先输出父线程的名称，
        for (int i = 0; i < 10; i++) {
            // 然后设置本地环境变量，并将父线程名称传入到子线程中，在子线程中尝试获取在父线程中的设置的环境变量
            new Thread(new ServiceThread(i)).start();
        }
        Thread.sleep(10000);
        //关闭线程池
        businessExecutors.shutdown();
    }

    /**
     * 模拟Service业务代码
     */
    static class ServiceThread implements Runnable {
        private int i;

        public ServiceThread(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            requestIdThreadLocal.set(i);

            log.info("执行service方法==>在{}中存储变量副本==>{}", Thread.currentThread().getName(), "run" + i);

            businessExecutors.execute(Objects.requireNonNull(TtlRunnable.get(() -> {
                // 模拟执行时间
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.info("执行异步任务，在执行异步任务的线程中，获取父线程（service）中存储的值：{}", requestIdThreadLocal.get());
            })));


            requestIdThreadLocal.remove();
        }
    }
}
