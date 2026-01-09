package com.snek.fancyplayershops.data.data_types;

import java.util.HashMap;
import java.util.UUID;




//FIXME make subclasses and move the generic code to framework lib. automate data saving
//FIXME - PersistentData
//FIXME - PersistentDataManager
public class PlayerStash extends HashMap<UUID, StashEntry> {
    private boolean scheduledForSave = false;
    public boolean isScheduledForSave() { return scheduledForSave; }
    public void setScheduledForSave(final boolean scheduled) { scheduledForSave = scheduled; }


    @Override
    public boolean equals(final Object other) {
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
