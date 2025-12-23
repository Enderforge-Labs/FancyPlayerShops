package com.snek.fancyplayershops.data.data_types;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.data.ShopGroupManager;
import com.snek.fancyplayershops.main.Shop;







//FIXME make subclasses and move the generic code to framework lib. automate data saving
//FIXME - PersistentData
//FIXME - PersistentDataManager
public class ShopGroup {
    private boolean scheduledForSave = false;
    private boolean dissolved = false;
    public boolean isScheduledForSave() { return scheduledForSave; }
    public boolean isDissolved() { return dissolved; }
    public void setScheduledForSave(final boolean scheduled) { scheduledForSave = scheduled; }

    // Group data
    private @NotNull UUID   ownerUuid;
    private @NotNull UUID   uuid;
    private @NotNull String displayName;
    //TODO add icon - 8x pixel art

    // Runtime data
    private long balance;
    private @NotNull List<@NotNull Shop> shops;
    //TODO ^ this prob doesn't get updated correctly when loading in.
    //TODO shops are saved when they load into the world, not all at once when the server starts. though im not sure
    //TODO check this

    // Getters
    public @NotNull UUID                getOwnerUuid  () { return ownerUuid;   }
    public @NotNull UUID                getUuid       () { return uuid;        }
    public @NotNull String              getDisplayName() { return displayName; }
    public          long                getBalance    () { return balance;     }
    public @NotNull List<@NotNull Shop> getShops      () { return shops;       }

    // Setters
    public void setDisplayName(final @NotNull String _displayName) { displayName = _displayName; }
    public void addBalance(final long amount) { balance += amount; ShopGroupManager.scheduleGroupSave(this); }
    public void subBalance(final long amount) { balance -= amount; ShopGroupManager.scheduleGroupSave(this); }


    public void addShop(final @NotNull Shop shop) {
        shops.add(shop);
        //TODO update shop list UIs
        //TODO also update the list of items in any buyer's HUD
    }


    public void removeShop(final @NotNull Shop shop) {
        shops.remove(shop);
        //TODO update shop list UIs
        //TODO also update the list of items in any buyer's HUD
    }




    /**
     * Creates a new empty shop group with 0 balance and a random UUID.
     * @param _displayName The display name of the group.
     */
    public ShopGroup(final @NotNull String _displayName, final @NotNull UUID _ownerUuid) {
        this(_displayName, UUID.randomUUID(), _ownerUuid);
    }


    /**
     * Creates a shop group with 0 balance using the specified data.
     * @param _displayName The display name of the group.
     * @param _uuid The UUID of the group. This must be unique among a player's shop groups.
     * @param _ownerUuid The UUID of the owner.
     */
    public ShopGroup(final @NotNull String _displayName, final @NotNull UUID _uuid, final @NotNull UUID _ownerUuid) {
        uuid = _uuid;
        ownerUuid = _ownerUuid;
        displayName = _displayName;
        balance = 0;
        shops = new ArrayList<>();
    }


    /**
     * Claims the balance of all the shops in this group, sending it to the owner's balance.
    */
    public void claimBalance() {
        for(final Shop s : shops) {
            s.claimBalance();
        }
    }


    /**
     * Removes all of the shops from this group, then flags it as dissolved (which prevents it from getting saved to file).
     * <p>
     * This method doesn't claim the balance as removing all of the shops already results in the group having 0 balance.
     */
    public void dissolve() {
        for(final Shop shop : shops) {
            ShopGroupManager.unregisterShop(shop);
        }
        dissolved = true;
    }
}
