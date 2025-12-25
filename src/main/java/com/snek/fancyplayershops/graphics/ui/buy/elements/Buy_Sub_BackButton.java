package com.snek.fancyplayershops.graphics.ui.buy.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.graphics.ui.buy.BuyCanvas;
import com.snek.fancyplayershops.graphics.ui.edit.styles.Edit_SquareButton_S;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.graphics.designs.SymbolDesigns;
import com.snek.frameworklib.graphics.functional.elements.SimpleButtonElm;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.composite.elements.PolylineSetElm;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class Buy_Sub_BackButton extends SimpleButtonElm {
    private int amountCache = 1;
    public void setAmountCache(final int _amountCache) { amountCache = _amountCache; }




    public Buy_Sub_BackButton(final @NotNull ProductDisplay display) {
        super(display.getLevel(), null, "Go back", 1,  new Edit_SquareButton_S(display));

        // Create design
        final Div e = addChild(new PolylineSetElm(display.getLevel(), SymbolDesigns.CurvedArrowPointingLeft));
        e.setSize(new Vector2f(FancyPlayerShops.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);

        // Change canvas
        final ProductDisplay display = GetDisplay.get(this);
        final BuyCanvas ui = new BuyCanvas(display);
        display.changeCanvas(ui);

        // Update amount
        ui.changeAmount(amountCache);
    }
}
