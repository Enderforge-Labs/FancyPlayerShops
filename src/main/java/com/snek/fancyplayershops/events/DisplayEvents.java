package com.snek.fancyplayershops.events;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.data.data_types.Shop;
import com.snek.fancyplayershops.events.data.DisplayCreationReason;
import com.snek.fancyplayershops.events.data.DisplayRemovalReason;
import com.snek.fancyplayershops.main.ProductDisplay;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;





public class DisplayEvents {

    public static final Event<ShopStockChanged> STOCK_CHANGED =
        EventFactory.createArrayBacked(ShopStockChanged.class,
            callbacks -> (display, oldStock, newStock) -> {
                for(ShopStockChanged callback : callbacks) {
                    callback.onStockChange(display, oldStock, newStock);
                }
            }
        )
    ;

    //TODO actually fire the events
    public static final Event<ItemsSold> ITEMS_SOLD =
        EventFactory.createArrayBacked(ItemsSold.class,
            callbacks -> (display, buyer, item, amount) -> {
                for(ItemsSold callback : callbacks) {
                    callback.onItemsSell(display, buyer, item, amount);
                }
            }
        )
    ;

    // //TODO
    // public static final Event<OrderPlaced> ORDER_PLACED =
    //     EventFactory.createArrayBacked(OrderPlaced.class,
    //         callbacks -> (display, oldStock, newStock) -> {
    //             for(OrderPlaced callback : callbacks) {
    //                 callback.onOrderPlace(display, oldStock, newStock);
    //             }
    //         }
    //     )
    // ;

    //TODO actually fire the events
    public static final Event<DisplayCreated> DISPLAY_CREATED =
        EventFactory.createArrayBacked(DisplayCreated.class,
            callbacks -> (display, reason) -> {
                for(DisplayCreated callback : callbacks) {
                    callback.onDisplayCreate(display, reason);
                }
            }
        )
    ;

    //TODO actually fire the events
    public static final Event<DisplayRemoved> DISPLAY_REMOVED =
        EventFactory.createArrayBacked(DisplayRemoved.class,
            callbacks -> (display, reason) -> {
                for(DisplayRemoved callback : callbacks) {
                    callback.onDisplayRemove(display, reason);
                }
            }
        )
    ;

    //TODO actually fire the events
    public static final Event<DisplayTransferred> DISPLAY_TRANSFERRED =
        EventFactory.createArrayBacked(DisplayTransferred.class,
            callbacks -> (display, prevOwner, newOwner) -> {
                for(DisplayTransferred callback : callbacks) {
                    callback.onDisplayTransfer(display, prevOwner, newOwner);
                }
            }
        )
    ;

    //TODO actually fire the events
    public static final Event<DisplayMoved> DISPLAY_MOVED =
        EventFactory.createArrayBacked(DisplayMoved.class,
            callbacks -> (display, prevShop, newShop) -> {
                for(DisplayMoved callback : callbacks) {
                    callback.onDisplayMove(display, prevShop, newShop);
                }
            }
        )
    ;


    @FunctionalInterface
    public interface ShopStockChanged {
        void onStockChange(@NotNull ProductDisplay display, long oldStock, long newStock);
    }

    @FunctionalInterface
    public interface ItemsSold {
        void onItemsSell(@NotNull ProductDisplay display, @NotNull Player buyer, @NotNull ItemStack item, long amount);
    }

    // //TODO
    // @FunctionalInterface
    // public interface OrderPlaced {
    //     void onOrderPlace(@NotNull ProductDisplay display, long oldStock, long newStock);
    // }

    @FunctionalInterface
    public interface DisplayCreated {
        void onDisplayCreate(@NotNull ProductDisplay display, @NotNull DisplayCreationReason reason);
    }

    @FunctionalInterface
    public interface DisplayRemoved {
        void onDisplayRemove(@NotNull ProductDisplay display, @NotNull DisplayRemovalReason reason);
    }

    @FunctionalInterface
    public interface DisplayTransferred {
        void onDisplayTransfer(@NotNull ProductDisplay display, @NotNull Player prevOwner, @NotNull Player newOwner);
    }

    @FunctionalInterface
    public interface DisplayMoved {
        void onDisplayMove(@NotNull ProductDisplay display, @Nullable Shop prevShop, @NotNull Shop newShop);
    }
}