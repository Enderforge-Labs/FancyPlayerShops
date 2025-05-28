package com.snek.fancyplayershops.hud_ui.stash.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.hud_ui._elements.Hud;
import com.snek.fancyplayershops.hud_ui._elements.__HudElm;
import com.snek.fancyplayershops.hud_ui.stash.styles.StashHud_ItemDisplay_S;
import com.snek.framework.ui.basic.elements.ItemElm;
import com.snek.framework.ui.basic.styles.ItemElmStyle;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;








public class StashHud_ItemDisplay extends ItemElm implements __HudElm {
    private final ItemStack item;


    /**
     * Creates a new StashHud_ItemDisplay.
     * @param _hud The parent HUD.
     */
    public StashHud_ItemDisplay(final @NotNull Hud _hud, final @NotNull ItemStack _item) {
        super((ServerLevel)(_hud.getPlayer().level()), new StashHud_ItemDisplay_S());
        item = _item;
        updateDisplay();
    }


    /**
     * Updates the displayed item.
     */
    public void updateDisplay() {

        // Set the item
        getStyle(ItemElmStyle.class).setItem(item);

        // Update the entity
        flushStyle();
    }
}
