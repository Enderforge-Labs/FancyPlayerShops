package com.snek.fancyplayershops.implementations.ui;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.framework.ui.Div;
import com.snek.framework.ui.elements.Elm;








/**
 * A generic canvas class used to create shop menus.
 */
public class ShopCanvas extends Div {
    protected final @NotNull Elm bg;
    protected final @NotNull Elm back;
    public @NotNull Elm getBackground() { return bg; }
    public @NotNull Elm getBack() { return back; }


    // Layout
    public static final int   SPAWN_SIZE_TIME            = 8;
    public static final float SQUARE_BUTTON_SIZE         = 0.12f;



    /**
     * Creates a new ShopCanvas
     * @param _bg The background element
     */
    public ShopCanvas(@NotNull Elm _bg, @NotNull Elm _back) {
        super();

        // Add front
        addChild(_bg);
        bg = _bg;

        // Add back
        addChild(_back);
        back = _back;

    }




    @Override
    public void spawn(Vector3d pos){
        super.spawn(pos);
    }


    @Override
    public void despawn(){
        super.despawn();
    }
}