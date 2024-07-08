package org.example.limiter.controller;

import com.alibaba.ttl.TtlRunnable;
import com.alibaba.ttl.threadpool.TtlExecutors;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.limiter.annotation.Limiter;
import org.example.thread.AsyncUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Limiter
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/get")
    public String test(String args1) {
        CompletableFuture.runAsync(Objects.requireNonNull(TtlRunnable.get(() -> {
            // 模拟执行时间
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("测试");
        })));
        new Thread().run();
//        throw new RuntimeException();
        return "测试";
    }


}

@Data
class Person {
    private String username;
}