package com.snek.fancyplayershops.graphics.ui.nbt_disclaimer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.misc.styles.TextStyle_Small;
import com.snek.fancyplayershops.graphics.ui.core.elements.ProductCanvasBase;
import com.snek.frameworklib.graphics.core.elements.CanvasBorder;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.graphics.interfaces.InputIndicatorCanvas;
import com.snek.frameworklib.graphics.basic.elements.TextElm;
import com.snek.frameworklib.graphics.composite.elements.DualInputIndicator;
import com.snek.frameworklib.graphics.composite.elements.InputIndicator;








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
        super(display, "Mixed NBTs", 1f, TITLE_H, TOOLBAR_H);
        Div e;


        // Add message
        e = bg.addChild(new TextElm(display.getLevel(), new TextStyle_Small()
            .withText(new Txt(
                "\n" +
                "This display contains products with mixed NBTs.\n" +
                "\n" +
                "\n" + // Leave some space for the item display
                "\n" + //
                "\n" + //
                "\n" + //
                "\n" + //
                "\n" + //
                "\n" + // Leave some space for the item display
                "\n" +
                "You might receive any variant of the\n" +
                "item shown here. This includes energy level,\n" +
                "upgrades, enchantments, and other data.\n" +
                "Buy at your own risk!"
            ).white().get())
        ));
        e.setSize(new Vector2f(1f, TITLE_H));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);


        // Add input indicators
        e = bg.addChild(new DualInputIndicator(display.getLevel()));
        e.setSize(DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE);
        e.setPosY(TOOLBAR_H + CanvasBorder.DEFAULT_HEIGHT);
        e.setAlignmentX(AlignmentX.CENTER);
        inputIndicator = (DualInputIndicator)e;


        //FIXME add item inspector element


        // Add buttons
        setToolbarButtons(new Div[] {
            _backButton
        });
    }




    @Override public @NotNull InputIndicator getLmbIndicator() { return inputIndicator.getLmbIndicator(); }
    @Override public @NotNull InputIndicator getRmbIndicator() { return inputIndicator.getRmbIndicator(); }
    @Override public @Nullable Div getDisclaimerElm() { return null; }
}
