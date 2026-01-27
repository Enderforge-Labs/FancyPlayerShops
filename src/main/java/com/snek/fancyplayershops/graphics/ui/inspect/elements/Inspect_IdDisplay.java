package com.snek.fancyplayershops.graphics.ui.inspect.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.frameworklib.data_types.graphics.TextOverflowBehaviour;
import com.snek.frameworklib.graphics.basic.elements.TextElm;
import com.snek.frameworklib.graphics.basic.styles.TextStyle;
import com.snek.frameworklib.utils.MinecraftUtils;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.world.item.Items;








/**
 * A UI element that shows the ID of the item.
 */
public class Inspect_IdDisplay extends TextElm {


    /**
     * Creates a new InspectUi_IdDisplay.
     * @param display The target product display.
     */
    public Inspect_IdDisplay(@NotNull final ProductDisplay display) {
        super(display.getLevel(), new TextStyle()
            .withTextOverflowBehaviour(TextOverflowBehaviour.SCROLL)
        );
    }


    @Override
    public void spawn(@NotNull final Vector3d pos, final boolean animate) {
        updateDisplay();
        super.spawn(pos, animate);
    }


    /**
     * Updates the displayed value.
     */
    public void updateDisplay() {

        final ProductDisplay display = GetDisplay.get(this);
        getStyle(TextStyle.class).setText(new Txt()
            .cat(new Txt("ID: ").lightGray())
            .cat(new Txt(
                display.getItem().is(Items.AIR) ? "-" :
                MinecraftUtils.getItemKey(display.getItem()).getPath()
            ).white())
        .get());

        // Flush style
        flushStyle();
    }
}
