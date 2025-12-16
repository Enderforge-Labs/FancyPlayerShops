package com.snek.fancyplayershops.graphics.ui.buy.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.GetShop;
import com.snek.fancyplayershops.graphics.ui.buy.BuyCanvas;
import com.snek.fancyplayershops.graphics.ui.edit.styles.Edit_SquareButton_S;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.graphics.core.Canvas;
import com.snek.frameworklib.graphics.designs.SymbolDesigns;
import com.snek.frameworklib.graphics.functional.elements.SimpleButtonElm;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.composite.elements.PolylineSetElm;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class Buy_Sub_BackButton extends SimpleButtonElm {
    private int amountCache = 1;
    public void setAmountCache(final int _amountCache) { amountCache = _amountCache; }




    public Buy_Sub_BackButton(final @NotNull Shop _shop) {
        super(_shop.getLevel(), null, "Go back", 1,  new Edit_SquareButton_S(_shop));

        // Create design
        final Div e = addChild(new PolylineSetElm(_shop.getLevel(), SymbolDesigns.CurvedArrowPointingLeft));
        e.setSize(new Vector2f(Canvas.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        super.onClick(player, click);

        // Change canvas
        final Shop shop = GetShop.get(this);
        final BuyCanvas ui = new BuyCanvas(shop);
        shop.changeCanvas(ui);

        // Update amount
        ui.changeAmount(amountCache);
    }
}
