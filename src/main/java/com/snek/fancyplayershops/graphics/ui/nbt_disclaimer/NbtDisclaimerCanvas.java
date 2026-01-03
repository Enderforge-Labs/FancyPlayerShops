package com.snek.fancyplayershops.graphics.ui.nbt_disclaimer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.misc.elements.Misc_TitleElm;
import com.snek.fancyplayershops.graphics.ui.core.elements.ProductCanvasBase;
import com.snek.fancyplayershops.graphics.ui.nbt_disclaimer.elements.styles.NbtDisclaimer_Text_S;
import com.snek.frameworklib.graphics.core.elements.CanvasBorder;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.interfaces.InputIndicatorCanvas;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.composite.elements.DualInputIndicator;
import com.snek.frameworklib.graphics.composite.elements.InputIndicator;
import com.snek.frameworklib.utils.Txt;








/**
 * A UI that allows the owner of the product display to transfer it to another player.
 */
public class NbtDisclaimerCanvas extends ProductCanvasBase implements InputIndicatorCanvas {
    private final @NotNull DualInputIndicator inputIndicator;




    /**
     * Creates a new NbtDisclaimerCanvas.
     * @param display The target product display.
     * @param _backButton The back button. This defines which menu the player is brought to when going back.
     */
    public NbtDisclaimerCanvas(final @NotNull ProductDisplay display, final @NotNull Div _backButton) {

        // Call superconstructor
        super(display, 1f, FancyPlayerShops.LINE_H, FancyPlayerShops.SQUARE_BUTTON_SIZE);
        Div e;


        // Add title
        e = bg.addChild(new Misc_TitleElm(display.getLevel(), new Txt("Mixed NBTs").get()));
        e.setPosY(1f - FancyPlayerShops.LINE_H * 1f);
        e.setSize(new Vector2f(Misc_TitleElm.DEFAULT_W, FancyPlayerShops.LINE_H));
        e.setAlignmentX(AlignmentX.CENTER);


        // Add message
        e = bg.addChild(new SimpleTextElm(display.getLevel(), new NbtDisclaimer_Text_S()));
        e.setSize(new Vector2f(1f, FancyPlayerShops.LINE_H));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);


        // Add input indicators
        e = bg.addChild(new DualInputIndicator(display.getLevel()));
        e.setSize(DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE);
        e.setPosY(FancyPlayerShops.SQUARE_BUTTON_SIZE + CanvasBorder.DEFAULT_HEIGHT);
        e.setAlignmentX(AlignmentX.CENTER);
        inputIndicator = (DualInputIndicator)e;


        //FIXME add item inspector element


        // Add buttons
        final Div[] buttons = new Div[] {
            _backButton
        };
        for(int i = 0; i < buttons.length; ++i) {
            e = bg.addChild(buttons[i]);
            e.setSize(new Vector2f(FancyPlayerShops.SQUARE_BUTTON_SIZE));
            e.setPosX(FancyPlayerShops.BOTTOM_ROW_SHIFT * (i - (int)(buttons.length / 2f + 0.0001f)));
            e.setAlignmentY(AlignmentY.BOTTOM);
        } //TODO unify duplicate code
    }




    @Override
    public void onStockChange() {
        // Empty
    }




    @Override public @NotNull InputIndicator getLmbIndicator() { return inputIndicator.getLmbIndicator(); }
    @Override public @NotNull InputIndicator getRmbIndicator() { return inputIndicator.getRmbIndicator(); }
    @Override public @Nullable Div getDisclaimerElm() { return null; }
}
