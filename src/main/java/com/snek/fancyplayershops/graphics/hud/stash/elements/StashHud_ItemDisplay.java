package com.snek.fancyplayershops.graphics.hud.stash.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.fancyplayershops.graphics.hud.stash.styles.StashHud_ItemDisplay_S;
import com.snek.frameworklib.graphics.basic.elements.ItemElm;
import com.snek.frameworklib.graphics.basic.styles.ItemElmStyle;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.ItemStack;








//FIXME make not clickable. this was only used for testing
public class StashHud_ItemDisplay extends ItemElm implements Clickable {
    private final ItemStack item;


    /**
     * Creates a new StashHud_ItemDisplay.
     * @param _hud The parent HUD.
     */
    public StashHud_ItemDisplay(final @NotNull HudContext _hud, final @NotNull ItemStack _item) {
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
        flushStyle(false);
    }


    @Override
    public boolean attemptClick(@NotNull Player player, @NotNull ClickAction click) {
        return true;
    }


    @Override
    public void onClick(@NotNull Player player, @NotNull ClickAction click) {
    }
}
