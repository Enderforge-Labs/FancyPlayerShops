package com.snek.fancyplayershops.ui.edit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.ShopItemDisplay;
import com.snek.fancyplayershops.ui.edit.styles.EditUi_RotateButtonLeft_S;
import com.snek.fancyplayershops.ui.edit.styles.EditUi_RotateButtonRight_S;
import com.snek.fancyplayershops.ui.misc.ShopButton;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.composite.PolylineData;
import com.snek.framework.ui.composite.PolylineSetElm;
import com.snek.framework.utils.Easings;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








/**
 * A button that allows the owner of the shop to change the default rotation of the displayed object.
 */
public class EditUi_RotateButton extends ShopButton {
    private static final @NotNull PolylineData[][] designs = new PolylineData[][] {
        new PolylineData[] {
            new PolylineData(
                EditUi.TOOLBAR_FG_COLOR, EditUi.TOOLBAR_FG_ALPHA,
                EditUi.TOOLBAR_FG_WIDTH, 0.06f,
                new Vector2f(-0.15f * -1 + 0.5f, 0.2f),
                new Vector2f(+0.15f * -1 + 0.5f, 0.5f),
                new Vector2f(-0.15f * -1 + 0.5f, 0.8f)
            )
        },
        new PolylineData[] {
            new PolylineData(
                EditUi.TOOLBAR_FG_COLOR, EditUi.TOOLBAR_FG_ALPHA,
                EditUi.TOOLBAR_FG_WIDTH, 0.06f,
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
        e.setSize(new Vector2f(EditUi.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }




    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        //Empty
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
                .rotY(shop.getCanvasDirection() / 8f * (float)(2f * Math.PI))
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
