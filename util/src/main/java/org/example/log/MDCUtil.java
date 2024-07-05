package org.example.log;

import org.slf4j.MDC;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @Description 封装MDC用于向线程池传递
 */
public class MDCUtil {
    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            if (CollectionUtils.isEmpty(context)) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            try {
                return callable.call();
            } finally {//清除子线程的，避免内存溢出，就和ThreadLocal.remove()一个原因
                MDC.clear();
            }
        };
    }

 public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            //setTraceIdIfAbsent();
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }

    public static void setMDCContextMap(final Map<String, String> context) {
        if (CollectionUtils.isEmpty(context)) {
            MDC.clear();
        } else {
            MDC.setContextMap(context);
        }
    }

}
