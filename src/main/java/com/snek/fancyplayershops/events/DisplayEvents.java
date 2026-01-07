package com.snek.fancyplayershops.events;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.ProductDisplay;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;





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


    @FunctionalInterface
    public interface ShopStockChanged {
        void onStockChange(@NotNull ProductDisplay display, long oldStock, long newStock);
    }

    //TODO ITEMS_SOLD onItemsSell
    //TODO ORDER_PLACED onOrderPlace
}