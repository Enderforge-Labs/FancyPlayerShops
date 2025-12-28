package com.snek.fancyplayershops.graphics.ui.details;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.misc.elements.TitleElm;
import com.snek.fancyplayershops.graphics.ui.core.elements.ProductCanvasBase;
import com.snek.fancyplayershops.graphics.ui.details.elements.Details_Names;
import com.snek.fancyplayershops.graphics.ui.details.elements.Details_OwnerHead;
import com.snek.fancyplayershops.graphics.ui.details.elements.Details_Values;
import com.snek.fancyplayershops.graphics.ui.details.styles.Details_OwnerHeadBg_S;
import com.snek.frameworklib.graphics.core.elements.CanvasBorder;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;

import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.TextAlignment;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.core.elements.Elm;
import com.snek.frameworklib.graphics.basic.elements.PanelElm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.graphics.composite.elements.DualInputIndicator;









/**
 * A UI that shows informations about the product.
 */
public class DetailsCanvas extends ProductCanvasBase {
    private final @NotNull Details_Values values;


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
     * @param display The target product display.
     */
    public DetailsCanvas(final @NotNull ProductDisplay display) {

        // Call superconstructor
        super(display, BACKGROUND_HEIGHT, CanvasBorder.DEFAULT_HEIGHT, CanvasBorder.DEFAULT_HEIGHT);
        Div e;


        // Add title
        e = bg.addChild(new TitleElm(display.getLevel(), recalculateTitle()));
        e.setAlignmentX(AlignmentX.CENTER);
        e.setSizeX(TitleElm.DEFAULT_W);
        e.setAbsSizeY(((TitleElm)e).calcTotEntityHeight());
        e.setPosY(1 - e.getAbsSize().y - CanvasBorder.DEFAULT_HEIGHT - VERTICAL_PADDING);


        // Add details display
        final Div details = bg.addChild(new Div());
        {
            // Add details display names
            e = details.addChild(new Details_Names(display));
            e.setAlignmentX(AlignmentX.LEFT);
            ((Elm)e).getStyle(SimpleTextElmStyle.class).setTextAlignment(TextAlignment.LEFT);
            e.setSize(new Vector2f(NAMES_VALUES_WIDTH_RATIO, 1f));

            // Add details display values
            e = details.addChild(new Details_Values(display));
            e.setAlignmentX(AlignmentX.RIGHT);
            ((Elm)e).getStyle(SimpleTextElmStyle.class).setTextAlignment(TextAlignment.LEFT);
            e.setSize(new Vector2f(1f - NAMES_VALUES_WIDTH_RATIO, 1f));
            values = (Details_Values)e;
        }
        details.setSizeX(DETAILS_W);
        details.setSizeY(0.25f);
        details.setAlignmentX(AlignmentX.CENTER);
        details.setPosY(H0 + VERTICAL_PADDING);


        // Add owner's head's background
        final Div headBg = bg.addChild(new PanelElm(display.getLevel(), new Details_OwnerHeadBg_S()));
        headBg.setSize(HEAD_BG_SIZE);
        headBg.setPosY(H0 - HEAD_BG_SIZE.y);
        headBg.setAlignmentX(AlignmentX.LEFT);


        // Add owner's head
        e = headBg.addChild(new Details_OwnerHead(display));
        e.setSize(new Vector2f(1f));
        e.setAlignmentX(AlignmentX.CENTER);
        e.setPosY(0.03f);


        // Add input indicators
        e = bg.addChild(new DualInputIndicator(display.getLevel()));
        e.setSize(DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE);
        e.setPos(new Vector2f(HEAD_BG_SIZE.x, H0 - (DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE.y + HEAD_BG_SIZE.y) / 2));
        final DualInputIndicator inputIndicator = (DualInputIndicator)e;

        // Force indicator text //! Details canvas doesn't have any buttons. Instead, it respons to click events directly
        final Player player = canvas.getContext().getPlayer();
        inputIndicator.getLmbIndicator().updateDisplay("Buy 1 item");
        inputIndicator.getRmbIndicator().updateDisplay(player.getUUID().equals(display.getOwnerUuid()) ? "Edit shop" : "Bulk buy options");
    }



    public @NotNull Component recalculateTitle() {
        return display.getItem().is(Items.AIR) ? ProductDisplay.EMPTY_PRODUCT_DISPLAY_NAME : Component.literal(display.getStandaloneName());
    }




    @Override
    public void onStockChange() {
        values.updateDisplay();
    }




    @Override
    protected void updateItemDisplayRot(final int from, final int to, final boolean instant) {
        //! Empty. This stops the item display from changing global rotation when the details UI rotates.
    }

}
