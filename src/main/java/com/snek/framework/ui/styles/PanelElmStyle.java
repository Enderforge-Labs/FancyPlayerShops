package com.snek.framework.ui.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.utils.Easings;








public class PanelElmStyle extends ElmStyle {


    private Flagged<Vector4i> color = null;




    /**
     * Creates a new default PanelElmStyle.
     */
    public PanelElmStyle() {
        super();
    }


    @Override
    public void resetAll(){
        resetColor();
        super.resetAll();
    }




    @Override
    public Transform getDefaultTransform(){
        return new Transform()
        ;
    }


    @Override
    public Animation getDefaultPrimerAnimation() {
        return new Animation(
            new Transition(ElmStyle.S_TIME, Easings.sineOut)
            .targetBackground(new Vector4i(getDefaultColor().mul(new Vector4i(0, 1, 1, 1))))
            .targetOpacity(0)
        );
    }


    @Override
    public Animation getDefaultSpawnAnimation() {
        return new Animation(
            new Transition(ElmStyle.S_TIME, Easings.sineOut)
            .targetBackground(getDefaultColor())
            .targetOpacity(255)
        );
    }


    @Override
    public Animation getDefaultDespawnAnimation() {
        return new Animation(
            new Transition(ElmStyle.D_TIME, Easings.sineOut)
            .targetBackground(new Vector4i(getDefaultColor().mul(new Vector4i(0, 1, 1, 1))))
            .targetOpacity(0)
        );
    }




    public @NotNull Vector4i getDefaultColor () { return new Vector4i(180, 12, 20, 20); }
    public void resetColor () { color = Flagged.from(getDefaultColor()); }
    public void setColor (@NotNull Vector4i _color ) { color.set(_color); }
    public @NotNull Flagged<Vector4i> getFlaggedColor () { return color; }
    public @NotNull Vector4i getColor () { return color.get(); }
    public @NotNull Vector4i editColor () { return color.edit(); }
}
