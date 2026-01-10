package com.snek.fancyplayershops.graphics.ui.inspect;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.misc.elements.Misc_BackButton;
import com.snek.fancyplayershops.graphics.ui.core.elements.ProductCanvasBase;
import com.snek.fancyplayershops.graphics.ui.edit.EditCanvas;
import com.snek.fancyplayershops.graphics.ui.inspect.elements.Inspect_IdDisplay;
import com.snek.fancyplayershops.graphics.ui.inspect.elements.Inspect_InventoryViewButton;
import com.snek.fancyplayershops.graphics.ui.inspect.elements.Inspect_ModDisplay;

import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.interfaces.InputIndicatorCanvas;
import com.snek.frameworklib.graphics.composite.elements.DualInputIndicator;
import com.snek.frameworklib.graphics.composite.elements.InputIndicator;
import com.snek.frameworklib.graphics.core.Canvas;








/**
 * A UI that allows the user of a product display to view details about the item.
 */
public class InspectCanvas extends ProductCanvasBase implements InputIndicatorCanvas {
    public static final float DETAILS_W = 0.9f;                 // The total width of the main displays
    public static final float NAMES_VALUES_WIDTH_RATIO = 0.35f; // The ration between the width of the names and the width of the values


    private final @NotNull DualInputIndicator inputIndicator;








    /**
     * Creates a new InspectUi.
     * @param display The target product display.
     * @param _backButton The back button.
     * <p> This defines which menu the player is brought to when going back.
     */
    public InspectCanvas(final @NotNull ProductDisplay display, final @NotNull Misc_BackButton _backButton) {

        // Call superconstructor
        super(display, display.getStandaloneName(), 1, TITLE_H, TOOLBAR_H);
        Div e;


        // Add id display
        e = bg.addChild(new Inspect_IdDisplay(display));
        e.setSize(new Vector2f(DETAILS_W, TITLE_H));
        e.setAlignmentX(AlignmentX.CENTER);
        e.setPosY(1f - TITLE_H * (1 + 1));


        // Add mod display
        e = bg.addChild(new Inspect_ModDisplay(display));
        e.setSize(new Vector2f(DETAILS_W, TITLE_H));
        e.setAlignmentX(AlignmentX.CENTER);
        e.setPosY(1f - TITLE_H * (1 + 2));


        //Add inventory view button
        e = bg.addChild(new Inspect_InventoryViewButton(display));
        e.setSize(new Vector2f(EditCanvas.ITEM_SELECTOR_SIZE));
        e.setPosY(EditCanvas.ITEM_SELECTOR_Y);
        e.setAlignmentX(AlignmentX.CENTER);


        // Add buttons
        setToolbarButtons(new Div[] {
            _backButton
        });


        // Add input indicators
        e = bg.addChild(new DualInputIndicator(display.getLevel()));
        e.setSize(DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE);
        e.setPosY(TOOLBAR_H + Canvas.DEFAULT_BORDER_H);
        e.setAlignmentX(AlignmentX.CENTER);
        inputIndicator = (DualInputIndicator)e;
    }




    @Override public @NotNull InputIndicator getLmbIndicator() { return inputIndicator.getLmbIndicator(); }
    @Override public @NotNull InputIndicator getRmbIndicator() { return inputIndicator.getRmbIndicator(); }
    @Override public @Nullable Div getDisclaimerElm() { return null; }
}