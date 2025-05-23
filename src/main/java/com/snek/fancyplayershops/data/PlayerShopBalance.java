package com.snek.fancyplayershops.data;

import org.jetbrains.annotations.NotNull;








public class PlayerShopBalance {
    private long value;
    public long getValue() { return value; }


    private boolean scheduledForSave = false;
    public boolean isScheduledForSave() { return scheduledForSave; }
    public void setScheduledForSave(final boolean scheduled) { scheduledForSave = scheduled; }


    public PlayerShopBalance(final long _value) {
        value = _value;
    }
    public PlayerShopBalance() {
        this(0);
    }


    public static PlayerShopBalance merge(final @NotNull PlayerShopBalance a, final PlayerShopBalance b) {
        a.value += b.value;
        return a;
    }
}
