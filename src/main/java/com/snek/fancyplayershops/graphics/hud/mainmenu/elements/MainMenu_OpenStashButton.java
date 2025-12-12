package com.snek.fancyplayershops.graphics.hud.mainmenu.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.graphics.hud.misc.elements.Hud_SimpleButton;
import com.snek.fancyplayershops.graphics.hud.misc.styles.Hud_SquareButton_S;
import com.snek.fancyplayershops.graphics.hud.stash.StashCanvas;
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








public class MainMenu_OpenStashButton extends Hud_SimpleButton {


    private static final @NotNull PolylineData[] design = new PolylineData[] {
        new PolylineData(
            Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA,
            Canvas.TOOLBAR_FG_WIDTH, 0.05f,
            new Vector2f(0.1f, 0.7f),
            new Vector2f(0.1f, 0.0f),
            new Vector2f(0.9f, 0.0f),
            new Vector2f(0.9f, 0.7f)
        ),
        new PolylineData(
            Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA,
            Canvas.TOOLBAR_FG_WIDTH, 0.05f,
            new Vector2f(0.0f, 1.0f),
            new Vector2f(0.0f, 0.7f),
            new Vector2f(1.0f, 0.7f),
            new Vector2f(1.0f, 1.0f),
            new Vector2f(0.0f, 1.0f)
        ),
        new PolylineData(
            Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA,
            Canvas.TOOLBAR_FG_WIDTH, 0.05f,
            new Vector2f(0.4f, 0.4f),
            new Vector2f(0.6f, 0.4f)
        )
    };




    public MainMenu_OpenStashButton(final @NotNull HudContext _hud) {
        super((ServerLevel)_hud.getPlayer().level(), null, "Open stash", 1, new Hud_SquareButton_S());

        // Create design
        final Div e = addChild(new PolylineSetElm(world, design));
        e.setSize(new Vector2f(Canvas.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        final @NotNull HudContext context = (HudContext)canvas.getContext();
        context.changeCanvas(new StashCanvas(context));
    }
}
