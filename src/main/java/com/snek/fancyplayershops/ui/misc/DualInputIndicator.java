package com.snek.fancyplayershops.ui.misc;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.interfaces.InputIndicatorCanvas;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.Div;

import net.minecraft.util.ClickType;








/**
 * A UI element that can display the actions mouse clicks would perform on the currently hovered element.
 */
public class DualInputIndicator extends ShopDiv implements InputIndicatorCanvas {
    public static final @NotNull Vector2f DEFAULT_DUAL_INDICATOR_SIZE  = new Vector2f(0.95f, 0.08f);
    public static final float INDICATOR_SIZE_SPACING = 0.2f;
    public static final @NotNull Vector2f INDICATOR_SIZE = new Vector2f(1, (1 - INDICATOR_SIZE_SPACING) / 2);

    private final @NotNull InputIndicator lmbIndicator;
    private final @NotNull InputIndicator rmbIndicator;




    /**
     * Creates a new DualInputIndicator.
     * @param _shop The target shop.
     */
    public DualInputIndicator(final @NotNull Shop _shop) {
        super(_shop);
        Div e;


        // Add left click display
        e = addChild(new InputIndicator(_shop, ClickType.LEFT));
        e.setSize(INDICATOR_SIZE);
        e.setAlignment(AlignmentX.CENTER, AlignmentY.TOP);
        lmbIndicator = (InputIndicator)e;


        // Add right click display
        e = addChild(new InputIndicator(_shop, ClickType.RIGHT));
        e.setSize(INDICATOR_SIZE);
        e.setAlignment(AlignmentX.CENTER, AlignmentY.BOTTOM);
        rmbIndicator = (InputIndicator)e;
    }


    @Override public @NotNull InputIndicator getLmbIndicator() { return lmbIndicator; }
    @Override public @NotNull InputIndicator getRmbIndicator() { return rmbIndicator; }
}
