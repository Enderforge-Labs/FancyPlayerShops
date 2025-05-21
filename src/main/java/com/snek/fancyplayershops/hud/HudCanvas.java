package com.snek.fancyplayershops.hud;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3f;

import com.snek.fancyplayershops.hud.misc.styles.HudCanvasBack_S;
import com.snek.fancyplayershops.hud.misc.styles.HudCanvasBackground_S;
import com.snek.fancyplayershops.main.Shop;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.elements.Elm;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class HudCanvas extends Canvas {

    // UI
    public static final float POS_UPDATE_DISTANCE = 0.1f;
    public static final float HUD_DISTANCE = 1.3f;

    // Canvas data
    protected final @NotNull Player player;
    private @NotNull Vector3f lastPos = new Vector3f();
    private int lastRotation = 0;

    // Active canvas list
    private static final Map<UUID, HudCanvas> activeCanvases = new HashMap<>();




    public HudCanvas(final @NotNull Player _player, final @Nullable Canvas prevCanvas, final float height, final float heightTop, final float heightBottom) {
        super(prevCanvas, (ServerLevel)_player.level(), height, heightTop, heightBottom, new HudCanvasBackground_S(), new HudCanvasBack_S());
        player = _player;
    }


    public static void updateActiveCanvases(){
        for (HudCanvas c : activeCanvases.values()) {
            c.update();
        }
    }
    public void update() {
        forwardHover(player);

        // Update rotation
        final int newRot = Math.round((player.getViewYRot(1) + 180f) / 45f) % 8;
        updateRot(newRot);


        // Calculate new position and position difference
        final Vector3f newPos = player.getEyePosition().toVector3f();
        final Vector3f posDelta = newPos.sub(lastPos, new Vector3f());

        // If the player moved too far since the last update, teleport the entities
        if(posDelta.length() >= POS_UPDATE_DISTANCE) {
            lastPos = newPos;
            updatePos(this);
        }
    }


    public void updateRot(final int newRot) {
        if(lastRotation != newRot) {
            applyAnimationRecursive(Shop.calcCanvasRotationAnimation(lastRotation, newRot));
            lastRotation = newRot;
        }
    }


    public void updatePos(final @NotNull Div div) {
        if(div instanceof Elm e) {
            e.getEntity().teleport(new Vector3d(lastPos.x, lastPos.y, lastPos.z));
        }
        for(Div c : div.getChildren()) {
            updatePos(c);
        }
    }


    public static void forwardClickStatic(final @NotNull Player _player, final @NotNull ClickAction action) {
        final HudCanvas c = activeCanvases.get(_player.getUUID());
        if(c != null) c.forwardClick(_player, action);
    }



    @Override
    public void spawn(Vector3d pos) {
        super.spawn(pos);
        lastPos = new Vector3f((float)pos.x, (float)pos.y, (float)pos.z);
        activeCanvases.put(player.getUUID(), this);

        // Move displays away from the player's center
        final float rotation = (float)Math.toRadians((lastRotation + 4) % 8 * -45f);
        final Vector3f direction = new Vector3f((float)Math.sin(rotation), 0, (float)Math.cos(rotation));
        final Vector3f shift = direction.mul(HUD_DISTANCE).sub(0, 0.5f, 0);
        applyAnimationNowRecursive(new Transition().additiveTransform(new Transform().move(shift)));
    }



    @Override
    public void despawnNow() {
        super.despawnNow();
        activeCanvases.remove(player.getUUID());
    }
}
