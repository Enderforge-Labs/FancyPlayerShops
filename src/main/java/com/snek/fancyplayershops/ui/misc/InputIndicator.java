package com.snek.fancyplayershops.ui.misc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3d;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.styles.InputIndicatorText_S;
import com.snek.fancyplayershops.ui.misc.styles.MouseButtonDown_S;
import com.snek.fancyplayershops.ui.misc.styles.MouseButtonUp_S;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.elements.Elm;
import com.snek.framework.ui.elements.TextElm;
import com.snek.framework.ui.styles.TextElmStyle;
import com.snek.framework.utils.Easings;
import com.snek.framework.utils.Txt;

import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity.TextAlignment;
import net.minecraft.util.ClickType;








/**
 * A UI element that can display the actions mouse clicks would perform on the currently hovered element.
 */
public class InputIndicator extends ShopDiv {
    public static final Vector2f BUTTON_SIZE = new Vector2f(0.5f, 0.4f);
    public static final Vector2f MOUSE_SIZE  = new Vector2f(0.025f, 1f);
    public static final float BUTTON_TEXT_SPACING = 0.025f / 2f;

    public static final float HIDDEN_SIZE = 1000f;
    private static final Transition hideAnimation = new Transition().additiveTransform(new Transform().moveY(-HIDDEN_SIZE));
    private static final Transition showAnimation = new Transition().additiveTransform(new Transform().moveY(+HIDDEN_SIZE));
    // private static final Transition hideAnimation = new Transition().additiveTransform(new Transform().scale(HIDDEN_SIZE));
    // private static final Transition showAnimation = new Transition().additiveTransform(new Transform().scale(1 / HIDDEN_SIZE));


    public final @NotNull ShopTextElm text; //FIXME make private
    private boolean isVisible = true;
    // private int activeNum = 0;
    private @Nullable Vector3d posCache = null;
    // private @NotNull ClickType button;




    /**
     * Creates a new InputIndicator.
     * @param _shop The target shop.
     * @param button The button to display.
     */
    public InputIndicator(@NotNull Shop _shop, @NotNull ClickType _button) {
        super(_shop);
        // button = _button;
        Div e;


        // Create mouse display element
        // Div m = addChild(new Div());
        Div m = addChild(new ShopPanelElm(shop, new MouseButtonDown_S()));
        m.setSize(MOUSE_SIZE);
        m.setAlignment(AlignmentX.LEFT, AlignmentY.BOTTOM);
        {
            // Add mouse button
            // e = m.addChild(new ShopPanelElm(shop, button == ClickType.LEFT ? new MouseButtonUp_S() : new MouseButtonDown_S()));
            e = m.addChild(new ShopPanelElm(shop, new MouseButtonUp_S()));
            e.setSize(BUTTON_SIZE);
            e.setAlignment(_button == ClickType.LEFT ? AlignmentX.LEFT : AlignmentX.RIGHT, AlignmentY.TOP);

            // e = m.addChild(new ShopPanelElm(shop, button == ClickType.LEFT ? new MouseButtonDown_S() : new MouseButtonUp_S()));
            // e.setSize(BUTTON_SIZE);
            // e.setAlignment(AlignmentX.RIGHT, AlignmentY.TOP);

            // e = m.addChild(new ShopPanelElm(shop, new MouseButtonDown_S()));
            // e.setSize(new Vector2f(1f, 1 - BUTTON_SIZE.y));
            // e.setAlignment(AlignmentX.CENTER, AlignmentY.BOTTOM);
        }


        // Add text element
        e = addChild(new ShopTextElm(shop, new InputIndicatorText_S()));
        e.setSizeX(1 - MOUSE_SIZE.x - BUTTON_TEXT_SPACING);
        e.setAlignment(AlignmentX.RIGHT, AlignmentY.CENTER);
        ((Elm)e).getStyle(TextElmStyle.class).setTextAlignment(TextAlignment.LEFT);
        text = (ShopTextElm)e;


        // Manually prime animation if necessary
        updateDisplay(null);
        // applyAnimationNowRecursive(hideAnimation);
    }




    @Override
    public void spawn(Vector3d pos) {
        if(posCache == null) posCache = new Vector3d();
        posCache.set(pos);
        super.spawn(pos);
        text.setAbsSizeY(TextElm.calcHeight(text));
    }




    /**
     * Updates the display to show the new action.
     * @param description The description of the action associated with a button click. Can be null.
     */
    public void updateDisplay(@Nullable String description) {
        if(description != null) {
            // activeNum += 1;
            // if(isHidden) {
                // Div e;


                // // Create mouse display element
                // // Div m = addChild(new Div());
                // Div m = addChild(new ShopPanelElm(shop, new MouseButtonDown_S()));
                // m.setSize(MOUSE_SIZE);
                // m.setAlignment(AlignmentX.LEFT, AlignmentY.BOTTOM);
                // {
                //     // Add mouse button
                //     // e = m.addChild(new ShopPanelElm(shop, button == ClickType.LEFT ? new MouseButtonUp_S() : new MouseButtonDown_S()));
                //     e = m.addChild(new ShopPanelElm(shop, new MouseButtonUp_S()));
                //     e.setSize(BUTTON_SIZE);
                //     e.setAlignment(button == ClickType.LEFT ? AlignmentX.LEFT : AlignmentX.RIGHT, AlignmentY.TOP);

                //     // e = m.addChild(new ShopPanelElm(shop, button == ClickType.LEFT ? new MouseButtonDown_S() : new MouseButtonUp_S()));
                //     // e.setSize(BUTTON_SIZE);
                //     // e.setAlignment(AlignmentX.RIGHT, AlignmentY.TOP);

                //     // e = m.addChild(new ShopPanelElm(shop, new MouseButtonDown_S()));
                //     // e.setSize(new Vector2f(1f, 1 - BUTTON_SIZE.y));
                //     // e.setAlignment(AlignmentX.CENTER, AlignmentY.BOTTOM);
                // }


                // // Add text element
                // e = addChild(new ShopTextElm(shop, new InputIndicatorText_S()));
                // e.setSizeX(1 - MOUSE_SIZE.x - BUTTON_TEXT_SPACING);
                // e.setAlignment(AlignmentX.RIGHT, AlignmentY.CENTER);
                // e.setAbsSizeY(TextElm.calcHeight((Elm)e));
                // ((Elm)e).getStyle(TextElmStyle.class).setText(new Txt(description).lightGray().get());
                // ((Elm)e).getStyle(TextElmStyle.class).setTextAlignment(TextAlignment.LEFT);
            text.getStyle(TextElmStyle.class).setText(new Txt(description).lightGray().get());
            if(!isVisible) {
                applyAnimationNowRecursive(showAnimation);
            // text.flushStyle();

                // System.out.println("Applied: show." + text.getStyle().getTransform().getPos().y); //TODO


                // Spawn children
                isVisible = true;
            }
        }
        else {
            if(isVisible) {
            // activeNum = Math.max(0, activeNum - 1);
            // if(!isHidden && activeNum == 0) {
            // if(!isHidden) {
                applyAnimationNowRecursive(hideAnimation);
                // System.out.println("Applied: hide." + text.getStyle().getTransform().getPos().y); //TODO
                // System.out.println("Applied: hide." + text.getStyle().getTransform().getPos());
                // for (Div c : children) c.despawnNow();
                // text.flushStyle();
            // }
                isVisible = false;
            }
        }
    }
}
