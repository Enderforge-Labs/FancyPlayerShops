package com.snek.fancyplayershops.graphics.ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.graphics.ui.core.elements.ProductItemDisplayElm;
import com.snek.fancyplayershops.graphics.ui.edit.styles.Edit_RotateButtonLeft_S;
import com.snek.fancyplayershops.graphics.ui.edit.styles.Edit_RotateButtonRight_S;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.designs.SymbolDesigns;
import com.snek.frameworklib.graphics.functional.elements.ButtonElm;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








/**
 * A button that allows the owner of the product display to change the default rotation of the displayed object.
 */
public class Edit_RotateButton extends ButtonElm {
    public static final int ROTATION_ANIMATION_TIME = 8;
    private final int rotation;




    /**
     * Creates a new RotateButton.
     * @param display The target product display.
     * @param _rotateAngle The angle to add to the default rotation each time this button is pressed, in eighths.
     */
    public Edit_RotateButton(final @NotNull ProductDisplay display, final int _rotation) {
        super(
            display.getLevel(),
            "Rotate once", "Rotate quickly", 2,
            _rotation > 0 ? new Edit_RotateButtonRight_S(display) : new Edit_RotateButtonLeft_S(display)
        );
        rotation = _rotation;


        // Create design
        addDesign(display.getLevel(), _rotation > 0 ? SymbolDesigns.ArrowHeadPointingLeft : SymbolDesigns.ArrowHeadPointingRight);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);
        final ProductDisplay display = GetDisplay.get(this);
        display.addDefaultRotation(rotation);


        // Animate the item display to show the new rotation
        final float radians = rotation * (float)Math.PI / 4;
        display.getItemDisplay().applyAnimation(
            new Transition(2, Easings.expOut)
            .additiveTransform(new Transform().rotY(-radians)),
            false, true
        );
        display.getItemDisplay().applyAnimation(
            new Transition(ROTATION_ANIMATION_TIME, Easings.expOut)
            .additiveTransform(new Transform().rotY(+radians)),
            false, true
        );


        // Play sound and send feedback message
        Clickable.playSound(player);
        player.displayClientMessage(new Txt()
            .cat(new Txt("Item direction set to: ").lightGray())
            .cat(new Txt(display.getDefaultDirection().getName()).white())
        .get(), true);
    }




    @Override
    public void onHoverEnter(final @NotNull Player player) {
        super.onHoverEnter(player);

        // Handle item display animations
        final ProductDisplay display = GetDisplay.get(this);
        final ProductItemDisplayElm itemDisplay = display.getItemDisplay();
        itemDisplay.stopLoopAnimation();
        itemDisplay.applyAnimation(
            new Transition(ROTATION_ANIMATION_TIME, Easings.expOut)
            .targetTransform(
                itemDisplay.genLastTransform()
                .setRot(new Quaternionf())
                .rotY(canvas.getContext().getRotation() / 8f * (float)(2f * Math.PI))
            ),
            false, true
        );
    }




    @Override
    public void onHoverExit(final @Nullable Player player) {
        super.onHoverExit(player);

        // Handle item display animations
        final ProductDisplay display = GetDisplay.get(this);
        display.getItemDisplay().startLoopAnimation();
    }
}
