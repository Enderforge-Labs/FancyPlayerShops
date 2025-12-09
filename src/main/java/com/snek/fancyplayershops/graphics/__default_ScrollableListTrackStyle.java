package com.snek.fancyplayershops.graphics;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.frameworklib.graphics.basic.styles.PanelElmStyle;



//FIXME move to framework lib
/**
 * The default style of the track element of {@link ScrollableList}.
 * <p>
 * This style is not directly used by the element but provides the default values instead.
 */
public class __default_ScrollableListTrackStyle extends PanelElmStyle {

    /**
     * Creates a new __default_ScrollableListTrackStyle.
     */
    public __default_ScrollableListTrackStyle() {
        super();
    }
    public @NotNull Vector3i getDefaultColor() { return new Vector3i(0, 255, 0); } //TODO choose default colors
    public          int      getDefaultAlpha() { return 255; } //TODO choose default colors
}
