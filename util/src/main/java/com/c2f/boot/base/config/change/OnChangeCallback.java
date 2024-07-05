package com.c2f.boot.base.config.change;

@FunctionalInterface
public interface OnChangeCallback {
    void onChange(Object configInfo) throws Exception;
}
