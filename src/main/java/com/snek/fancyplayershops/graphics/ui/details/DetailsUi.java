package com.snek.fancyplayershops.graphics.ui.details;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.core.elements.ShopCanvas;
import com.snek.fancyplayershops.graphics.ui.details.elements.DetailsUi_Names;
import com.snek.fancyplayershops.graphics.ui.details.elements.DetailsUi_OwnerHead;
import com.snek.fancyplayershops.graphics.ui.details.elements.DetailsUi_Title;
import com.snek.fancyplayershops.graphics.ui.details.elements.DetailsUi_Values;
import com.snek.fancyplayershops.graphics.ui.details.styles.DetailsUi_OwnerHeadBg_S;
import com.snek.fancyplayershops.graphics.ui.misc.elements.DualInputIndicator;
import com.snek.fancyplayershops.graphics.ui.misc.elements.ShopPanelElm;
import com.snek.frameworklib.graphics.core.elements.CanvasBorder;

import net.minecraft.world.entity.player.Player;

import com.snek.frameworklib.data_types.ui.AlignmentX;
import com.snek.frameworklib.data_types.ui.TextAlignment;
import com.snek.frameworklib.graphics.core.Div;
import com.snek.frameworklib.graphics.core.Elm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;









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
        super(_shop, BACKGROUND_HEIGHT, CanvasBorder.DEFAULT_HEIGHT, CanvasBorder.DEFAULT_HEIGHT);
        Div e;


        // Add title
        e = bg.addChild(new DetailsUi_Title(_shop));
        e.setAlignmentX(AlignmentX.CENTER);
        e.setSizeX(1f);
        e.setAbsSizeY(((DetailsUi_Title)e).calcEntityHeight());
        e.setPosY(1 - e.getAbsSize().y - CanvasBorder.DEFAULT_HEIGHT - VERTICAL_PADDING);


        // Add details display
        final Div details = bg.addChild(new Div());
        {
            // Add details display names
            e = details.addChild(new DetailsUi_Names(_shop));
            e.setAlignmentX(AlignmentX.LEFT);
            ((Elm)e).getStyle(SimpleTextElmStyle.class).setTextAlignment(TextAlignment.LEFT);
            e.setSize(new Vector2f(NAMES_VALUES_WIDTH_RATIO, 1f));

            // Add details display values
            e = details.addChild(new DetailsUi_Values(_shop));
            e.setAlignmentX(AlignmentX.RIGHT);
            ((Elm)e).getStyle(SimpleTextElmStyle.class).setTextAlignment(TextAlignment.LEFT);
            e.setSize(new Vector2f(1f - NAMES_VALUES_WIDTH_RATIO, 1f));
            values = (DetailsUi_Values)e;
        }
        details.setSizeX(DETAILS_W);
        details.setSizeY(((DetailsUi_Names)details.getChildren().get(0)).calcEntityHeight());
        details.setAlignmentX(AlignmentX.CENTER);
        details.setPosY(H0 + VERTICAL_PADDING);


        // Add owner's head's background
        final Div headBg = bg.addChild(new ShopPanelElm(_shop, new DetailsUi_OwnerHeadBg_S()));
        headBg.setSize(HEAD_BG_SIZE);
        headBg.setPosY(H0 - HEAD_BG_SIZE.y);
        headBg.setAlignmentX(AlignmentX.LEFT);


        // Add owner's head
        e = headBg.addChild(new DetailsUi_OwnerHead(_shop));
        e.setSize(new Vector2f(1f));
        e.setAlignmentX(AlignmentX.CENTER);
        e.setPosY(0.03f);


        // Add input indicators
        e = bg.addChild(new DualInputIndicator(_shop));
        e.setSize(DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE);
        e.setPos(new Vector2f(HEAD_BG_SIZE.x, H0 - (DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE.y + HEAD_BG_SIZE.y) / 2));
        final DualInputIndicator inputIndicator = (DualInputIndicator)e;

        // Set input indicator text
        final Player player = canvas.getContext().getPlayer();
        inputIndicator.getLmbIndicator().updateDisplay("Buy 1 item");
        inputIndicator.getRmbIndicator().updateDisplay(player.getUUID().equals(_shop.getOwnerUuid()) ? "Edit shop" : "Bulk buy options");
    }




    @Override
    public void onStockChange() {
        values.updateDisplay();
    }
}
