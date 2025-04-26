package com.snek.fancyplayershops.implementations.ui.edit;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3i;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.ShopCanvas;
import com.snek.fancyplayershops.implementations.ui.ShopItemDisplay;
import com.snek.fancyplayershops.implementations.ui.details.DetailsUi;
import com.snek.fancyplayershops.implementations.ui.misc.ShopFancyTextElm;
import com.snek.fancyplayershops.implementations.ui.misc.ShopUiBorder;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.elements.Elm;
import com.snek.framework.utils.Easings;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;







//TODO add left click functionality to the item selector that lets you open a 1-slot UI
//TODO to read the item's name, lore and tags as if it were in a normal chest

//TODO add small text elements in a corner of the UIs that tell you what each mouse button does when clicked.
//TODO they change based on the player's currently hovered element.

/**
 * A UI that allows the owner of the shop to edit it.
 */
public class EditUi extends ShopCanvas {
    //TODO MOVE TO SHOP CANVAS. It takes the borders, the background and the back side and changes their sizes on spawn
    private final @NotNull Elm bottomBorder;
    private final @NotNull Elm title;
    public @NotNull Elm getBottomBorder() { return bottomBorder; }
    public @NotNull Elm getTitle() { return title; }
    //TODO MOVE TO SHOP CANVAS. It takes the borders, the background and the back side and changes their sizes on spawn


    // Colors
    public static final Vector3i RGB_STOCK_COLOR = Utils.HSVtoRGB(DetailsUi.C_HSV_STOCK_HIGH);


    // Layout
    public static final float ROTATE_BUTTON_Y            = 0.4f - ShopCanvas.SQUARE_BUTTON_SIZE / 2 + ShopItemDisplay.FOCUS_HEIGHT;
    public static final float ROTATE_BUTTON_CENTER_SHIFT = 0.3f;

    public static final float ITEM_SELECTOR_SIZE         = 0.25f;
    public static final float ITEM_SELECTOR_Y            = 0.4f - ITEM_SELECTOR_SIZE / 2 + ShopItemDisplay.FOCUS_HEIGHT;


    // Functionalities
    public static final float ROTATE_BUTTON_AMOUNT = (float)Math.toRadians(45);








    /**
     * Creates a new EditUi.
     * @param _shop The target shop.
     */
    public EditUi(Shop _shop){


        //TODO MOVE TO SHOP CANVAS. It takes the borders, the background and the back side and changes their sizes on spawn
        // Call superconstructor and add background
        super(_shop.getActiveCanvas().getBackground(), _shop.getActiveCanvas().getBack());
        Div e;

        // Instantly despawn and remove previous children
        for (Div c : bg.getChildren()) c.despawnNow();
        bg.clearChildren();

        // Reset size and position, visually simulate the previous values using an instant animation
        bg.setSizeY(1);
        bg.setPosY(0);
        bg.applyAnimationNow(
            new Transition()
            .additiveTransform(new Transform().scaleY(DetailsUi.BACKGROUND_HEIGHT).moveY(1 - DetailsUi.BACKGROUND_HEIGHT))
        );
        back.setSizeY(1);
        back.setPosY(0);
        back.applyAnimationNow(
            new Transition()
            .additiveTransform(new Transform().scaleY(DetailsUi.BACKGROUND_HEIGHT).moveY(1 - DetailsUi.BACKGROUND_HEIGHT))
        );
        //TODO MOVE TO SHOP CANVAS. It takes the borders, the background and the back side and changes their sizes on spawn




        // Add title
        e = bg.addChild(new EditUi_Title(_shop));
        e.moveY(1f - ShopFancyTextElm.LINE_H * 1f);
        e.setAlignmentX(AlignmentX.CENTER);
        title = (Elm)e;

        // Add price button
        e = bg.addChild(new EditUi_PriceButton(_shop));
        e.moveY(1f - ShopFancyTextElm.LINE_H * 2f);
        e.setAlignmentX(AlignmentX.LEFT);

        // Add stock limit button
        e = bg.addChild(new EditUi_StockLimitButton(_shop));
        e.moveY(1f - ShopFancyTextElm.LINE_H * 3f);
        e.setAlignmentX(AlignmentX.LEFT);

        // Add rotation buttons
        e = bg.addChild(new EditUi_RotateButton(_shop, -ROTATE_BUTTON_AMOUNT, new Txt("◀").get()));
        e.move(new Vector2f(-ROTATE_BUTTON_CENTER_SHIFT, ROTATE_BUTTON_Y));
        e = bg.addChild(new EditUi_RotateButton(_shop, +ROTATE_BUTTON_AMOUNT, new Txt("▶").get()));
        e.move(new Vector2f(+ROTATE_BUTTON_CENTER_SHIFT, ROTATE_BUTTON_Y));

        // Add item selector
        e = bg.addChild(new EditUi_ItemSelector(_shop));
        e.moveY(ITEM_SELECTOR_Y);

        // Add bottom border
        e = bg.addChild(new ShopUiBorder(_shop));
        e.applyAnimationNow(
            new Transition(0,Easings.linear)
            .additiveTransform(new Transform().moveY(1 - DetailsUi.BACKGROUND_HEIGHT))
        );
        bottomBorder = (Elm)e;
    }








    @Override
    public void spawn(Vector3d pos){

        //TODO MOVE TO SHOP CANVAS. It takes the borders, the background and the back side and changes their sizes on spawn
        // Only spawn the children of the background element. The background itself is already spawned
        for (Div c : bg.getChildren()) {
            c.spawn(pos);
        }

        // Apply an animation to the background to make it look like it's stretching back to the normal height
        bg.applyAnimation(
            new Transition(ShopCanvas.SPAWN_SIZE_TIME, Easings.sineOut)
            .additiveTransform(new Transform().scaleY(1 / DetailsUi.BACKGROUND_HEIGHT).moveY(-(1 - DetailsUi.BACKGROUND_HEIGHT)))
        );
        back.applyAnimation(
            new Transition(ShopCanvas.SPAWN_SIZE_TIME, Easings.sineOut)
            .additiveTransform(new Transform().scaleY(1 / DetailsUi.BACKGROUND_HEIGHT).moveY(-(1 - DetailsUi.BACKGROUND_HEIGHT)))
        );
        bottomBorder.applyAnimation(
            new Transition(ShopCanvas.SPAWN_SIZE_TIME, Easings.sineOut)
            .additiveTransform(new Transform().moveY(-(1 - DetailsUi.BACKGROUND_HEIGHT)))
        );
        //TODO MOVE TO SHOP CANVAS. It takes the borders, the background and the back side and changes their sizes on spawn
    }
}
