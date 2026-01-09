package com.snek.fancyplayershops.graphics;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.frameworklib.graphics.basic.styles.PanelStyle;



//FIXME move to framework lib
/**
 * The default style of the track element of {@link ScrollableList}.
 * <p>
 * This style is not directly used by the element but provides the default values instead.
 */
public class __default_ScrollableListTrackStyle extends PanelStyle {

    /**
     * Creates a new __default_ScrollableListTrackStyle.
     */
    public __default_ScrollableListTrackStyle() {
        super();
    }


    @Override
    public @NotNull Vector3i getDefaultColor() {
        return new Vector3i(120);
    }

    @Override
    public int getDefaultAlpha() {
        return 255;
    }
}
