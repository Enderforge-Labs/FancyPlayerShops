package com.snek.fancyplayershops.data;

import java.util.HashMap;
import java.util.UUID;




public class PlayerStash extends HashMap<UUID, StashEntry> {
    private boolean scheduledForSave = false;
    public boolean isScheduledForSave() { return scheduledForSave; }
    public void setScheduledForSave(final boolean scheduled) { scheduledForSave = scheduled; }


    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
