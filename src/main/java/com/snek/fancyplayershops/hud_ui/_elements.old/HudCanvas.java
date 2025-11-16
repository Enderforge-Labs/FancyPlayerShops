package com.snek.fancyplayershops.hud_ui._elements.old;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3f;

import com.snek.fancyplayershops.hud_ui._styles.HudCanvasBack_S;
import com.snek.fancyplayershops.hud_ui._styles.HudCanvasBackground_S;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui._elements.UiCanvas;
import com.snek.framework.old.data_types.animations.Transform;
import com.snek.framework.old.data_types.animations.Transition;
import com.snek.framework.old.ui.Div;
import com.snek.framework.old.ui.Elm;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;








public class HudCanvas extends UiCanvas implements __HudElm {

    // UI
    public static final float POS_UPDATE_DISTANCE = 0.1f;
    public static final float HUD_DISTANCE = 1.3f;

    // Canvas data
    protected final @NotNull Hud hud;
    private @NotNull Vector3f lastPos = new Vector3f();
    private int lastRotation = 0;
    private boolean spawned = false;




    public HudCanvas(final @NotNull Hud _hud, final float height, final float heightTop, final float heightBottom) {
        super(_hud.getActiveCanvas(), (ServerLevel)(_hud.getPlayer().level()), height, heightTop, heightBottom, new HudCanvasBackground_S(), new HudCanvasBack_S());
        hud = _hud;
    }


    public void update() {
        final Player player = hud.getPlayer();

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




    @Override
    public void spawn(Vector3d pos) {
        if(!spawned) {
            spawned = true;
            super.spawn(pos);
            lastPos = new Vector3f((float)pos.x, (float)pos.y, (float)pos.z);

            // Move displays away from the player's center
            applyAnimationNowRecursive(new Transition().additiveTransform(new Transform().move(__calcVisualShift())));
        }
    }
    public @NotNull Vector3f __calcVisualShift() {
        final float rotation = (float)Math.toRadians((lastRotation + 4) % 8 * -45f);
        final Vector3f direction = new Vector3f((float)Math.sin(rotation), 0, (float)Math.cos(rotation));
        final Vector3f shift = direction.mul(HUD_DISTANCE).sub(0, 0.5f, 0);
        return shift;
    }




    @Override
    public void despawn() {
        if(spawned) {
            spawned = false;
            super.despawn();
        }
    }
}
