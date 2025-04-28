package com.snek.fancyplayershops.ui.misc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3d;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.styles.InputIndicatorText_S;
import com.snek.fancyplayershops.ui.misc.styles.MouseButtonDown_S;
import com.snek.fancyplayershops.ui.misc.styles.MouseButtonUp_S;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.elements.Elm;
import com.snek.framework.ui.elements.TextElm;
import com.snek.framework.ui.styles.TextElmStyle;
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


    private final @NotNull ShopTextElm text;




    /**
     * Creates a new InputIndicator.
     * @param _shop The target shop.
     * @param button The button to display.
     */
    public InputIndicator(@NotNull Shop _shop, @NotNull ClickType button){
        super(_shop);
        Div e;


        // Create mouse display element
        Div m = addChild(new Div());
        m.setSize(MOUSE_SIZE);
        m.setAlignment(AlignmentX.LEFT, AlignmentY.BOTTOM);
        {
            // Add mouse buttons and main part
            e = m.addChild(new ShopPanelElm(shop, button == ClickType.LEFT ? new MouseButtonUp_S() : new MouseButtonDown_S()));
            e.setSize(BUTTON_SIZE);
            e.setAlignment(AlignmentX.LEFT, AlignmentY.TOP);

            e = m.addChild(new ShopPanelElm(shop, button == ClickType.LEFT ? new MouseButtonDown_S() : new MouseButtonUp_S()));
            e.setSize(BUTTON_SIZE);
            e.setAlignment(AlignmentX.RIGHT, AlignmentY.TOP);

            e = m.addChild(new ShopPanelElm(shop, new MouseButtonDown_S()));
            e.setSize(new Vector2f(1f, 1 - BUTTON_SIZE.y));
            e.setAlignment(AlignmentX.CENTER, AlignmentY.BOTTOM);
        }


        // Add text element
        e = addChild(new ShopTextElm(_shop, new InputIndicatorText_S()));
        e.setSizeX(1 - MOUSE_SIZE.x - BUTTON_TEXT_SPACING);
        e.setAlignment(AlignmentX.RIGHT, AlignmentY.CENTER);
        ((Elm)e).getStyle(TextElmStyle.class).setTextAlignment(TextAlignment.LEFT);
        text = (ShopTextElm)e;
    }



    @Override
    public void spawn(Vector3d pos) {
        super.spawn(pos);
        text.setAbsSizeY(TextElm.calcHeight(text));
    }




    /**
     * Updates the display to show the new action.
     * @param description The description of the action associated with a button click. Can be null.
     */
    public void updateDisplay(@Nullable String description){
        if(description != null) {
            text.getStyle(TextElmStyle.class).setText(new Txt(description).lightGray().get());
        }
        else {
            //FIXME hide the text
        }
        text.flushStyle();
    }
}
