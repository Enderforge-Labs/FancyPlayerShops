package com.snek.fancyplayershops.shop_ui.details.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.shop_ui.details.styles.DetailsUi_OwnerHead_S;
import com.snek.fancyplayershops.shop_ui.misc.elements.ShopItemElm;
import com.snek.framework.old.ui.basic.styles.ItemElmStyle;
import com.snek.framework.old.utils.MinecraftUtils;

import net.minecraft.world.item.ItemStack;








/**
 * A UI element that displays the head of the owner of the shop.
 */
public class DetailsUi_OwnerHead extends ShopItemElm {
    private boolean suppressSpawn = false;


    /**
     * Creates a new DetailsUiOwnerHead.
     * @param _shop The target shop.
     */
    public DetailsUi_OwnerHead(final @NotNull Shop _shop) {
        super(_shop, new DetailsUi_OwnerHead_S());
        updateDisplay();
    }




    /**
     * Updates the displayed head using the owner's uuid.
     */
    public void updateDisplay() {
        final @Nullable ItemStack head = MinecraftUtils.getOfflinePlayerHead(shop.getOwnerUuid(), FancyPlayerShops.getServer());
        if(head == null) suppressSpawn = true; //! Prevent rendering the default skins in case of cached player data lookup issues
        else getStyle(ItemElmStyle.class).setItem(head);
    }




    @Override
    public void spawn(Vector3d pos) {
        if(!suppressSpawn) super.spawn(pos);
    }
}