package com.c2f.boot.base.config.annotation;

import com.c2f.boot.base.config.constant.BootPropertyChangeType;

public @interface PropertyChange {
    BootPropertyChangeType[] changeTypes() default {
            BootPropertyChangeType.ADDED,
            BootPropertyChangeType.DELETED,
            BootPropertyChangeType.MODIFIED
    };

    String pattern();

    boolean isPrefix() default false;
}
