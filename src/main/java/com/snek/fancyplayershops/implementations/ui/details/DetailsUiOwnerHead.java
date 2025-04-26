package com.snek.fancyplayershops.implementations.ui.details;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.misc.ShopItemElm;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.ui.styles.ItemElmStyle;
import com.snek.framework.utils.MinecraftUtils;

import net.minecraft.item.ItemStack;








/**
 * A UI element that displays the head of the owner of the shop.
 */
public class DetailsUiOwnerHead extends ShopItemElm {
    private boolean suppressSpawn = false;


    /**
     * Creates a new DetailsUiOwnerHead.
     * @param _shop The target shop.
     */
    public DetailsUiOwnerHead(Shop _shop) {
        super(_shop);
        applyAnimationNow(new Transition().additiveTransform(new Transform().scale(DetailsUi.HEAD_SIZE).scaleZ(0.001f).rotY((float)Math.PI)));
        updateDisplay();
    }




    /**
     * Updates the displayed head using the owner's uuid.
     */
    public void updateDisplay(){
        final @Nullable ItemStack head = MinecraftUtils.getOfflinePlayerHead(shop.getOwnerUuid(), shop.getWorld().getServer());
        if(head == null) suppressSpawn = true; //! Prevent rendering the default skins in case of skin lookup issues
        else ((ItemElmStyle)style).setItem(head);
    }




    @Override
    public void spawn(Vector3d pos) {
        if(!suppressSpawn) super.spawn(pos);
    }
}
