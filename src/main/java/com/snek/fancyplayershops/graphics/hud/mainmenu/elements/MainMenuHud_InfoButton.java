package com.snek.fancyplayershops.graphics.hud.mainmenu.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.graphics.hud.misc.elements.HudSimpleButton;
import com.snek.fancyplayershops.graphics.hud.misc.styles.HudSquareButton_S;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.data_types.graphics.PolylineData;
import com.snek.frameworklib.graphics.composite.elements.PolylineSetElm;
import com.snek.frameworklib.graphics.core.Canvas;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.layout.Div;

import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.ClickAction;








public class MainMenuHud_InfoButton extends HudSimpleButton {


    private static final @NotNull PolylineData[] design = new PolylineData[] {
        new PolylineData(
            Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA,
            Canvas.TOOLBAR_FG_WIDTH, 0.05f,
            new Vector2f(0.5f, 0.90f),
            new Vector2f(0.5f, 0.95f)
        ),
        new PolylineData(
            Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA,
            Canvas.TOOLBAR_FG_WIDTH, 0.05f,
            new Vector2f(0.35f, 0.6f),
            new Vector2f(0.5f, 0.6f),
            new Vector2f(0.5f, 0.1f)
        ),
        new PolylineData(
            Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA,
            Canvas.TOOLBAR_FG_WIDTH, 0.05f,
            new Vector2f(0.2f, 0.1f),
            new Vector2f(0.8f, 0.1f)
        )
    };




    public MainMenuHud_InfoButton(final @NotNull HudContext _hud) {
        super((ServerLevel)_hud.getPlayer().level(), null, "Info", 1, new HudSquareButton_S());

        // Create design
        final Div e = addChild(new PolylineSetElm(world, design));
        e.setSize(new Vector2f(Canvas.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        //TODO
    }
}
