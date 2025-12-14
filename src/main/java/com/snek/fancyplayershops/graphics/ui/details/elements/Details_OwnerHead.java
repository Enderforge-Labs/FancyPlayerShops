package com.snek.fancyplayershops.graphics.ui.details.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.GetShop;
import com.snek.fancyplayershops.graphics.ui.details.styles.Details_OwnerHead_S;
import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.graphics.basic.elements.ItemElm;
import com.snek.frameworklib.graphics.basic.styles.ItemElmStyle;
import com.snek.frameworklib.utils.MinecraftUtils;

import net.minecraft.world.item.ItemStack;








/**
 * A UI element that displays the head of the owner of the shop.
 */
public class Details_OwnerHead extends ItemElm {
    private boolean suppressSpawn = false;


    /**
     * Creates a new DetailsUiOwnerHead.
     * @param _shop The target shop.
     */
    public Details_OwnerHead(final @NotNull Shop _shop) {
        super(_shop.getLevel(), new Details_OwnerHead_S());
    }




    /**
     * Updates the displayed head using the owner's uuid.
     */
    public void updateDisplay() {
        final Shop shop = GetShop.get(this);
        final @Nullable ItemStack head = MinecraftUtils.getOfflinePlayerHead(shop.getOwnerUuid(), FrameworkLib.getServer());
        if(head == null) suppressSpawn = true; //! Prevent rendering the default skins in case of cached player data lookup issues
        else getStyle(ItemElmStyle.class).setItem(head);
    }




    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        updateDisplay();
        if(!suppressSpawn) super.spawn(pos, animate);
    }
}