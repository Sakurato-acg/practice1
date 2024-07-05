package com.c2f.boot.base.config.change;

import com.c2f.boot.base.config.constant.BootPropertyChangeType;

public class BootConfigChangeItem {
    private String key;
    private String oldValue;

    private String newValue;
    private BootPropertyChangeType type;

    public BootConfigChangeItem(String key, String oldValue, String newValue) {
        this.key = key;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public BootConfigChangeItem(String key, String oldValue, String newValue, BootPropertyChangeType type) {
        this.key = key;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public BootPropertyChangeType getType() {
        return type;
    }

    public void setType(BootPropertyChangeType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BootConfigChangeItem{" +
                "key='" + key + '\'' +
                ", oldValue='" + oldValue + '\'' +
                ", newValue='" + newValue + '\'' +
                ", type=" + type +
                '}';
    }
}
