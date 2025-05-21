package com.snek.fancyplayershops.input;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.hud_ui.HudCanvas;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.shop_ui.elements.InteractionBlocker;
import com.snek.framework.utils.scheduler.RateLimiter;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.level.Level;








/**
 * A utility class that handles clicks from players.
 */
public abstract class ClickReceiver {
    private static final @NotNull Map<@NotNull UUID, @Nullable RateLimiter> clickLimiters = new HashMap<>();
    private ClickReceiver() {}




    /**
     * Handles left and right clicks on shop blocks before the interaction blocker is spawned.
     * <p> Must be called on AttackBlockCallback and UseBlockCallback events.
     * @param world The world the player is in.
     * @param player The player.
     * @param hand The hand used.
     * @param clickType The type of click (LEFT click or RIGHT click).
     * @return FAIL if the player clicked a shop, PASS if not.
     */
    private static @NotNull InteractionResult onClick(final @NotNull Level world, final @NotNull Player player, final @NotNull InteractionHand hand, final @NotNull ClickAction clickType) {

        // Skip player if they are dead or in spectator mode
        if(player.isSpectator() || player.isDeadOrDying()) return InteractionResult.PASS;


        // Handle limiter
        RateLimiter limiter = clickLimiters.get(player.getUUID());
        if(limiter == null) {
            limiter = new RateLimiter();
            clickLimiters.put(player.getUUID(), limiter);
        }


        // Send click to HUD and return if one is present
        if(HudCanvas.forwardClickStatic(player, clickType)) {
            return InteractionResult.PASS;
        }


        // Forward clicks to the shop if the limiter allows it. Ignore offhand events
        if(hand == InteractionHand.MAIN_HAND && world instanceof ServerLevel serverWorld) {
            final Shop targetShop = HoverReceiver.getLookedAtShop(player, serverWorld);
            if(targetShop != null) {
                if(limiter.attempt()) {
                    limiter.renewCooldown(1);
                    targetShop.onClick(player, clickType);
                }
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.PASS;
    }




    /**
     * Handles left and right clicks on shop interactions.
     * <p> Must be called on AttackEntityCallback and UseEntityCallback events.
     * @param world The world the player is in.
     * @param player The player.
     * @param hand The hand used.
     * @param clickType The type of click (LEFT click or RIGHT click).
     * @param entity The entity.
     * @return FAIL if the player clicked a shop, PASS if not.
     */
    public static @NotNull InteractionResult onClickEntity(final @NotNull Level world, final @NotNull Player player, final @NotNull InteractionHand hand, final @NotNull ClickAction clickType, final @NotNull Entity entity) {
        if(entity instanceof Interaction && entity.hasCustomName() && entity.getCustomName().getString().equals(InteractionBlocker.ENTITY_CUSTOM_NAME)) {
            return onClick(world, player, hand, clickType);
        }
        return InteractionResult.PASS;
    }




    /**
     * Handles left and right clicks on shop blocks before the interaction blocker is spawned.
     * <p> Must be called on AttackBlockCallback and UseBlockCallback events.
     * @param world The world the player is in.
     * @param player The player.
     * @param hand The hand used.
     * @param clickType The type of click (LEFT click or RIGHT click).
     * @param pos The position of the clicked block.
     * @return FAIL if the player clicked a shop, PASS if not.
     */
    public static @NotNull InteractionResult onClickBlock(final @NotNull Level world, final @NotNull Player player, final @NotNull InteractionHand hand, final @NotNull ClickAction clickType, final @NotNull Vec3i pos) {

        // Check ray casting result
        final InteractionResult r =  onClick(world, player, hand, clickType);


        // If the ray casting fails, check the block reported by the event.
        //! This is necessary due to the ray casting's low accuracy and slight delay.
        //! These would allow players to bypass the ray casting check by quickly clicking after changing view or by looking at the edge of the block.
        if(r == InteractionResult.PASS) {
            return ShopManager.findShop(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), world) == null ? InteractionResult.PASS : InteractionResult.FAIL;
        }
        else return InteractionResult.FAIL;
    }




    /**
     * Handles right clicks on shop blocks before the interaction blocker is spawned.
     * <p> Must be called on useItemCallback events.
     * @param world The world the player is in.
     * @param player The player.
     * @param hand The hand used.
     * @return FAIL if the player clicked a shop, PASS if not.
     */
    public static @NotNull InteractionResult onUseItem(final @NotNull Level world, final @NotNull Player player, final @NotNull InteractionHand hand) {
        return onClick(world, player, hand, ClickAction.SECONDARY);
    }
}
