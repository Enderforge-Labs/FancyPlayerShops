package com.snek.fancyplayershops.graphics.ui.misc.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.misc.styles.InputIndicatorText_S;
import com.snek.fancyplayershops.graphics.ui.misc.styles.MouseButtonDown_S;
import com.snek.fancyplayershops.graphics.ui.misc.styles.MouseButtonUp_S;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.data_types.graphics.TextAlignment;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.core.elements.Elm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.world.inventory.ClickAction;








/**
 * A UI element that can display the actions mouse clicks would perform on the currently hovered element.
 */
public class InputIndicator extends ShopDiv {
    public static final @NotNull Vector2f BUTTON_SIZE = new Vector2f(0.5f, 0.4f);   // The size of a mouse button compared to the mouse display
    public static final @NotNull Vector2f MOUSE_SIZE  = new Vector2f(0.025f, 1f);   // The size of the mouse display compared to the InputIndicator
    public static final float BUTTON_TEXT_SPACING = 0.025f / 2f;                    // The distance between mouse display and text

    public static final float HIDDEN_TRANSLATION = 1000f;                           // The distance elements are moved by in order to hide them
    private static final @NotNull Transition hideAnimation = new Transition().additiveTransform(new Transform().moveY(-HIDDEN_TRANSLATION));
    private static final @NotNull Transition showAnimation = new Transition().additiveTransform(new Transform().moveY(+HIDDEN_TRANSLATION));


    private final @NotNull ShopTextElm text;
    private boolean isVisible = true;




    /**
     * Creates a new InputIndicator.
     * @param _shop The target shop.
     * @param button The button to display.
     */
    public InputIndicator(final @NotNull Shop _shop, final @NotNull ClickAction _button) {
        super(_shop);
        Div e;


        // Create mouse display element
        final Div m = addChild(new ShopPanelElm(shop, new MouseButtonDown_S()));
        m.setSize(MOUSE_SIZE);
        m.setAlignment(AlignmentX.LEFT, AlignmentY.BOTTOM);
        {
            // Add mouse button
            e = m.addChild(new ShopPanelElm(shop, new MouseButtonUp_S()));
            e.setSize(BUTTON_SIZE);
            e.setAlignment(_button == ClickAction.PRIMARY ? AlignmentX.LEFT : AlignmentX.RIGHT, AlignmentY.TOP);
        }


        // Add text element
        e = addChild(new ShopTextElm(shop, new InputIndicatorText_S()));
        e.setSize(new Vector2f(1 - MOUSE_SIZE.x - BUTTON_TEXT_SPACING, 1f));
        e.setAlignment(AlignmentX.RIGHT, AlignmentY.BOTTOM);
        ((Elm)e).getStyle(SimpleTextElmStyle.class).setTextAlignment(TextAlignment.LEFT);
        text = (ShopTextElm)e;


        // Manually prime animation if necessary
        updateDisplay(null);
    }




    /**
     * Updates the display to show the new action.
     * @param description The description of the action associated with a button click. Can be null.
     */
    public void updateDisplay(final @Nullable String description) {

        // If the description is not null and the display is hidden, show it and update the text
        if(description != null) {
            text.getStyle(SimpleTextElmStyle.class).setText(new Txt(description).lightGray().get());
            if(!isVisible) {
                applyAnimationNowRecursive(showAnimation);
                isVisible = true;
            }
        }

        // If the description is null and the display is visible, hide it.
        else {
            if(isVisible) {
                applyAnimationNowRecursive(hideAnimation);
                isVisible = false;
            }
        }
    }
}
