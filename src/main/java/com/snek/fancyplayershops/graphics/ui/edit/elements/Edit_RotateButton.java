package com.snek.fancyplayershops.graphics.ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.GetShop;
import com.snek.fancyplayershops.graphics.ui.core.elements.ShopItemDisplayElm;
import com.snek.fancyplayershops.graphics.ui.edit.styles.Edit_RotateButtonLeft_S;
import com.snek.fancyplayershops.graphics.ui.edit.styles.Edit_RotateButtonRight_S;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.graphics.core.Canvas;
import com.snek.frameworklib.graphics.designs.SymbolDesigns;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.functional.elements.SimpleButtonElm;
import com.snek.frameworklib.graphics.functional.elements.__base_ButtonElm;
import com.snek.frameworklib.graphics.composite.elements.PolylineSetElm;
import com.snek.frameworklib.utils.Easings;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








/**
 * A button that allows the owner of the shop to change the default rotation of the displayed object.
 */
public class Edit_RotateButton extends SimpleButtonElm {
    public static final int ROTATION_ANIMATION_TIME = 8;
    private final float rotation;




    /**
     * Creates a new RotateButton.
     * @param _shop The target shop.
     * @param _rotateAngle The angle to add to the default rotation each time this button is pressed.
     */
    public Edit_RotateButton(final @NotNull Shop _shop, final float _rotation) {
        super(
            _shop.getLevel(),
            "Rotate once",
            "Rotate quickly",
            2,
            _rotation > 0 ? new Edit_RotateButtonRight_S(_shop) : new Edit_RotateButtonLeft_S(_shop)
        );
        rotation = _rotation;


        // Create design
        final Div e = addChild(new PolylineSetElm(
            _shop.getLevel(),
            _rotation > 0 ? SymbolDesigns.ArrowHeadPointingLeft : SymbolDesigns.ArrowHeadPointingRight
        ));
        e.setSize(new Vector2f(FancyPlayerShops.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        super.onClick(player, click);
        final Shop shop = GetShop.get(this);
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

        __base_ButtonElm.playButtonSound(player);
    }




    @Override
    public void onHoverEnter(final @NotNull Player player) {
        super.onHoverEnter(player);

        // Handle item display animations
        final Shop shop = GetShop.get(this);
        final ShopItemDisplayElm itemDisplay = shop.getItemDisplay();
        itemDisplay.stopLoopAnimation();
        itemDisplay.applyAnimation(
            new Transition(ROTATION_ANIMATION_TIME, Easings.expOut)
            .targetTransform(
                itemDisplay.genLastTransform()
                .setRot(new Quaternionf())
                .rotY(canvas.getContext().getRotation() / 8f * (float)(2f * Math.PI))
            )
        );
    }




    @Override
    public void onHoverExit(final @Nullable Player player) {
        super.onHoverExit(player);

        // Handle item display animations
        final Shop shop = GetShop.get(this);
        shop.getItemDisplay().startLoopAnimation();
    }
}
