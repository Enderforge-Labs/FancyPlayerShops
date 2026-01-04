package com.snek.fancyplayershops.graphics.hud.main_menu.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.fancyplayershops.graphics.misc.styles.SimpleTextElmStyle_Small;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.world.entity.player.Player;





public class MainMenu_Summary extends SimpleTextElm {

    public MainMenu_Summary(final @NotNull HudContext context) {
        super(context.getLevel(), new SimpleTextElmStyle_Small());
    }


    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        updateDisplay();
        super.spawn(pos, animate);
    }


    public void updateDisplay() {
        //TODO update auomtically? idk
        final Player player = getCanvas().getContext().getPlayer();
        getStyle(SimpleTextElmStyle.class).setText(new Txt()
            .cat(new Txt("You own ").white())
            .cat(new Txt("" + ProductDisplayManager.getDisplaysByOwner().get(player.getUUID()).size()).color(ProductDisplayManager.DISPLAY_ITEM_NAME_COLOR))
            .cat(new Txt(" displays.").white())
            .cat("\n")
            .cat(new Txt("You sold a total of ").white())
            .cat(new Txt(Utils.formatAmountShort(65_533_745l)).gold()) //TODO actually get the stats
            .cat(new Txt(" products.").white())
            .cat("\n")
            .cat(new Txt("You earned a total of ").white())
            .cat(new Txt(Utils.formatPriceShort(4_621_564_234_00l)).gold()) //TODO actually get the stats
            .cat(new Txt(".").white())
        .get());
    }
}
