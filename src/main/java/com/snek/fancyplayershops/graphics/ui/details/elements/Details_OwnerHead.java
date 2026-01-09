package com.snek.fancyplayershops.graphics.ui.details.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.graphics.ui.details.styles.Details_OwnerHead_S;
import com.snek.frameworklib.graphics.basic.elements.ItemElm;
import com.snek.frameworklib.graphics.basic.styles.ItemStyle;
import com.snek.frameworklib.utils.MinecraftUtils;

import net.minecraft.world.item.ItemStack;








/**
 * A UI element that displays the head of the owner of the product display.
 */
public class Details_OwnerHead extends ItemElm {
    private boolean suppressSpawn = false;


    /**
     * Creates a new DetailsUiOwnerHead.
     * @param display The target product display.
     */
    public Details_OwnerHead(final @NotNull ProductDisplay display) {
        super(display.getLevel(), new Details_OwnerHead_S());
    }




    /**
     * Updates the displayed head using the owner's uuid.
     */
    public void updateDisplay() {
        final ProductDisplay display = GetDisplay.get(this);
        final @Nullable ItemStack head = MinecraftUtils.getOfflinePlayerHead(display.getOwnerUuid());
        if(head == null) suppressSpawn = true; //! Prevent rendering the default skins in case of cached player data lookup issues
        else getStyle(ItemStyle.class).setItem(head);
    }




    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        updateDisplay();
        if(!suppressSpawn) super.spawn(pos, animate);
    }
}