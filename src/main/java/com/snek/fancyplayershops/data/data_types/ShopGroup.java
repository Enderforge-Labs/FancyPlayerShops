package com.snek.fancyplayershops.data.data_types;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;







//FIXME make subclasses and move the generic code to framework lib. automate data saving
//FIXME - PersistentData
//FIXME - PersistentDataManager
public class ShopGroup {
    private boolean scheduledForSave = false;
    public boolean isScheduledForSave() { return scheduledForSave; }
    public void setScheduledForSave(final boolean scheduled) { scheduledForSave = scheduled; }

    // Group data
    private @NotNull UUID   uuid;
    private @NotNull String displayName;
    //TODO add icon - 8x pixel art

    // Runtime data
    private long balance;
    private @NotNull List<@NotNull Shop> shops;

    // Getters
    public @NotNull UUID                getUuid       () { return uuid;        }
    public @NotNull String              getDisplayName() { return displayName; }
    public          long                getBalance    () { return balance;     }
    public @NotNull List<@NotNull Shop> getShops      () { return shops;       }

    // Setters
    public void setDisplayName(final @NotNull String _displayName) { displayName = _displayName; }
    public void addBalance(final long amount) { balance += amount; }
    public void subBalance(final long amount) { balance -= amount; }


    public void addShop   (final @NotNull Shop shop) {
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
    public ShopGroup(final @NotNull String _displayName) {
        this(_displayName, UUID.randomUUID());
    }


    /**
     * Creates a shop group with 0 balance using the specified data.
     * @param _displayName The display name of the group.
     * @param _uuid The UUID of the group. This must be unique among a player's shop groups.
     */
    public ShopGroup(final @NotNull String _displayName, final @NotNull UUID _uuid) {
        uuid = _uuid;
        displayName = _displayName;
        balance = 0;
        shops = new ArrayList<>();
    }
}
