package com.snek.fancyplayershops.graphics.misc.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.graphics.hud.misc.styles.Hud_SquareButton_S;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.graphics.composite.elements.PolylineSetElm;
import com.snek.frameworklib.graphics.core.Context;
import com.snek.frameworklib.graphics.designs.SymbolDesigns;
import com.snek.frameworklib.graphics.functional.elements.SimpleButtonElm;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.graphics.layout.Div;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class Misc_BackButton extends SimpleButtonElm {
    final Runnable onClickTask;




    public Misc_BackButton(final @NotNull Context context, final @NotNull Runnable onClickTask) {
        super(context.getLevel(), null, "Go back", 1, new Hud_SquareButton_S());
        this.onClickTask = onClickTask;

        // Create design
        final Div e = addChild(new PolylineSetElm(context.getLevel(), SymbolDesigns.CurvedArrowPointingLeft));
        e.setSize(new Vector2f(FancyPlayerShops.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);
        Clickable.playSound(player);
        onClickTask.run();
    }
}

