package org.example.thread.threadLocal;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;
import com.info.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
public class TTLbug {

    private static final ThreadLocal<Map<Integer, Integer>> threadLocal = new TransmittableThreadLocal();
    private static ExecutorService executor = Executors.newFixedThreadPool(1);

    static {
//        init();
    }

    int i = 0;

    public static void init() {
        log.info("init begin");
        Map<Integer, Integer> map = threadLocal.get();
        if (map == null) {
            threadLocal.set(new HashMap<>());
        }
    }


    @SystemLog(businessName = "测试")
    @GetMapping
    public String test() {
        Map<Integer, Integer> map = threadLocal.get();
        if (map == null) {
            init();
        }
        log.info("主线程开始：{}", threadLocal.get());

        threadLocal.get().put(++i, i);

        log.info("主线程put:\t{}={}", i, i);

        executor.execute(Objects.requireNonNull(TtlRunnable.get(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("子线程取到:{}", threadLocal.get());
            log.info("------------------------------");
        })));

        threadLocal.remove();
        log.info("主线程remove之后：{}", threadLocal.get());
        log.info("主线程结束");


        return "测试";
    }
}
