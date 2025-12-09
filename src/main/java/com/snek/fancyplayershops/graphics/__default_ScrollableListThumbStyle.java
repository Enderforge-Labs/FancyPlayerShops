package com.snek.fancyplayershops.graphics;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.frameworklib.graphics.basic.styles.PanelElmStyle;



//FIXME move to framework lib
/**
 * The default style of the thumb element of {@link ScrollableList}.
 * <p>
 * This style is not directly used by the element but provides the default values instead.
 */
public class __default_ScrollableListThumbStyle extends PanelElmStyle {

    /**
     * Creates a new __default_ScrollableListThumbStyle.
     */
    public __default_ScrollableListThumbStyle() {
        super();
    }


    @Override
    public @NotNull Vector3i getDefaultColor() {
        return new Vector3i(255, 0, 0);
    } //TODO choose default colors

    @Override
    public int getDefaultAlpha() {
        return 255;
    } //TODO choose default colors
}
