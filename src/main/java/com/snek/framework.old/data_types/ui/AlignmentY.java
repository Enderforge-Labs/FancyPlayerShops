package com.snek.framework.old.data_types.ui;




/**
 * An enum that defines the vertical alignment of a UI element.
 * <p> NONE:    The element is not vertically aligned and can move freely.
 * <p> TOP:     The top edge    of the element is always aligned with the top edge    of its parent.
 * <p> CENTER:  The center      of the element is always aligned with the center      of its parent.
 * <p> BOTTOM:  The bottom edge of the element is always aligned with the bottom edge of its parent.
 */
public enum AlignmentY {
    NONE  (-1),
    TOP   (0),
    CENTER(1),
    BOTTOM(2);


    public final int value;
    AlignmentY(final int _value) {
        value = _value;
    }
}
