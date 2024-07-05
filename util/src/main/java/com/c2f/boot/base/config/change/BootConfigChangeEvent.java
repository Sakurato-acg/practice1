package com.c2f.boot.base.config.change;

import java.util.Collection;
import java.util.Map;

public class BootConfigChangeEvent {
    private final Map<String, BootConfigChangeItem> data;

    public BootConfigChangeEvent(Map<String, BootConfigChangeItem> data) {
        this.data = data;
    }

    public BootConfigChangeItem getChangeItem(String key) {
        return (BootConfigChangeItem) this.data.get(key);
    }

    public Collection<BootConfigChangeItem> getChangeItems() {
        return this.data.values();
    }
}
