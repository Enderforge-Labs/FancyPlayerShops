package com.snek.fancyplayershops.ui.edit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.ShopItemDisplay;
import com.snek.fancyplayershops.ui.edit.styles.EditUi_RotateButtonLeft_S;
import com.snek.fancyplayershops.ui.edit.styles.EditUi_RotateButtonRight_S;
import com.snek.fancyplayershops.ui.misc.ShopButton;
import com.snek.fancyplayershops.ui.misc.styles.ShopButton_S;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.utils.Easings;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;








/**
 * A button that allows the owner of the shop to change the default rotation of the displayed object.
 */
public class EditUi_RotateButton extends ShopButton {
    public static final int ROTATION_ANIMATION_TIME = 8;

    private final float rotation;
    private final @NotNull Text buttonText;


    /**
     * Creates a new RotateButton.
     * @param _shop The target shop.
     * @param _rotateAngle The angle to add to the default rotation each time this button is pressed.
     * @param _buttonText The text to display on the button.
     */
    public EditUi_RotateButton(final @NotNull Shop _shop, final float _rotation, final @NotNull Text _buttonText) {
        super(
            _shop,
            "Rotate once",
            "Rotate quickly",
            2,
            _rotation > 0 ? new EditUi_RotateButtonRight_S() : new EditUi_RotateButtonLeft_S()
        );
        rotation = _rotation;
        buttonText = _buttonText;
        updateDisplay(null);

        // Adjust arrow size
        applyAnimationNow(
            new Transition()
            .additiveTransformFg(new Transform().scale(EditUi.SQUARE_BUTTON_SIZE * 10))
        );
    }


    @Override
    public void updateDisplay(final @Nullable Text textOverride) {
        getStyle(ShopButton_S.class).setText(textOverride != null ? textOverride : buttonText);
        flushStyle();
    }


    @Override
    public boolean onClick(final @NotNull PlayerEntity player, final @NotNull ClickType click) {
        final boolean r = super.onClick(player, click);
        if(r) {
            shop.addDefaultRotation(rotation);

            // Animate the item display to show the new rotation
            shop.getItemDisplay().applyAnimation(
                new Transition(2, Easings.expOut)
                .additiveTransform(new Transform().rotY(-rotation))
            );
            shop.getItemDisplay().applyAnimation(
                new Transition(ROTATION_ANIMATION_TIME, Easings.expOut)
                .additiveTransform(new Transform().rotY(rotation))
            );

            playButtonSound(player);
        }
        return r;
    }


    @Override
    public void onHoverEnter(final @NotNull PlayerEntity player) {
        super.onHoverEnter(player);

        // Handle item display animations
        final ShopItemDisplay itemDisplay = shop.getItemDisplay();
        itemDisplay.stopLoopAnimation();
        itemDisplay.applyAnimation(
            new Transition(ROTATION_ANIMATION_TIME, Easings.expOut)
            .targetTransform(
                itemDisplay.genLastTransform()
                .setRot(new Quaternionf())
                .rotY(shop.getCanvasDirection() / 8f * (float)(2f * Math.PI))
            )
        );
    }


    @Override
    public void onHoverExit(final @Nullable PlayerEntity player) {
        super.onHoverExit(player);

        // Handle item display animations
        shop.getItemDisplay().startLoopAnimation();
    }
}
