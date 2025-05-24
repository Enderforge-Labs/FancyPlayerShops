package com.snek.fancyplayershops.hud_ui.stash.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.hud_ui._elements.Hud;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.ui.elements.ItemElm;
import com.snek.framework.ui.elements.styles.ItemElmStyle;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;








public class StashHud_ItemDisplay extends ItemElm {
    final ItemStack item;


    /**
     * Creates a new StashHud_ItemDisplay.
     * @param _hud The parent HUD.
     */
    public StashHud_ItemDisplay(final @NotNull Hud _hud, final @NotNull ItemStack _item) {
        super((ServerLevel)(_hud.getPlayer().level()));
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


    @Override
    protected @NotNull Transform __calcTransform() {
        return super.__calcTransform()
            .scale(0.075f)
        ;
    }
}
