package com.snek.fancyplayershops.graphics.ui.inspect.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.graphics.ui.inspect.styles.Inspect_IdDisplay_S;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;








/**
 * A UI element that shows the ID of the item.
 */
public class Inspect_IdDisplay extends SimpleTextElm {


    /**
     * Creates a new InspectUi_IdDisplay.
     * @param display The target product display.
     */
    public Inspect_IdDisplay(@NotNull ProductDisplay display) {
        super(display.getLevel(), new Inspect_IdDisplay_S());
    }


    @Override
    public void spawn(@NotNull Vector3d pos, boolean animate) {
        updateDisplay();
        super.spawn(pos, animate);
    }


    /**
     * Updates the displayed value.
     */
    public void updateDisplay() {

        final ProductDisplay display = GetDisplay.get(this);
        getStyle(SimpleTextElmStyle.class).setText(new Txt()
            .cat(new Txt("ID: ").lightGray())
            .cat(new Txt(
                display.getItem().is(Items.AIR) ? "-" :
                BuiltInRegistries.ITEM.getKey(display.getItem().getItem()).getPath()
            ).white())
        .get());

        // Flush style
        flushStyle();
    }
}
