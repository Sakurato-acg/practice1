package org.example.thread;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("all")
@Slf4j
public class TtlControllerTest {

    private static ExecutorService executorService = Executors.newFixedThreadPool(1);

    public static void main(String[] args) throws InterruptedException {
        final ThreadLocal tl1 = new TransmittableThreadLocal();
        final ThreadLocal tl2 = new TransmittableThreadLocal();
        tl1.set(1);
        executorService.execute(TtlRunnable.get(() -> {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
        tl2.set(3);
        Thread.sleep(1000L); //这块停顿一下
        executorService.execute(TtlRunnable.get(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }));
    }
}