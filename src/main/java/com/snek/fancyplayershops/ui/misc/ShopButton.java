package com.snek.fancyplayershops.ui.misc;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.ui.elements.FancyTextElm;
import com.snek.framework.ui.interfaces.Clickable;
import com.snek.framework.ui.interfaces.Hoverable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3d;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.interfaces.InputIndicatorCanvas;
import com.snek.fancyplayershops.ui.misc.styles.ShopButton_S;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.scheduler.RateLimiter;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;








/**
 * A generic button class with clicking and hovering capabilities and a configurable cooldown time.
 * It also specifies action names for the input indicators to display.
 */
public abstract class ShopButton extends FancyTextElm implements Hoverable, Clickable {
    protected final @NotNull Shop shop;
    protected final RateLimiter clickRateLimiter = new RateLimiter();
    private   final int clickCooldown;

    private final @Nullable String lmbActionName;
    private final @Nullable String rmbActionName;




    /**
     * Creates a new ShopButton using a custom style.
     * @param _shop The target shop.
     * @param _lmbActionName The name of the action associated with left clicks.
     * @param _rmbActionName The name of the action associated with right clicks.
     * @param clickCooldown The amount of ticks before the button becomes clickable again after being clicked.
     * @param _style The custom style.
     */
    protected ShopButton(@NotNull Shop _shop, @Nullable String _lmbActionName, @Nullable String _rmbActionName, int _clickCooldown, ShopButton_S _style) {
        super(_shop.getWorld(), _style);
        shop = _shop;
        lmbActionName = _lmbActionName;
        rmbActionName = _rmbActionName;
        clickCooldown = _clickCooldown;
    }


    /**
     * Creates a new ShopButton using the default style.
     * @param _shop The target shop.
     * @param _lmbActionName The name of the action associated with left clicks.
     * @param _rmbActionName The name of the action associated with right clicks.
     * @param clickCooldown The amount of ticks before the button becomes clickable again after being clicked.
     */
    protected ShopButton(@NotNull Shop _shop, @Nullable String _lmbActionName, @Nullable String _rmbActionName, int _clickCooldown) {
        this(_shop, _lmbActionName, _rmbActionName, _clickCooldown, new ShopButton_S());
    }




    @Override
    public void spawn(Vector3d pos) {
        final Animation animation = getStyle(ShopButton_S.class).getHoverPrimerAnimation();
        if(animation != null) {
            applyAnimationNow(animation);
        }
        super.spawn(pos);
    }


    @Override
    public void onHoverEnter(@NotNull PlayerEntity player) {
        if(player != shop.user) return;
        final Animation animation = getStyle(ShopButton_S.class).getHoverEnterAnimation();
        if(animation != null) {
            applyAnimation(animation);
        }

        // Update input displays if present
        if(shop.getActiveCanvas() != null) {
            if(shop.getActiveCanvas() instanceof InputIndicatorCanvas c) {
                c.getLmbIndicator().updateDisplay(lmbActionName);
                c.getRmbIndicator().updateDisplay(rmbActionName);
            }
        }
    }


    @Override
    public void onHoverExit(@Nullable PlayerEntity player) {
        if(player != shop.user) return;
        final Animation animation = getStyle(ShopButton_S.class).getHoverLeaveAnimation();
        if(animation != null) {
            applyAnimation(animation);
        }
    }


    @Override
    public boolean onClick(@NotNull PlayerEntity player, @NotNull ClickType click) {
        if(clickRateLimiter.attempt()) {
            boolean r = checkIntersection(player);
            if(r) clickRateLimiter.renewCooldown(clickCooldown);
            return r;
        }
        else {
            return false;
        }
    }


    /**
     * Updates the displayed text, reading data from the target shop.
     * @param textOverride If not null, it replaces the shop's data.
     */
    public abstract void updateDisplay(@Nullable Text textOverride);


    /**
     * Plays the button click sound to the specified player.
     * @param player The player to play the sound to.
     */
    protected void playButtonSound(@NotNull PlayerEntity player) {
        MinecraftUtils.playSoundClient(player, SoundEvents.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, 1, 1.5f);
    }
}
