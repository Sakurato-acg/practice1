package org.example.limiter.aop;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.limiter.annotation.Limiter;
import org.example.limiter.util.RateLimiterUtil;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Aspect
@Component
public class AopAspect {

    //@Around("@annotation(org.example.limiter.annotation.Limiter)")
    @Around("execution(* *..controller..*.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        Limiter annotation = AnnotationUtils.findAnnotation(method, Limiter.class);
        if (null == annotation) {
            annotation = AnnotationUtils.findAnnotation(point.getSignature().getDeclaringType(), Limiter.class);
        }
        if (null == annotation) {
            return point.proceed();
        }

        //  TODO: 组装key
        StringBuffer buffer = new StringBuffer();
        HttpServletRequest request = getRequest();
        buffer.append(getFromIp(request));
        buffer.append(request.getServletPath());

        //是否用于幂等性
        if (annotation.avoidRepeat()) {
            avoidRepeat(buffer, request, point);
        }
        String key = buffer.toString();


        boolean acquire;
//        boolean hasThrowable = false;

        acquire = RateLimiterUtil.tryAcquire(key, annotation.period(), annotation.permit(), annotation.timeout());

        if (acquire) {
            return point.proceed();
        } else {
            throw new RuntimeException("令牌获取失败");
        }
    }

    private static void avoidRepeat(StringBuffer buffer, HttpServletRequest request, ProceedingJoinPoint point) {
        // 1.入参的key和value
        // 1.1 values
        Object[] args = point.getArgs();
        // 1.2 keys
        String[] paramNames = ((CodeSignature) point.getSignature()).getParameterNames();
        // 1.3 转json
        Map<String, Object> map = new ConcurrentHashMap<>();
        for (int i = 0; i < args.length; i++) {
            map.put(paramNames[i], args[i]);
        }
        buffer.append(JSON.toJSONString(map));

        //2. ua
        buffer.append(request.getHeader("user-agent"));

    }

    public static HttpServletRequest getRequest() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        assert sra != null;
        return sra.getRequest();
    }

    public static String getFromIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip) && ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;

    }
}

