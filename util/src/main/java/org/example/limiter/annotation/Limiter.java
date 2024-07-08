package org.example.limiter.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Limiter {

    //时间
    long period() default 2_000;

    //次数
    long permit() default 2;

    long timeout() default -1;

    boolean avoidRepeat() default false;

//    String[] headers() default {};

    //如果表达式非空则仅当入参满足表达式才进行限流
    String expression() default "";


}
