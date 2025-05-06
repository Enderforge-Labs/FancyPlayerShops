package com.snek.fancyplayershops.main;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.ui.InteractionBlocker;
import com.snek.framework.utils.scheduler.RateLimiter;

import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.InteractionEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;








/**
 * A utility class that handles clicks from players.
 */
public abstract class ClickManager {
    private static final @NotNull Map<@NotNull UUID, @NotNull RateLimiter> clickLimiters = new HashMap<>();
    private ClickManager() {}




    /**
     * Handles left and right clicks on shop blocks before the interaction blocker is spawned.
     * <p> Must be called on AttackBlockCallback and UseBlockCallback events.
     * @param world The world the player is in.
     * @param player The player.
     * @param hand The hand used.
     * @param clickType The type of click (LEFT click or RIGHT click).
     * @return FAIL if the player clicked a shop, PASS if not.
     */
    private static @NotNull ActionResult onClick(final @NotNull World world, final @NotNull PlayerEntity player, final @NotNull Hand hand, final @NotNull ClickType clickType) {

        // Handle limiter
        RateLimiter limiter = clickLimiters.get(player.getUuid());
        if(limiter == null) {
            limiter = new RateLimiter();
            clickLimiters.put(player.getUuid(), limiter);
        }


        // Forward clicks to the shop if the limiter allows it. Ignore offhand events
        if(hand == Hand.MAIN_HAND && world instanceof ServerWorld serverWorld) {
            final Shop targetShop = HoverManager.getLookedAtShop(player, serverWorld);
            if(targetShop != null) {
                if(limiter.attempt()) {
                    limiter.renewCooldown(1);
                    targetShop.onClick(player, clickType);
                }
                return ActionResult.FAIL;
            }
        }
        return ActionResult.PASS;
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
    public static @NotNull ActionResult onClickEntity(final @NotNull World world, final @NotNull PlayerEntity player, final @NotNull Hand hand, final @NotNull ClickType clickType, final @NotNull Entity entity) {
        if(entity instanceof InteractionEntity && entity.hasCustomName() && entity.getCustomName().getString().equals(InteractionBlocker.ENTITY_CUSTOM_NAME)) {
            return onClick(world, player, hand, clickType);
        }
        return ActionResult.PASS;
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
    public static @NotNull ActionResult onClickBlock(final @NotNull World world, final @NotNull PlayerEntity player, final @NotNull Hand hand, final @NotNull ClickType clickType, final @NotNull Vec3i pos) {

        // Check ray casting result
        final ActionResult r =  onClick(world, player, hand, clickType);


        // If the ray casting fails, check the block reported by the event.
        //! This is necessary due to the ray casting's low accuracy and slight delay.
        //! These would allow players to bypass the ray casting check by quickly clicking after changing view or by looking at the edge of the block.
        if(r == ActionResult.PASS) {
            return DataManager.findShop(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), world) == null ? ActionResult.PASS : ActionResult.FAIL;
        }
        else return ActionResult.FAIL;
    }




    /**
     * Handles right clicks on shop blocks before the interaction blocker is spawned.
     * <p> Must be called on useItemCallback events.
     * @param world The world the player is in.
     * @param player The player.
     * @param hand The hand used.
     * @return FAIL if the player clicked a shop, PASS if not.
     */
    public static @NotNull ActionResult onUseItem(final @NotNull World world, final @NotNull PlayerEntity player, final @NotNull Hand hand) {
        return onClick(world, player, hand, ClickType.RIGHT);
    }
}
