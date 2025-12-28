package com.snek.fancyplayershops.data.data_types;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.main.ProductDisplay;







//FIXME make subclasses and move the generic code to framework lib. automate data saving
//FIXME - PersistentData
//FIXME - PersistentDataManager
public class Shop {
    private boolean scheduledForSave = false;
    private boolean dissolved = false;
    public boolean isScheduledForSave() { return scheduledForSave; }
    public boolean isDissolved() { return dissolved; }
    public void setScheduledForSave(final boolean scheduled) { scheduledForSave = scheduled; }

    // Shop data
    private final @NotNull UUID   ownerUuid;
    private final @NotNull UUID   uuid;
    private @NotNull String displayName;
    //TODO add icon - 8x pixel art

    // Runtime data
    private long balance;
    private final @NotNull List<@NotNull ProductDisplay> displays;
    //TODO ^ this prob doesn't get updated correctly when loading in.
    //TODO shops are saved when they load into the world, not all at once when the server starts. though im not sure
    //TODO check this

    // Getters
    public @NotNull UUID                          getOwnerUuid  () { return ownerUuid;   }
    public @NotNull UUID                          getUuid       () { return uuid;        }
    public @NotNull String                        getDisplayName() { return displayName; }
    public          long                          getBalance    () { return balance;     }
    public @NotNull List<@NotNull ProductDisplay> getDisplays   () { return displays;    }

    // Setters
    public void setDisplayName(final @NotNull String _displayName) { displayName = _displayName; }
    public void addBalance(final long amount) { balance += amount; ShopManager.scheduleShopSave(this); }
    public void subBalance(final long amount) { balance -= amount; ShopManager.scheduleShopSave(this); }


    public void addDisplay(final @NotNull ProductDisplay display) {
        displays.add(display);
        //TODO update shop list UIs
        //TODO also update the list of items in any buyer's HUD
    }


    public void removeDisplay(final @NotNull ProductDisplay display) {
        displays.remove(display);
        //TODO update shop list UIs
        //TODO also update the list of items in any buyer's HUD
    }




    /**
     * Creates a new empty shop with 0 balance and a random UUID.
     * @param _displayName The display name of the shop.
     */
    public Shop(final @NotNull String _displayName, final @NotNull UUID _ownerUuid) {
        this(_displayName, UUID.randomUUID(), _ownerUuid);
    }


    /**
     * Creates a shop with 0 balance using the specified data.
     * @param _displayName The display name of the shop.
     * @param _uuid The UUID of the shop. This must be unique among a player's shops.
     * @param _ownerUuid The UUID of the owner.
     */
    public Shop(final @NotNull String _displayName, final @NotNull UUID _uuid, final @NotNull UUID _ownerUuid) {
        uuid = _uuid;
        ownerUuid = _ownerUuid;
        displayName = _displayName;
        balance = 0;
        displays = new ArrayList<>();
    }


    /**
     * Claims the balance of all the displays in this shop, sending it to the owner's balance.
    */
    public void claimBalance() {
        for(final ProductDisplay s : displays) {
            s.claimBalance();
        }
    }


    /**
     * Removes all of the displays from this shop, then flags it as dissolved (which prevents it from getting saved to file).
     * <p>
     * This method doesn't claim the balance as removing all of the displays already results in the shop having 0 balance.
     */
    public void dissolve() {
        for(final ProductDisplay display : displays) {
            ShopManager.unregisterDisplay(display);
        }
        dissolved = true;
    }
}
