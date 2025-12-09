package com.snek.fancyplayershops.graphics;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.frameworklib.data_types.containers.Flagged;
import com.snek.frameworklib.graphics.basic.styles.PanelElmStyle;







//FIXME move to framework lib
/**
 * The default style of the composite element {@link ScrollableList}
 */
public class ScrollableListStyle extends PanelElmStyle {
    private final @NotNull Flagged<@NotNull PanelElmStyle> trackStyle = Flagged.from(new PanelElmStyle());
    private final @NotNull Flagged<@NotNull PanelElmStyle> thumbStyle = Flagged.from(new PanelElmStyle());




    /**
     * Creates a new ScrollableListStyle.
     */
    public ScrollableListStyle() {
        super();
    }


    @Override
    public int getDefaultAlpha() {
        return 0;
    }




    @Override
    public void resetAll() {
        super.resetAll();
        resetTrackStyle();
        resetThumbStyle();
    }

    @Override
    public void flagAll() {
        super.flagAll();
        editTrackStyle();
        editThumbStyle();
    }




    public @NotNull Vector3i      getDefaultTrackColor() { return trackStyle.get().getDefaultColor(); }
    public          int           getDefaultTrackAlpha() { return trackStyle.get().getDefaultAlpha(); }
    public @NotNull Vector3i      getDefaultThumbColor() { return thumbStyle.get().getDefaultColor(); }
    public          int           getDefaultThumbAlpha() { return thumbStyle.get().getDefaultAlpha(); }
    public @NotNull PanelElmStyle getDefaultTrackStyle() { return new __default_ScrollableListTrackStyle(); }
    public @NotNull PanelElmStyle getDefaultThumbStyle() { return new __default_ScrollableListThumbStyle(); }


    public void resetThumbColor() { thumbStyle.get().resetColor(); }
    public void resetThumbAlpha() { thumbStyle.get().resetAlpha(); }
    public void resetTrackColor() { trackStyle.get().resetColor(); }
    public void resetTrackAlpha() { trackStyle.get().resetAlpha(); }
    public void resetTrackStyle() { trackStyle.get().resetAll(); }
    public void resetThumbStyle() { thumbStyle.get().resetAll(); }


    public void setThumbColor(final @NotNull Vector3i color ) { thumbStyle.get().setColor(color); }
    public void setThumbAlpha(final          int      alpha ) { thumbStyle.get().setAlpha(alpha); }
    public void setTrackColor(final @NotNull Vector3i color ) { trackStyle.get().setColor(color); }
    public void setTrackAlpha(final          int      alpha ) { trackStyle.get().setAlpha(alpha); }
    public void setTrackStyle(final @NotNull PanelElmStyle style) { trackStyle.set(style); }
    public void setThumbStyle(final @NotNull PanelElmStyle style) { thumbStyle.set(style); }


    public @NotNull Flagged<@NotNull Vector3i>      getFlaggedThumbColor() { return thumbStyle.get().getFlaggedColor(); }
    public @NotNull Flagged<@NotNull Integer>       getFlaggedThumbAlpha() { return thumbStyle.get().getFlaggedAlpha(); }
    public @NotNull Flagged<@NotNull Vector3i>      getFlaggedTrackColor() { return trackStyle.get().getFlaggedColor(); }
    public @NotNull Flagged<@NotNull Integer>       getFlaggedTrackAlpha() { return trackStyle.get().getFlaggedAlpha(); }
    public @NotNull Flagged<@NotNull PanelElmStyle> getFlaggedTrackStyle() { return trackStyle; }
    public @NotNull Flagged<@NotNull PanelElmStyle> getFlaggedThumbStyle() { return thumbStyle; }


    public @NotNull Vector3i      getThumbColor() { return thumbStyle.get().getColor(); }
    public          int           getThumbAlpha() { return thumbStyle.get().getAlpha(); }
    public @NotNull Vector3i      getTrackColor() { return trackStyle.get().getColor(); }
    public          int           getTrackAlpha() { return trackStyle.get().getAlpha(); }
    public @NotNull PanelElmStyle getTrackStyle() { return trackStyle.get(); }
    public @NotNull PanelElmStyle getThumbStyle() { return thumbStyle.get(); }


    public @NotNull Vector3i      editThumbColor() { return thumbStyle.get().editColor(); }
    public          int           editThumbAlpha() { return thumbStyle.get().editAlpha(); }
    public @NotNull Vector3i      editTrackColor() { return trackStyle.get().editColor(); }
    public          int           editTrackAlpha() { return trackStyle.get().editAlpha(); }
    public @NotNull PanelElmStyle editTrackStyle() { return trackStyle.edit(); }
    public @NotNull PanelElmStyle editThumbStyle() { return thumbStyle.edit(); }
}
