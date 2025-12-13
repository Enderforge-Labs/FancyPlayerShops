package com.snek.fancyplayershops.graphics.ui.inspect;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.misc.elements.TitleElm;
import com.snek.fancyplayershops.graphics.ui.core.elements.ShopCanvasBase;
import com.snek.fancyplayershops.graphics.ui.edit.EditCanvas;
import com.snek.fancyplayershops.graphics.ui.inspect.elements.Inspect_IdDisplay;
import com.snek.fancyplayershops.graphics.ui.inspect.elements.Inspect_InventoryViewButton;
import com.snek.fancyplayershops.graphics.ui.inspect.elements.Inspect_ModDisplay;
import com.snek.fancyplayershops.graphics.ui.misc.elements.ShopFancyTextElm;
import com.snek.frameworklib.graphics.core.elements.CanvasBorder;

import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.data_types.graphics.TextAlignment;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.core.elements.Elm;
import com.snek.frameworklib.graphics.interfaces.InputIndicatorCanvas;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.graphics.composite.elements.DualInputIndicator;
import com.snek.frameworklib.graphics.composite.elements.InputIndicator;








/**
 * A UI that allows the user of a shop to view details about the item.
 */
public class InspectCanvas extends ShopCanvasBase implements InputIndicatorCanvas {
    public static final float DETAILS_W = 0.9f;                 // The total width of the main displays
    public static final float NAMES_VALUES_WIDTH_RATIO = 0.35f; // The ration between the width of the names and the width of the values


    private final @NotNull DualInputIndicator inputIndicator;








    /**
     * Creates a new InspectUi.
     * @param _shop The target shop.
     * @param _backButton The back button.
     * <p> This defines which menu the player is brought to when going back.
     */
    public InspectCanvas(final @NotNull Shop _shop, final @NotNull Div _backButton) {

        // Call superconstructor
        super(_shop, 1, ShopFancyTextElm.LINE_H, SQUARE_BUTTON_SIZE);
        Div e;


        // Add title
        e = bg.addChild(new TitleElm(_shop.getWorld(), shop.getStandaloneName()));
        e.setSize(new Vector2f(TitleElm.DEFAULT_W, ShopFancyTextElm.LINE_H));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.TOP);


        // Add id display
        e = bg.addChild(new Inspect_IdDisplay(_shop));
        e.setSize(new Vector2f(DETAILS_W, ShopFancyTextElm.LINE_H));
        e.setAlignmentX(AlignmentX.CENTER);
        e.setPosY(1f - ShopFancyTextElm.LINE_H * (1 + 1));
        ((Elm)e).getStyle(SimpleTextElmStyle.class).setTextAlignment(TextAlignment.CENTER);


        // Add mod display
        e = bg.addChild(new Inspect_ModDisplay(_shop));
        e.setSize(new Vector2f(DETAILS_W, ShopFancyTextElm.LINE_H));
        e.setAlignmentX(AlignmentX.CENTER);
        e.setPosY(1f - ShopFancyTextElm.LINE_H * (1 + 2));
        ((Elm)e).getStyle(SimpleTextElmStyle.class).setTextAlignment(TextAlignment.CENTER);


        //Add inventory view button
        e = bg.addChild(new Inspect_InventoryViewButton(_shop));
        e.setSize(new Vector2f(EditCanvas.ITEM_SELECTOR_SIZE));
        e.setPosY(EditCanvas.ITEM_SELECTOR_Y);
        e.setAlignmentX(AlignmentX.CENTER);


        // Add back button
        e = bg.addChild(_backButton);
        e.setSize(new Vector2f(SQUARE_BUTTON_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.BOTTOM);


        // Add input indicators
        e = bg.addChild(new DualInputIndicator(_shop.getWorld()));
        e.setSize(DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE);
        e.setPosY(SQUARE_BUTTON_SIZE + CanvasBorder.DEFAULT_HEIGHT);
        e.setAlignmentX(AlignmentX.CENTER);
        inputIndicator = (DualInputIndicator)e;
    }



    @Override
    public void onStockChange() {
        // Empty
    }




    @Override public @NotNull InputIndicator getLmbIndicator() { return inputIndicator.getLmbIndicator(); }
    @Override public @NotNull InputIndicator getRmbIndicator() { return inputIndicator.getRmbIndicator(); }
}