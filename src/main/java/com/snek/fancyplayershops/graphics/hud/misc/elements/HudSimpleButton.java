package com.snek.fancyplayershops.graphics.hud.misc.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.graphics.hud.misc.styles.HudSimpleButton_S;
import com.snek.fancyplayershops.graphics.ui.misc.interfaces.InputIndicatorCanvas;
import com.snek.frameworklib.graphics.functional.elements.SimpleButtonElm;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;








/**
 * A generic button class with clicking and hovering capabilities and a configurable cooldown time.
 * <p> It also specifies action names for the input indicators to display.
 */
public class HudSimpleButton extends SimpleButtonElm {
    private   final @Nullable String lmbActionName;
    private   final @Nullable String rmbActionName;




    /**
     * Creates a new HudSimpleButton using a custom style.
     * @param world The world to spawn this button in.
     * @param _lmbActionName The name of the action associated with left clicks.
     * @param _rmbActionName The name of the action associated with right clicks.
     * @param clickCooldown The amount of ticks before the button becomes clickable again after being clicked.
     * @param _style The custom style.
     */
    protected HudSimpleButton(final @NotNull ServerLevel world, final @Nullable String _lmbActionName, final @Nullable String _rmbActionName, final int _clickCooldown, final HudSimpleButton_S _style) {
        super(world, _clickCooldown, _style);
        lmbActionName = _lmbActionName;
        rmbActionName = _rmbActionName;
    }


    /**
     * Creates a new HudSimpleButton using the default style.
     * @param world The world to spawn this button in.
     * @param _lmbActionName The name of the action associated with left clicks.
     * @param _rmbActionName The name of the action associated with right clicks.
     * @param clickCooldown The amount of ticks before the button becomes clickable again after being clicked.
     */
    protected HudSimpleButton(final @NotNull ServerLevel world, final @Nullable String _lmbActionName, final @Nullable String _rmbActionName, final int _clickCooldown) {
        this(world, _lmbActionName, _rmbActionName, _clickCooldown, new HudSimpleButton_S());
    }




    @Override
    public void onHoverTick(final @NotNull Player player) {
        super.onHoverTick(player);

        // Update input displays if present
        if(canvas != null && canvas instanceof InputIndicatorCanvas c) {
            c.getLmbIndicator().updateDisplay(lmbActionName);
            c.getRmbIndicator().updateDisplay(rmbActionName);
        }
    }




    @Override
    public void onHoverExit(final @Nullable Player player) {
        super.onHoverExit(player);

        // Update input displays if present
        if(canvas != null && canvas instanceof InputIndicatorCanvas c) {
            c.getLmbIndicator().updateDisplay(null);
            c.getRmbIndicator().updateDisplay(null);
        }
    }
}
