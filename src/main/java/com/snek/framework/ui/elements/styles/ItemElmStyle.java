package com.snek.framework.ui.elements.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.framework.data_types.containers.Flagged;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;








/**
 * The default style of the generic ItemElm UI element.
 */
public class ItemElmStyle extends ElmStyle {
    private @NotNull Flagged<@NotNull ItemStack> item = null;




    /**
     * Creates a new ItemElmStyle.
     */
    public ItemElmStyle() {
        super();
    }


    @Override
    public void resetAll() {
        resetItem();
        super.resetAll();
    }




    public @NotNull ItemStack getDefaultItem() { return Items.AIR.getDefaultStack(); }
    public void resetItem() { item = Flagged.from(getDefaultItem()); }
    public void setItem(final @NotNull ItemStack _item) { item.set(_item); }
    public @NotNull Flagged<@NotNull ItemStack> getFlaggedItem() { return item; }
    public @NotNull ItemStack getItem() { return item.get(); }
    public @NotNull ItemStack editItem() { return item.edit(); }
}
