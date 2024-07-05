package org.example.thread.threadLocal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalDemo1 {

    private static ThreadLocal<String> userHolder = new ThreadLocal<>();

    public static void main(String[] args) {

        new Thread(() -> {
            log.info("{} 保存临时用户信息", Thread.currentThread().getName());
            String userInfo = "宁在春";
            userHolder.set(userInfo);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 不会收到线程2的影响，因为ThreadLocal 线程本地存储
            log.info("{} 获取临时用户信息 {}", Thread.currentThread().getName(), userHolder.get());
            // 线程结束前，需要移除
            userHolder.remove();
        }, "myThread1").start();

        new Thread(() -> {
            log.info("{} 保存临时用户信息", Thread.currentThread().getName());
            String userInfo = "hello world";
            userHolder.set(userInfo);
            userHolder.remove();
        }, "myThread2").start();
    }
}