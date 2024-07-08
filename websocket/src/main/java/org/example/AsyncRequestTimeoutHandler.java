package org.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

@Slf4j
@RestControllerAdvice
public class AsyncRequestTimeoutHandler {

    //    @ResponseStatus(HttpStatus.NOT_MODIFIED)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public String asyncRequestTimeoutHandler(AsyncRequestTimeoutException e) {
        log.error("异步请求超时");
        return "请求超时";
    }
}
