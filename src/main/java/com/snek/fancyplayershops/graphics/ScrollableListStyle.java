package com.snek.fancyplayershops.graphics;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.frameworklib.data_types.containers.Flagged;
import com.snek.frameworklib.graphics.basic.styles.PanelStyle;







//FIXME move to framework lib
/**
 * The default style of the composite element {@link ScrollableList}
 */
public class ScrollableListStyle extends PanelStyle {
    private final @NotNull Flagged<@NotNull PanelStyle> trackStyle = Flagged.from(new PanelStyle());
    private final @NotNull Flagged<@NotNull PanelStyle> thumbStyle = Flagged.from(new PanelStyle());



    /**
     * Creates a new ScrollableListStyle.
     */
    public ScrollableListStyle(final boolean reset) {
        super(false);
        if(reset) resetAll();
    }

    /**
     * Creates a new ScrollableListStyle.
     */
    public ScrollableListStyle() {
        this(true);
    }


    @Override
    public int getDefaultAlpha() {
        return 0;
    }




    @Override
    public void resetAll() {
        super.resetAll();
        resetTrackStyleReference();
        resetThumbStyleReference();
    }

    @Override
    public void flagAll() {
        super.flagAll();
        editTrackStyleReference();
        editThumbStyleReference();
    }




    public @NotNull Vector3i      getDefaultTrackColor() { return trackStyle.get().getDefaultColor(); }
    public          int           getDefaultTrackAlpha() { return trackStyle.get().getDefaultAlpha(); }
    public @NotNull Vector3i      getDefaultThumbColor() { return thumbStyle.get().getDefaultColor(); }
    public          int           getDefaultThumbAlpha() { return thumbStyle.get().getDefaultAlpha(); }
    public @NotNull PanelStyle getDefaultTrackStyle() { return new __default_ScrollableListTrackStyle(); }
    public @NotNull PanelStyle getDefaultThumbStyle() { return new __default_ScrollableListThumbStyle(); }


    public void resetThumbColor() { thumbStyle.get().resetColor(); }
    public void resetThumbAlpha() { thumbStyle.get().resetAlpha(); }
    public void resetTrackColor() { trackStyle.get().resetColor(); }
    public void resetTrackAlpha() { trackStyle.get().resetAlpha(); }
    public void resetTrackStyleReference() { trackStyle.set(getDefaultTrackStyle()); }
    public void resetThumbStyleReference() { thumbStyle.set(getDefaultThumbStyle()); }


    public void setThumbColor(final @NotNull Vector3i color ) { thumbStyle.get().setColor(color); }
    public void setThumbAlpha(final          int      alpha ) { thumbStyle.get().setAlpha(alpha); }
    public void setTrackColor(final @NotNull Vector3i color ) { trackStyle.get().setColor(color); }
    public void setTrackAlpha(final          int      alpha ) { trackStyle.get().setAlpha(alpha); }
    public void setTrackStyleReference(final @NotNull PanelStyle style) { trackStyle.set(style); }
    public void setThumbStyleReference(final @NotNull PanelStyle style) { thumbStyle.set(style); }


    public final @NotNull Flagged<@NotNull Vector3i>      getFlaggedThumbColor() { return thumbStyle.get().getFlaggedColor(); }
    public final @NotNull Flagged<@NotNull Integer>       getFlaggedThumbAlpha() { return thumbStyle.get().getFlaggedAlpha(); }
    public final @NotNull Flagged<@NotNull Vector3i>      getFlaggedTrackColor() { return trackStyle.get().getFlaggedColor(); }
    public final @NotNull Flagged<@NotNull Integer>       getFlaggedTrackAlpha() { return trackStyle.get().getFlaggedAlpha(); }
    public final @NotNull Flagged<@NotNull PanelStyle> getFlaggedTrackStyle() { return trackStyle; }
    public final @NotNull Flagged<@NotNull PanelStyle> getFlaggedThumbStyle() { return thumbStyle; }


    public final @NotNull Vector3i      getThumbColor() { return thumbStyle.get().getColor(); }
    public final          int           getThumbAlpha() { return thumbStyle.get().getAlpha(); }
    public final @NotNull Vector3i      getTrackColor() { return trackStyle.get().getColor(); }
    public final          int           getTrackAlpha() { return trackStyle.get().getAlpha(); }
    public final @NotNull PanelStyle getTrackStyleReference() { return trackStyle.get(); }
    public final @NotNull PanelStyle getThumbStyleReference() { return thumbStyle.get(); }


    public @NotNull Vector3i      editThumbColor() { return thumbStyle.get().editColor(); }
    public          int           editThumbAlpha() { return thumbStyle.get().editAlpha(); }
    public @NotNull Vector3i      editTrackColor() { return trackStyle.get().editColor(); }
    public          int           editTrackAlpha() { return trackStyle.get().editAlpha(); }
    public @NotNull PanelStyle editTrackStyleReference() { return trackStyle.edit(); }
    public @NotNull PanelStyle editThumbStyleReference() { return thumbStyle.edit(); }


    public @NotNull ScrollableListStyle withThumbColor         (final @NotNull Vector3i      color) { setThumbColor         (color); return this; }
    public @NotNull ScrollableListStyle withThumbAlpha         (final          int           alpha) { setThumbAlpha         (alpha); return this; }
    public @NotNull ScrollableListStyle withTrackColor         (final @NotNull Vector3i      color) { setTrackColor         (color); return this; }
    public @NotNull ScrollableListStyle withTrackAlpha         (final          int           alpha) { setTrackAlpha         (alpha); return this; }
    public @NotNull ScrollableListStyle withTrackStyleReference(final @NotNull PanelStyle style) { setTrackStyleReference(style); return this; }
    public @NotNull ScrollableListStyle withThumbStyleReference(final @NotNull PanelStyle style) { setThumbStyleReference(style); return this; }
}
