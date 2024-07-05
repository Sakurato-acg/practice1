package org.example.thread.threadLocal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalExample2 {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        log.info("在主线程{}中保存临时用户信息", Thread.currentThread().getName());
        String userInfo = "宁在春";
        threadLocal.set(userInfo);
        new Thread(() -> {
            // 获取不到父线程存储的信息
            log.info("在子线程{}中获取临时用户信息 {}", Thread.currentThread().getName(), threadLocal.get());
        }, "MyThread2").start();
        threadLocal.remove();
    }
}
// 在主线程main中保存临时用户信息
// 在子线程pool-1-thread-1中获取临时用户信息 null
