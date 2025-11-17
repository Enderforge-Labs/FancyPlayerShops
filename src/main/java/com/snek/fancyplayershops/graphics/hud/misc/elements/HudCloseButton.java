package com.snek.fancyplayershops.graphics.hud.misc.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.frameworklib.graphics.hud._elements.Hud;
import com.snek.frameworklib.graphics.hud._elements.__HudElm;
import com.snek.fancyplayershops.graphics.hud.misc.styles.HudCloseButton_S;
import com.snek.frameworklib.graphics.ui._elements.UiCanvas;
import com.snek.frameworklib.data_types.ui.AlignmentX;
import com.snek.frameworklib.data_types.ui.AlignmentY;
import com.snek.frameworklib.data_types.ui.PolylineData;
import com.snek.frameworklib.graphics.Context;
import com.snek.frameworklib.graphics.Div;
import com.snek.frameworklib.graphics.composite.elements.PolylineSetElm;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class HudCloseButton extends HudSimpleButtonElm implements __HudElm {


    private static final @NotNull PolylineData[] design = new PolylineData[] {
        new PolylineData(
            UiCanvas.TOOLBAR_FG_COLOR, UiCanvas.TOOLBAR_FG_ALPHA,
            UiCanvas.TOOLBAR_FG_WIDTH, 0.05f,
            new Vector2f(0.1f, 0.1f),
            new Vector2f(0.9f, 0.9f)
        ),
        new PolylineData(
            UiCanvas.TOOLBAR_FG_COLOR, UiCanvas.TOOLBAR_FG_ALPHA,
            UiCanvas.TOOLBAR_FG_WIDTH, 0.05f,
            new Vector2f(0.1f, 0.9f),
            new Vector2f(0.9f, 0.1f)
        )
    };




    public HudCloseButton(final @NotNull Hud _hud) {
        super(_hud, 1, new HudCloseButton_S()); //TODO add input indicator

        // Create design
        final Div e = addChild(new PolylineSetElm(world, design));
        e.setSize(new Vector2f(UiCanvas.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        Context.closeContext(player);
    }
}
