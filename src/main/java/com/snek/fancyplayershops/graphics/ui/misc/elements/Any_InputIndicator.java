package com.snek.fancyplayershops.graphics.ui.misc.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3d;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.misc.styles.Any_InputIndicator_Text_S;
import com.snek.fancyplayershops.graphics.ui.misc.styles.Any_InputIndicator_MouseButtonDown_S;
import com.snek.fancyplayershops.graphics.ui.misc.styles.Any_InputIndicator_MouseButtonUp_S;
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
public class Any_InputIndicator extends ShopDiv {
    public static final @NotNull Vector2f BUTTON_SIZE = new Vector2f(0.5f, 0.4f);   // The size of a mouse button compared to the mouse display
    public static final @NotNull Vector2f MOUSE_SIZE  = new Vector2f(0.025f, 1f);   // The size of the mouse display compared to the InputIndicator
    public static final float BUTTON_TEXT_SPACING = 0.025f / 2f;                    // The distance between mouse display and text
    private final @NotNull ShopTextElm text;




    /**
     * Creates a new InputIndicator.
     * @param _shop The target shop.
     * @param button The button to display.
     */
    public Any_InputIndicator(final @NotNull Shop _shop, final @NotNull ClickAction _button) {
        super(_shop);
        Div e;


        // Create mouse display element
        final Div m = addChild(new ShopPanelElm(shop, new Any_InputIndicator_MouseButtonDown_S()));
        m.setSize(MOUSE_SIZE);
        m.setAlignment(AlignmentX.LEFT, AlignmentY.BOTTOM);
        {
            // Add mouse button
            e = m.addChild(new ShopPanelElm(shop, new Any_InputIndicator_MouseButtonUp_S()));
            e.setSize(BUTTON_SIZE);
            e.setAlignment(_button == ClickAction.PRIMARY ? AlignmentX.LEFT : AlignmentX.RIGHT, AlignmentY.TOP);
        }


        // Add text element
        e = addChild(new ShopTextElm(shop, new Any_InputIndicator_Text_S()));
        e.setSize(new Vector2f(1 - MOUSE_SIZE.x - BUTTON_TEXT_SPACING, 1f));
        e.setAlignment(AlignmentX.RIGHT, AlignmentY.BOTTOM);
        ((Elm)e).getStyle(SimpleTextElmStyle.class).setTextAlignment(TextAlignment.LEFT);
        text = (ShopTextElm)e;
    }




    @Override
    public void spawn(@NotNull Vector3d pos, boolean animate) {
        // Empty
    }




    /**
     * Updates the display to show the new action.
     * @param description The description of the action associated with a button click. Can be null.
     */
    public void updateDisplay(final @Nullable String description) {

        // If the description is not null and the display is hidden, show it and update the text
        if(description != null) {
            text.getStyle(SimpleTextElmStyle.class).setText(new Txt(description).lightGray().get());
            super.spawn(canvas.getContext().getSpawnPos(), false);
        }

        // If the description is null and the display is visible, hide it.
        else {
            super.despawn(false);
        }
    }
}
