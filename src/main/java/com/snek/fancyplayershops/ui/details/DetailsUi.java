package com.snek.fancyplayershops.ui.details;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.ShopCanvas;
import com.snek.fancyplayershops.ui.details.styles.DetailsUi_OwnerHeadBg_S;
import com.snek.fancyplayershops.ui.misc.ShopPanelElm;
import com.snek.fancyplayershops.ui.misc.ShopUiBorder;
import com.snek.framework.data_types.displays.CustomTextDisplay;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.elements.Elm;
import com.snek.framework.ui.elements.TextElm;
import com.snek.framework.ui.styles.TextElmStyle;
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
    public static final float H0 = 1 - BACKGROUND_HEIGHT;
    public static final float VERTICAL_PADDING = 0.02f;
    public static final float DETAILS_W = 0.9f;
    public static final float NAMES_VALUES_WIDTH_RATIO = 0.35f;
    public static final float HEAD_SIZE = 0.2f;
    public static final Vector2f HEAD_BG_SIZE = new Vector2f(HEAD_SIZE, HEAD_SIZE - 2 * VERTICAL_PADDING);




    /**
     * Creates a new DetailsUi.
     * @param _shop The target shop.
     */
    public DetailsUi(Shop _shop) {

        // Call superconstructor
        super(_shop, BACKGROUND_HEIGHT, ShopUiBorder.DEFAULT_HEIGHT, ShopUiBorder.DEFAULT_HEIGHT);
        Div e;


        // Add title
        e = bg.addChild(new DetailsUi_Title(_shop));
        e.setAlignmentX(AlignmentX.CENTER);
        e.setSizeX(1f);
        e.setAbsSizeY(TextElm.calcHeight((Elm)e));
        e.setPosY(1 - e.getAbsSize().y - VERTICAL_PADDING - 0.04f); //! -0.04 is a workaround and should not be required


        // Add details display
        Div details = bg.addChild(new Div());
        {
            // Add details display names
            e = details.addChild(new DetailsUi_Names(_shop));
            e.setAlignmentX(AlignmentX.LEFT);
            ((Elm)e).getStyle(TextElmStyle.class).setTextAlignment(TextAlignment.LEFT);
            e.setSize(new Vector2f(NAMES_VALUES_WIDTH_RATIO, 1f));

            // Add details display values
            e = details.addChild(new DetailsUi_Values(_shop));
            e.setAlignmentX(AlignmentX.RIGHT);
            ((Elm)e).getStyle(TextElmStyle.class).setTextAlignment(TextAlignment.LEFT);
            e.setSize(new Vector2f(1f - NAMES_VALUES_WIDTH_RATIO, 1f));
        }
        details.setSizeX(DETAILS_W);
        details.setAbsSizeY(TextElm.calcHeight((TextElm)details.getChildren().get(0)));
        details.setAlignmentX(AlignmentX.CENTER);
        details.setPosY(H0 + VERTICAL_PADDING);


        // Add owner's head's background
        e = bg.addChild(new ShopPanelElm(_shop, new DetailsUi_OwnerHeadBg_S()));
        e.setAbsSize(HEAD_BG_SIZE);
        e.setPosY(H0 - HEAD_BG_SIZE.y);
        e.setAlignmentX(AlignmentX.LEFT);


        // Add owner's head
        e = bg.addChild(new DetailsUi_OwnerHead(_shop));
        e.setAlignmentX(AlignmentX.LEFT);
        e.setAbsSize(new Vector2f(HEAD_SIZE, HEAD_SIZE));
        e.setPosY(H0 - VERTICAL_PADDING);
    }
}
