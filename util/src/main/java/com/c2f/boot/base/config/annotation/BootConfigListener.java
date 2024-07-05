package com.c2f.boot.base.config.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface BootConfigListener {
    String namespace() default "public";

    String dataId();

    String group() default "DEFAULT_GROUP";

    long timeout() default 5000L;

    String type() default "yaml";

    PropertyChange[] propertyChanges() default {};
}
