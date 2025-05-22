package com.snek.fancyplayershops.shop_ui.details;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.shop_ui._elements.ShopCanvas;
import com.snek.fancyplayershops.shop_ui.details.elements.DetailsUi_Names;
import com.snek.fancyplayershops.shop_ui.details.elements.DetailsUi_OwnerHead;
import com.snek.fancyplayershops.shop_ui.details.elements.DetailsUi_Title;
import com.snek.fancyplayershops.shop_ui.details.elements.DetailsUi_Values;
import com.snek.fancyplayershops.shop_ui.details.styles.DetailsUi_OwnerHeadBg_S;
import com.snek.fancyplayershops.shop_ui.misc.elements.ShopPanelElm;
import com.snek.fancyplayershops.ui._elements.UiBorder;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.TextAlignment;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.elements.Elm;
import com.snek.framework.ui.elements.TextElm;
import com.snek.framework.ui.elements.styles.TextElmStyle;









/**
 * A UI that shows informations about the shop.
 */
public class DetailsUi extends ShopCanvas {
    private final @NotNull DetailsUi_Values values;


    // Colors
    public static final int LOW_S_COLOR_MIN = 110;
    public static final @NotNull Vector3i C_RGB_PRICE      = new Vector3i(243, 255, LOW_S_COLOR_MIN);
    public static final @NotNull Vector3i C_HSV_STOCK_HIGH = new Vector3i(LOW_S_COLOR_MIN, 223, LOW_S_COLOR_MIN);
    public static final @NotNull Vector3i C_HSV_STOCK_LOW  = new Vector3i(200, LOW_S_COLOR_MIN, LOW_S_COLOR_MIN);

    // Layout
    public static final float BACKGROUND_HEIGHT = 0.4f;         // The absolute height of the background element
    public static final float H0 = 1 - BACKGROUND_HEIGHT;       // The height at which the background element starts
    public static final float VERTICAL_PADDING = 0.02f;         // The distance of elements from the top and bottom edges
    public static final float DETAILS_W = 0.9f;                 // The total width of the main displays
    public static final float NAMES_VALUES_WIDTH_RATIO = 0.35f; // The ration between the width of the names and the width of the values
    public static final float HEAD_SIZE = 0.2f;                 // The size of the owner's head
    public static final @NotNull Vector2f HEAD_BG_SIZE = new Vector2f(HEAD_SIZE, HEAD_SIZE - 2 * VERTICAL_PADDING);




    /**
     * Creates a new DetailsUi.
     * @param _shop The target shop.
     */
    public DetailsUi(final @NotNull Shop _shop) {

        // Call superconstructor
        super(_shop, BACKGROUND_HEIGHT, UiBorder.DEFAULT_HEIGHT, UiBorder.DEFAULT_HEIGHT);
        Div e;


        // Add title
        e = bg.addChild(new DetailsUi_Title(_shop));
        e.setAlignmentX(AlignmentX.CENTER);
        e.setSizeX(1f);
        e.setAbsSizeY(TextElm.calcHeight((Elm)e));
        e.setPosY(1 - e.getAbsSize().y - VERTICAL_PADDING - 0.04f); //! -0.04 is a workaround and should not be required


        // Add details display
        final Div details = bg.addChild(new Div());
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
            values = (DetailsUi_Values)e;
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




    @Override
    public void onStockChange() {
        values.updateDisplay();
    }
}
