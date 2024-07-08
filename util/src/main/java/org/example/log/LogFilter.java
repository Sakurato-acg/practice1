package org.example.log;

import cn.hutool.core.util.IdUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class LogFilter extends OncePerRequestFilter implements Ordered {

    private final static String MDC_TRACE_ID = "traceId";

    /**
     * 实现 TransmittableThreadLocal 的 initialValue,beforeExecute,afterExecute接口
     */
    static TransmittableThreadLocal<Map<String, String>> ttlMDC = new TransmittableThreadLocal<>() {

        // 在多线程数据传递的时候，将数据复制一份给MDC
        @Override
        protected void beforeExecute() {
            final Map<String, String> mdc = get();
            mdc.forEach(MDC::put);
        }

        @Override
        protected void afterExecute() {
            MDC.clear();
        }

        @Override
        protected Map<String, String> initialValue() {
            return new HashMap<>();
        }

    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String mdcTraceId = getMdcTraceId();
        MDC.put(MDC_TRACE_ID, mdcTraceId);
        log.info("纯字符串信息的info级别日志");
        //同时给TransmittableThreadLocal记录traceId
        ttlMDC.get().put("traceId", mdcTraceId);

        filterChain.doFilter(request, response);
        MDC.clear();

        ttlMDC.get().clear();
        ttlMDC.remove();
    }

    private String getMdcTraceId() {
        String idStr = IdUtil.getSnowflakeNextIdStr();
        return String.join("_", MDC_TRACE_ID, idStr);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
