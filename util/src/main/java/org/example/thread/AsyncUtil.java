package org.example.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

@Slf4j
public class AsyncUtil {
    private static ExecutorService executor;

    static {
        ThreadFactory threadFactory = new NameThreadFactory()
                .setNameFormat("async-util-pool-%d")
                .setDaemon(Boolean.TRUE)
                .build();
        executor = new ThreadPoolExecutor(
                10,
                10,
                60 * 5L,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                threadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    /**
     * 并发执行任务，未设置超时时间（存在隐式异常情况，需要调用方处理）
     */
    public static <T> List<T> executeThrown(Supplier<T>... suppliers) {
        if (suppliers == null || suppliers.length == 0) {
            return new ArrayList<>();
        }
        List<T> result = new ArrayList<>();
        for (CompletableFuture<T> future : getCompletableFuture(suppliers)) {
            result.add(future.join());
        }
        return result;
    }

    public static <T> List<T> execute(Supplier<T>... suppliers) {
        return execute(-1, null, suppliers);
    }

    /**
     * 并发执行任务，出现异常则返回null
     */
    public static <T> List<T> execute(long timeout, TimeUnit unit, Supplier<T>... suppliers) {
        if (suppliers == null || suppliers.length == 0) {
            return new ArrayList<>();
        }
        List<T> result = new ArrayList<>();
        for (CompletableFuture<T> future : getCompletableFuture(suppliers)) {
            T t = null;
            try {
                t = timeout > 0 ? future.get(timeout, unit) : future.get();
            } catch (Exception e) {
                log.error("future.get exception", e);
            }
            result.add(t);
        }
        return result;
    }

    private static <T> List<CompletableFuture<T>> getCompletableFuture(Supplier<T>[] suppliers) {
        List<CompletableFuture<T>> list = new ArrayList<>();
        for (Supplier<T> supplier : suppliers) {
            CompletableFuture<T> future = CompletableFuture.supplyAsync(supplier, executor);
            list.add(future);
        }
        return list;
    }

    /**
     * 异步执行
     */
    public static List<CompletableFuture<Void>> executeRunnable(Runnable... tasks) {
        if (tasks == null || tasks.length == 0) {
            return new ArrayList<>();
        }
        List<CompletableFuture<Void>> result = new ArrayList<>();
        for (Runnable task : tasks) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    task.run();
                } catch (Exception e) {
                    log.error("task.run exception", e);
                }
            }, executor);
            result.add(future);
        }
        return result;
    }
}