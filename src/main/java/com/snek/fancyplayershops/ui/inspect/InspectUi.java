package com.snek.fancyplayershops.ui.inspect;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.ShopCanvas;
import com.snek.fancyplayershops.ui.edit.EditUi;
import com.snek.fancyplayershops.ui.misc.DualInputIndicator;
import com.snek.fancyplayershops.ui.misc.InputIndicator;
import com.snek.fancyplayershops.ui.misc.ShopFancyTextElm;
import com.snek.fancyplayershops.ui.misc.ShopUiBorder;
import com.snek.fancyplayershops.ui.misc.interfaces.InputIndicatorCanvas;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.Div;








/**
 * A UI that allows the user of a shop to view details about the item.
 */
public class InspectUi extends ShopCanvas implements InputIndicatorCanvas {
    private final @NotNull DualInputIndicator inputIndicator;








    /**
     * Creates a new InspectUi.
     * @param _shop The target shop.
     * @param _backButton The back button.
     * <p> This defines which menu the player is brought to when going back.
     */
    public InspectUi(final @NotNull Shop _shop, final @NotNull Div _backButton) {

        // Call superconstructor
        super(_shop, 1, ShopFancyTextElm.LINE_H, ShopCanvas.SQUARE_BUTTON_SIZE);
        Div e;


        // Add title
        e = bg.addChild(new InspectUi_Title(_shop));
        e.setSize(new Vector2f(1f, ShopFancyTextElm.LINE_H));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.TOP);


        // Add back button
        e = bg.addChild(_backButton);
        e.setSize(new Vector2f(SQUARE_BUTTON_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.BOTTOM);


        // Add input indicators
        e = bg.addChild(new DualInputIndicator(_shop));
        e.setSize(DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE);
        e.setPosY(ShopUiBorder.DEFAULT_HEIGHT * 2);
        e.setAlignmentX(AlignmentX.CENTER);
        inputIndicator = (DualInputIndicator)e;
    }




    @Override public @NotNull InputIndicator getLmbIndicator() { return inputIndicator.getLmbIndicator(); }
    @Override public @NotNull InputIndicator getRmbIndicator() { return inputIndicator.getRmbIndicator(); }
}