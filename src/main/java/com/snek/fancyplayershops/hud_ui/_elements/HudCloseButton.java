package com.snek.fancyplayershops.hud_ui._elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.ui._elements.UiCanvas;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.data_types.ui.PolylineData;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.composite.elements.PolylineSetElm;
import com.snek.framework.ui.functional.elements.SimpleButtonElm;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class HudCloseButton extends SimpleButtonElm {


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




    public HudCloseButton(final @NotNull ServerLevel _world) {
        super(_world, 1); //TODO add input indicator

        // Create design
        final Div e = addChild(new PolylineSetElm(world, design));
        e.setSize(new Vector2f(UiCanvas.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        Hud.closeHud(player);
    }
}
