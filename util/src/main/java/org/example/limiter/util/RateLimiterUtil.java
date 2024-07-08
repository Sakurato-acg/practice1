package org.example.limiter.util;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RateLimiterUtil {
    private static final String RATE_LIMITER_PREFIX = "rl:";

    private static RedissonClient redissonClient;

    public static boolean disableRedisson() {
        return null == redissonClient;
    }

    /**
     * @param key    key
     * @param period 时间窗口
     * @param permit limit_count
     * @return 获取结果
     */
    public static boolean tryAcquire(String key, long period, int permit) {
        return tryAcquire(key, period, permit, -1);
    }

    public static boolean tryAcquire(String key, long period, long permit, long timeout) {
        return handlerLimiter(() -> {
            RRateLimiter rateLimiter = redissonClient.getRateLimiter(RATE_LIMITER_PREFIX + key);
            boolean isExists = rateLimiter.isExists();

            if (!isExists) {
                rateLimiter.setRate(RateType.OVERALL, permit, period, RateIntervalUnit.SECONDS);
                log.info("set");
            } else {
                RateLimiterConfig config = rateLimiter.getConfig();
                //与配置的permit是否一样
                boolean permitChange = permit != config.getRate();
                boolean periodChange = !Objects.equals(period, config.getRateInterval() / 1000);
                if (permitChange || periodChange) {
                    rateLimiter.setRate(
                            RateType.OVERALL,
                            permit,
                            period,
                            RateIntervalUnit.SECONDS
                    );
                    log.info("set");

                    //todo
//                    if (permitChange) {
//                        expire(period, rateLimiter);
//                    }
                }


            }
            boolean acquire;

            if (-1 == timeout) {
                acquire = rateLimiter.tryAcquire();
            } else {
                acquire = rateLimiter.tryAcquire(timeout, TimeUnit.MILLISECONDS);
            }

//            if (!isExists) {
            expire(period, rateLimiter);
//            }

            return acquire;
        });
    }

    public static boolean cancel(String key) {
        try {
            boolean cancelResult = handlerLimiter(() -> {
                RRateLimiter rateLimiter = redissonClient.getRateLimiter(RATE_LIMITER_PREFIX + key);
                return rateLimiter.delete();
            });
            if (!cancelResult) {
                log.warn("cancel limiter fail for key :{}", key);
            }

        } catch (Exception e) {
            log.warn("cancel limiter exception for key :{},exception detail:", key, e);
        }
        return true;
    }

    private static void expire(long period, RRateLimiter rateLimiter) {
        //todo 为什么
        long keyExpire = 2 * period;
        rateLimiter.expireAsync(keyExpire, TimeUnit.MILLISECONDS);
    }

    private static boolean handlerLimiter(RateLimiterCallback callback) {
        if (disableRedisson()) throw new RuntimeException("redisson not load");
        return callback.handleLimiter();
    }

    @Autowired
    public void setRedissonClient(RedissonClient redissonClient) {
        RateLimiterUtil.redissonClient = redissonClient;
    }

    @FunctionalInterface
    private interface RateLimiterCallback {
        boolean handleLimiter() throws RuntimeException;
    }


}
