package com.snek.framework.ui.functional.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.ui.basic.elements.FancyTextElm;
import com.snek.framework.ui.functional.styles.FancyButtonElmStyle;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.scheduler.RateLimiter;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








/**
 * A generic button class with clicking and hovering capabilities and a configurable cooldown time.
 */
public abstract class FancyButtonElm extends FancyTextElm implements __base_ButtonElm {
    protected final RateLimiter clickRateLimiter       = new RateLimiter();
    protected final RateLimiter initialCooldownLimiter = new RateLimiter();
    private   final int clickCooldown;




    /**
     * Creates a new ButtonElm using a custom style.
     * @param _world The world in which to place the element.
     * @param clickCooldown The amount of ticks before the button becomes clickable again after being clicked.
     * @param _style The custom style.
     */
    protected FancyButtonElm(final @NotNull ServerLevel _world, final int _clickCooldown, final FancyButtonElmStyle _style) {
        super(_world, _style);
        clickCooldown = _clickCooldown;
    }


    /**
     * Creates a new ButtonElm using the default style.
     * @param _world The world in which to place the element.
     * @param clickCooldown The amount of ticks before the button becomes clickable again after being clicked.
     */
    protected FancyButtonElm(final @NotNull ServerLevel _world, final int _clickCooldown) {
        this(_world, _clickCooldown, new FancyButtonElmStyle());
    }




    @Override
    public void spawn(final @NotNull Vector3d pos) {
        initialCooldownLimiter.renewCooldown(SimpleButtonElm.INITIAL_COOLDOWN);
        super.spawn(pos);
        final Animation animation = getStyle(FancyButtonElmStyle.class).getHoverPrimerAnimation();
        if(animation != null) {
            applyAnimationNow(animation);
        }
    }




    @Override
    public void onHoverEnter(final @NotNull Player player) {
        final Animation animation = getStyle(FancyButtonElmStyle.class).getHoverEnterAnimation();
        if(animation != null) {
            applyAnimation(animation);
        }
    }




    @Override
    public void onHoverTick(final @NotNull Player player) {
        // Empty
    }




    @Override
    public void onHoverExit(final @Nullable Player player) {
        final Animation animation = getStyle(FancyButtonElmStyle.class).getHoverLeaveAnimation();
        if(animation != null) {
            applyAnimation(animation);
            hoverRateLimiter.renewCooldown(animation.getTotalDuration());
        }
    }




    @Override
    public boolean attemptClick(final @NotNull Player player, final @NotNull ClickAction click) {
        if(!initialCooldownLimiter.attempt()) return false;
        if(!clickRateLimiter.attempt()) return false;
        clickRateLimiter.renewCooldown(clickCooldown);
        return checkIntersection(player);
    }




    /**
     * Updates the displayed text.
     * @param textOverride If not null, it replaces the shop's data.
     */
    public abstract void updateDisplay(@Nullable Component textOverride);




    /**
     * Plays the button click sound to the specified player.
     * @param player The player to play the sound to.
     */
    public static void playButtonSound(final @NotNull Player player) {
        MinecraftUtils.playSoundClient(player, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, 2, 1.5f);
    }
}
