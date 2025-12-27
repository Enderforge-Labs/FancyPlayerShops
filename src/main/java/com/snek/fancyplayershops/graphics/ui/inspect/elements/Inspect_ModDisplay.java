package com.snek.fancyplayershops.graphics.ui.inspect.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.graphics.ui.inspect.styles.Inspect_ModDisplay_S;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.utils.MinecraftUtils;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.world.item.Items;








/**
 * A UI element that shows the mod of the item.
 */
public class Inspect_ModDisplay extends SimpleTextElm {


    /**
     * Creates a new InspectUi_ModDisplay.
     * @param display The target product display.
     */
    public Inspect_ModDisplay(@NotNull ProductDisplay display) {
        super(display.getLevel(), new Inspect_ModDisplay_S());
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
            .cat(new Txt("Mod: ").lightGray())
            .cat(new Txt(
                display.getItem().is(Items.AIR) ? "-" :
                MinecraftUtils.getItemKey(display.getItem()).getNamespace()
            ).white())
        .get());

        // Flush style
        flushStyle();
    }
}
