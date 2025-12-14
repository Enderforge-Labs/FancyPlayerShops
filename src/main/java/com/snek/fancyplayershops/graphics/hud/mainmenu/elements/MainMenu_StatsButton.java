package com.snek.fancyplayershops.graphics.hud.mainmenu.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.graphics.hud.misc.styles.Hud_SquareButton_S;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.data_types.graphics.PolylineData;
import com.snek.frameworklib.graphics.composite.elements.PolylineSetElm;
import com.snek.frameworklib.graphics.core.Canvas;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.functional.elements.SimpleButtonElm;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.GeometryUtils;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class MainMenu_StatsButton extends SimpleButtonElm {


    private static final @NotNull PolylineData[] design = {
        //TODO this design is used like twice, make it a public catalogue in FrameworkLib
        new PolylineData(
            Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA, //FIXME this is used literally everywhere. it should be a standard method or something
            Canvas.TOOLBAR_FG_WIDTH, 0.04f, //FIXME this is used literally everywhere. it should be a standard method or something
            new Vector2f(0.0f, 0.0f),
            new Vector2f(0.3f, 0.4f),
            new Vector2f(0.7f, 0.4f),
            new Vector2f(1.0f, 0.9f).sub(0.02f, 0.05f)
        ),
        new PolylineData(
            Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA,
            Canvas.TOOLBAR_FG_WIDTH, 0.06f,
            GeometryUtils.rotateVec2(new Vector2f(-0.25f, -0.00f), (float)Math.toRadians(15)).add(1, 0.9f),
            GeometryUtils.rotateVec2(new Vector2f(+0.00f, -0.00f), (float)Math.toRadians(15)).add(1, 0.9f),
            GeometryUtils.rotateVec2(new Vector2f(+0.00f, -0.25f), (float)Math.toRadians(15)).add(1, 0.9f)
        )
    };




    public MainMenu_StatsButton(final @NotNull HudContext _hud) {
        super(_hud.getLevel(), null, "Statistics", 1, new Hud_SquareButton_S());

        // Create design
        final Div e = addChild(new PolylineSetElm(level, design));
        e.setSize(new Vector2f(Canvas.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        super.onClick(player, click);
        player.displayClientMessage(new Txt("Shop stats coming soon!").get(), false);
    }
}
