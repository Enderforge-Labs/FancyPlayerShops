package com.snek.framework.ui.functional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.ui.elements.FancyTextElm;
import com.snek.framework.ui.functional.styles.ButtonElmStyle;
import com.snek.framework.ui.interfaces.Clickable;
import com.snek.framework.ui.interfaces.Hoverable;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.scheduler.RateLimiter;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;








/**
 * A generic button class with clicking and hovering capabilities and a configurable cooldown time.
 */
public abstract class ButtonElm extends FancyTextElm implements Hoverable, Clickable {
    protected final RateLimiter clickRateLimiter = new RateLimiter();
    private   final int clickCooldown;




    /**
     * Creates a new ButtonElm using a custom style.
     * @param _world The world in which to place the element.
     * @param clickCooldown The amount of ticks before the button becomes clickable again after being clicked.
     * @param _style The custom style.
     */
    protected ButtonElm(final @NotNull ServerWorld _world, final int _clickCooldown, final ButtonElmStyle _style) {
        super(_world, _style);
        clickCooldown = _clickCooldown;
    }


    /**
     * Creates a new ButtonElm using the default style.
     * @param _world The world in which to place the element.
     * @param clickCooldown The amount of ticks before the button becomes clickable again after being clicked.
     */
    protected ButtonElm(final @NotNull ServerWorld _world, final int _clickCooldown) {
        this(_world, _clickCooldown, new ButtonElmStyle());
    }




    @Override
    public void spawn(final @NotNull Vector3d pos) {
        super.spawn(pos);
        final Animation animation = getStyle(ButtonElmStyle.class).getHoverPrimerAnimation();
        if(animation != null) {
            applyAnimationNow(animation);
        }
    }




    @Override
    public void onHoverEnter(final @NotNull PlayerEntity player) {
        final Animation animation = getStyle(ButtonElmStyle.class).getHoverEnterAnimation();
        if(animation != null) {
            applyAnimation(animation);
        }
    }




    @Override
    public void onCheckTick(final @NotNull PlayerEntity player) {
        // Empty
    }




    @Override
    public void onHoverExit(final @Nullable PlayerEntity player) {
        final Animation animation = getStyle(ButtonElmStyle.class).getHoverLeaveAnimation();
        if(animation != null) {
            applyAnimation(animation);
        }
    }




    @Override
    public boolean onClick(final @NotNull PlayerEntity player, final @NotNull ClickType click) {
        if(clickRateLimiter.attempt()) {
            final boolean r = checkIntersection(player);
            if(r) clickRateLimiter.renewCooldown(clickCooldown);
            return r;
        }
        else {
            return false;
        }
    }



    /**
     * Plays the button click sound to the specified player.
     * @param player The player to play the sound to.
     */
    public static void playButtonSound(final @NotNull PlayerEntity player) {
        MinecraftUtils.playSoundClient(player, SoundEvents.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, 2, 1.5f);
    }




    /**
     * Updates the displayed text.
     * @param textOverride If not null, it replaces the shop's data.
     */
    public abstract void updateDisplay(@Nullable Text textOverride);
}
