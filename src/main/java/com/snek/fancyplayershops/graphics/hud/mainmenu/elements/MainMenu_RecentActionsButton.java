package com.snek.fancyplayershops.graphics.hud.mainmenu.elements;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.graphics.hud.misc.elements.Hud_SimpleButton;
import com.snek.fancyplayershops.graphics.hud.misc.styles.Hud_SquareButton_S;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.data_types.graphics.PolylineData;
import com.snek.frameworklib.graphics.composite.elements.PolylineSetElm;
import com.snek.frameworklib.graphics.core.Canvas;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.GeometryUtils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.ClickAction;








public class MainMenu_RecentActionsButton extends Hud_SimpleButton {
    private static final @NotNull List<@NotNull Vector2f> design0 = new ArrayList<>();
    static {
        for(int i = 0; i < 8; ++i) {
            design0.add(
                GeometryUtils.rotateVec2(
                    new Vector2f(0, 0.4f),
                    (float)Math.toRadians(45) * (i + 0.5f)
                )
                .add(0.5f, 0.4f)
            );
        }
    }
    private static final @NotNull PolylineData[] design = new PolylineData[] {
        new PolylineData(
            Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA,
            Canvas.TOOLBAR_FG_WIDTH, 0.05f,
            new Vector2f(0.5f, 1.0f- 0.025f),
            new Vector2f(0.3f, 0.8f- 0.025f),
            new Vector2f(0.5f, 0.6f- 0.025f)
        ),
        new PolylineData(
            Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA,
            Canvas.TOOLBAR_FG_WIDTH, 0.03f,
            design0.get(2),
            design0.get(3),
            design0.get(4),
            design0.get(5),
            design0.get(6),
            design0.get(7),
            design0.get(0)
        )
    };



    public MainMenu_RecentActionsButton(final @NotNull HudContext _hud) {
        super((ServerLevel)_hud.getPlayer().level(), null, "Recent actions", 1, new Hud_SquareButton_S());

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
