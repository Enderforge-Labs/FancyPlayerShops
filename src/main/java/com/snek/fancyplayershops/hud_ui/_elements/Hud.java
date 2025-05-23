package com.snek.fancyplayershops.hud_ui._elements;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.fancyplayershops.shop_ui._elements.ShopCanvas;
import com.snek.fancyplayershops.ui._elements.InteractionBlocker;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.functional.ButtonElm;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.phys.Vec3;








public class Hud {
    public static final float INTERACTION_BLOCKER_SIZE = 0.5f;

    // Hud data
    private final Player player;
    private @Nullable HudCanvas activeCanvas = null;
    private @Nullable InteractionBlocker interactionBlocker = null;
    private boolean spawned = false;

    // Getters
    public @NotNull  Player    getPlayer      () { return player; }
    public @Nullable HudCanvas getActiveCanvas() { return activeCanvas; }

    // Active canvas list
    private static final Map<UUID, Hud> activeHuds = new HashMap<>();








    public Hud(final @NotNull Player _player) {
        player = _player;
        activeHuds.put(player.getUUID(), this);
    }


    public void spawn(Vector3d pos) {
        if(!spawned) {
            spawned = true;
            if(activeCanvas != null) activeCanvas.spawn(pos);
            interactionBlocker = new InteractionBlocker(player.level(), INTERACTION_BLOCKER_SIZE, INTERACTION_BLOCKER_SIZE);
            interactionBlocker.spawn(pos.sub(new Vector3d(0, INTERACTION_BLOCKER_SIZE / -2f, 0), new Vector3d()));
        }
    }

    public void despawn() {
        if(spawned) {
            spawned = false;
            activeHuds.remove(player.getUUID());
            if(activeCanvas != null) activeCanvas.despawn();
            interactionBlocker.despawn();
            interactionBlocker = null;
        }
    }

    public void update() {
        if(activeCanvas != null) activeCanvas.update();
        if(interactionBlocker != null) {
            final Vec3 pos = player.getEyePosition();
            interactionBlocker.teleport(new Vector3d(pos.x, pos.y - INTERACTION_BLOCKER_SIZE / 2f, pos.z));
        }
    }

    public void changeCanvas(final @NotNull HudCanvas canvas) {
        activeCanvas = canvas;

        //TODO
        // // Adjust rotation if needed
        // if(lastDirection != 0) {
        //     final Animation animation = calcCanvasRotationAnimation(0, lastDirection);
        //     for(final Div c : canvas.getBg().getChildren()) {
        //         c.applyAnimationNowRecursive(animation);
        //     }
        // }

        // Spawn canvas into the world and play a sound to the user
        final Vec3 pos = player.getPosition(1);
        canvas.spawn(new Vector3d(pos.x, pos.y, pos.z));
    }








    public static void closeHud(final @NotNull Player player) {
        final Hud hud = activeHuds.get(player.getUUID());
        if(hud != null) hud.despawn();
    }


    public static Hud getOpenHud(final @NotNull Player player) {
        return activeHuds.get(player.getUUID());
    }


    public static Hud getOpenHudOrCreate(final @NotNull Player player) {
        final Hud hud = getOpenHud(player);
        if(hud == null) return new Hud(player);
        else return hud;
    }


    public static void updateActiveHuds() {
        for(Hud h : activeHuds.values()) {
            h.update();
        }
    }


    /**
     * Forwards a click event to the HUD of a player.
     * @param _player The player.
     * @param action The type of click.
     * @return True if the player has an open HUD, false otherwise.
     */
    public static boolean forwardClickStatic(final @NotNull Player _player, final @NotNull ClickAction action) {
        final Hud hud = activeHuds.get(_player.getUUID());
        if(hud == null) return false;
        final HudCanvas canvas = hud.activeCanvas;
        if(canvas == null) return false;

        canvas.forwardClick(_player, action);
        return true;
    }
}
