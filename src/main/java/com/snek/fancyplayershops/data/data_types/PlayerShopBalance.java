package com.snek.fancyplayershops.data.data_types;

import org.jetbrains.annotations.NotNull;








//FIXME make subclasses and move the generic code to framework lib. automate data saving
//FIXME - PersistentData
//FIXME - PersistentDataManager
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
