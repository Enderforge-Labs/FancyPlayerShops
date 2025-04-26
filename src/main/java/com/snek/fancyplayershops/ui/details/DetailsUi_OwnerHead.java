package com.snek.fancyplayershops.ui.details;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.details.styles.DetailsUi_OwnerHead_S;
import com.snek.fancyplayershops.ui.misc.ShopItemElm;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.ui.styles.ItemElmStyle;
import com.snek.framework.utils.MinecraftUtils;

import net.minecraft.item.ItemStack;








/**
 * A UI element that displays the head of the owner of the shop.
 */
public class DetailsUi_OwnerHead extends ShopItemElm {
    private boolean suppressSpawn = false;


    /**
     * Creates a new DetailsUiOwnerHead.
     * @param _shop The target shop.
     */
    public DetailsUi_OwnerHead(Shop _shop) {
        super(_shop, new DetailsUi_OwnerHead_S());
        updateDisplay();
    }




    /**
     * Updates the displayed head using the owner's uuid.
     */
    public void updateDisplay(){
        final @Nullable ItemStack head = MinecraftUtils.getOfflinePlayerHead(shop.getOwnerUuid(), shop.getWorld().getServer());
        if(head == null) suppressSpawn = true; //! Prevent rendering the default skins in case of cached player data lookup issues
        else ((ItemElmStyle)style).setItem(head);
    }




    @Override
    public void spawn(Vector3d pos) {
        if(!suppressSpawn) super.spawn(pos);
    }
}
