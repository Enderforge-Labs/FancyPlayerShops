package com.snek.fancyplayershops.implementations.ui.details;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.CanvasBackground;
import com.snek.fancyplayershops.implementations.ui.ShopCanvas;
import com.snek.fancyplayershops.implementations.ui.misc.ShopPanelElm;
import com.snek.fancyplayershops.implementations.ui.misc.ShopUiBorder;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.displays.CustomTextDisplay;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.elements.Elm;
import com.snek.framework.ui.elements.TextElm;
import com.snek.framework.utils.Utils;

import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity.TextAlignment;







//TODO add small text elements in a corner of the UIs that tell you what each mouse button does when clicked.
//TODO they change based on the player's currently hovered element.



/**
 * A UI that shows informations about the shop.
 */
public class DetailsUi extends ShopCanvas {

    // Colors
    public static final int LOW_S_COLOR_MIN = 110;
    public static final Vector3i C_RGB_PRICE      = new Vector3i(243, 255, LOW_S_COLOR_MIN);
    public static final Vector3f C_HSV_STOCK_HIGH = Utils.RGBtoHSV(new Vector3i(LOW_S_COLOR_MIN, 223, LOW_S_COLOR_MIN)); //! Float instead of int for more precision
    public static final Vector3f C_HSV_STOCK_LOW  = Utils.RGBtoHSV(new Vector3i(200, LOW_S_COLOR_MIN, LOW_S_COLOR_MIN)); //! Float instead of int for more precision

    // Layout
    public static final float BACKGROUND_HEIGHT = 0.4f;
    public static final float VERTICAL_PADDING = 0.02f;
    public static final float NAMES_VALUES_WIDTH_RATIO = 0.3f;
    public static final float HEAD_SIZE = 0.2f;




    /**
     * Creates a new DetailsUi.
     * @param _shop The target shop.
     */
    public DetailsUi(Shop _shop){

        // Call superconstructor and add background
        super(new CanvasBackground(_shop), new CanvasBackground(_shop));
        bg.setPosY(1 - DetailsUi.BACKGROUND_HEIGHT);
        bg.setSize(new Vector2f(1, BACKGROUND_HEIGHT));
        back.setPosY(1 - DetailsUi.BACKGROUND_HEIGHT);
        back.setSize(new Vector2f(1, BACKGROUND_HEIGHT));
        back.applyAnimationNow(new Transition().additiveTransform(new Transform().rotY((float)Math.PI)));
        Div e;

        // Add title
        e = bg.addChild(new DetailsUiTitle(_shop));
        e.setAlignmentX(AlignmentX.CENTER);
        e.setSizeX(1f);
        e.setAbsSizeY(TextElm.calcHeight((Elm)e));
        e.setPosY(BACKGROUND_HEIGHT - e.getAbsSize().y - VERTICAL_PADDING - 0.04f); //! -0.04 is a workaround and should not be required

        // Add details display names
        e = bg.addChild(new DetailsUiDisplayNames(_shop));
        e.setAlignmentX(AlignmentX.LEFT);
        ((CustomTextDisplay)((DetailsUiDisplayNames)e).getEntity()).setTextAlignment(TextAlignment.LEFT);
        e.setSizeX(NAMES_VALUES_WIDTH_RATIO);
        e.setAbsSizeY(TextElm.calcHeight((Elm)e));
        e.setPosY(VERTICAL_PADDING);

        // Add details display values
        e = bg.addChild(new DetailsUiDisplayValues(_shop));
        e.setAlignmentX(AlignmentX.RIGHT);
        ((CustomTextDisplay)((DetailsUiDisplayValues)e).getEntity()).setTextAlignment(TextAlignment.LEFT);
        e.setSizeX(1 - NAMES_VALUES_WIDTH_RATIO);
        e.setAbsSizeY(TextElm.calcHeight((Elm)e));
        e.setPosY(VERTICAL_PADDING);

        // Add owner's head
        e = bg.addChild(new DetailsUiOwnerHead(_shop));
        e.setAlignmentX(AlignmentX.LEFT);
        e.setAbsSize(new Vector2f(HEAD_SIZE, HEAD_SIZE));
        e.setPosY(-VERTICAL_PADDING);

        // Add borders
        e = bg.addChild(new ShopUiBorder(_shop));
        e.setAlignmentY(AlignmentY.BOTTOM);
        e = bg.addChild(new ShopUiBorder(_shop));
        e.setAlignmentY(AlignmentY.TOP);
    }
}
