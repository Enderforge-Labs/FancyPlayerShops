package com.snek.fancyplayershops.graphics.hud.mod_info.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.fancyplayershops.graphics.hud.mod_info.styles.ModInfo_0_DisplayItem_S;
import com.snek.frameworklib.graphics.basic.elements.ItemElm;
import com.snek.frameworklib.graphics.basic.styles.ItemElmStyle;








public class ModInfo_0_DisplayItem extends ItemElm {


    /**
     * Creates a new ModInfo_0_DisplayItem.
     * @param _hud The parent HUD.
     */
    public ModInfo_0_DisplayItem(final @NotNull HudContext _hud) {
        super(_hud.getLevel(), new ModInfo_0_DisplayItem_S());
        getStyle(ItemElmStyle.class).setItem(ProductDisplayManager.getProductDisplayItemCopy());
    }
}
