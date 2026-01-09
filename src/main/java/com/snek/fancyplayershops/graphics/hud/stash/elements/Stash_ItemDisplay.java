package com.snek.fancyplayershops.graphics.hud.stash.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.basic.elements.ItemElm;
import com.snek.frameworklib.graphics.basic.presets.ItemStyle_Gui;
import com.snek.frameworklib.graphics.basic.styles.ItemStyle;

import net.minecraft.world.item.ItemStack;








public class Stash_ItemDisplay extends ItemElm {
    private final ItemStack item;


    /**
     * Creates a new StashHud_ItemDisplay.
     * @param _hud The parent HUD.
     */
    public Stash_ItemDisplay(final @NotNull HudContext _hud, final @NotNull ItemStack _item) {
        super(_hud.getLevel(), new ItemStyle_Gui(_item, 0.075f));
        item = _item;
        updateDisplay();
    }


    /**
     * Updates the displayed item.
     */
    public void updateDisplay() {

        // Set the item
        getStyle(ItemStyle.class).setItem(item);

        // Update the entity
        flushStyle();
    }
}
