package com.snek.fancyplayershops.graphics.misc.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.graphics.hud.misc.styles.Hud_SquareButton_S;
import com.snek.frameworklib.graphics.core.Context;
import com.snek.frameworklib.graphics.designs.SymbolDesigns;
import com.snek.frameworklib.graphics.functional.elements.ButtonElm;
import com.snek.frameworklib.graphics.interfaces.Clickable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class Misc_BackButton extends ButtonElm {
    final Runnable onClickTask;




    public Misc_BackButton(final @NotNull Context context, final @NotNull Runnable onClickTask) {
        super(context.getLevel(), null, "Go back", 1, new Hud_SquareButton_S());
        this.onClickTask = onClickTask;

        // Create design
        addDesign(context.getLevel(), SymbolDesigns.CurvedArrowPointingLeft);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);
        Clickable.playSound(player);
        onClickTask.run();
    }
}

