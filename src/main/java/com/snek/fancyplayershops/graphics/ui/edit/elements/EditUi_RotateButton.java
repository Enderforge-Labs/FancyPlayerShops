package com.snek.fancyplayershops.graphics.ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.core.elements.ShopItemDisplay;
import com.snek.fancyplayershops.graphics.ui.edit.styles.EditUi_RotateButtonLeft_S;
import com.snek.fancyplayershops.graphics.ui.edit.styles.EditUi_RotateButtonRight_S;
import com.snek.fancyplayershops.graphics.ui.misc.elements.SimpleShopButton;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.ui.AlignmentX;
import com.snek.frameworklib.data_types.ui.AlignmentY;
import com.snek.frameworklib.data_types.ui.PolylineData;
import com.snek.frameworklib.graphics.Canvas;
import com.snek.frameworklib.graphics.Div;
import com.snek.frameworklib.graphics.composite.elements.PolylineSetElm;
import com.snek.frameworklib.utils.Easings;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








/**
 * A button that allows the owner of the shop to change the default rotation of the displayed object.
 */
public class EditUi_RotateButton extends SimpleShopButton {
    private static final @NotNull PolylineData[][] designs = new PolylineData[][] {
        new PolylineData[] {
            new PolylineData(
                Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA,
                Canvas.TOOLBAR_FG_WIDTH, 0.06f,
                new Vector2f(-0.15f * -1 + 0.5f, 0.2f),
                new Vector2f(+0.15f * -1 + 0.5f, 0.5f),
                new Vector2f(-0.15f * -1 + 0.5f, 0.8f)
            )
        },
        new PolylineData[] {
            new PolylineData(
                Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA,
                Canvas.TOOLBAR_FG_WIDTH, 0.06f,
                new Vector2f(-0.15f * 1 + 0.5f, 0.8f),
                new Vector2f(+0.15f * 1 + 0.5f, 0.5f),
                new Vector2f(-0.15f * 1 + 0.5f, 0.2f)
            )
        }
    };
    public static final int ROTATION_ANIMATION_TIME = 8;
    private final float rotation;




    /**
     * Creates a new RotateButton.
     * @param _shop The target shop.
     * @param _rotateAngle The angle to add to the default rotation each time this button is pressed.
     */
    public EditUi_RotateButton(final @NotNull Shop _shop, final float _rotation) {
        super(
            _shop,
            "Rotate once",
            "Rotate quickly",
            2,
            _rotation > 0 ? new EditUi_RotateButtonRight_S(_shop) : new EditUi_RotateButtonLeft_S(_shop)
        );
        rotation = _rotation;


        // Create design
        final Div e = addChild(new PolylineSetElm(_shop.getWorld(), designs[_rotation > 0 ? 1 : 0]));
        e.setSize(new Vector2f(Canvas.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
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




    @Override
    public void onHoverEnter(final @NotNull Player player) {
        super.onHoverEnter(player);

        // Handle item display animations
        final ShopItemDisplay itemDisplay = shop.getItemDisplay();
        itemDisplay.stopLoopAnimation();
        itemDisplay.applyAnimation(
            new Transition(ROTATION_ANIMATION_TIME, Easings.expOut)
            .targetTransform(
                itemDisplay.genLastTransform()
                .setRot(new Quaternionf())
                .rotY(shop.getActiveCanvas().getRotation() / 8f * (float)(2f * Math.PI)) // Rotation is in eighths (0-7), convert to radians
            )
        );
    }




    @Override
    public void onHoverExit(final @Nullable Player player) {
        super.onHoverExit(player);

        // Handle item display animations
        shop.getItemDisplay().startLoopAnimation();
    }
}
