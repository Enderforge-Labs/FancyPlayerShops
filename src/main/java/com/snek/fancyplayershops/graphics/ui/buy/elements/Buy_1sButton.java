package com.snek.fancyplayershops.graphics.ui.buy.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3d;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.graphics.ui.buy.BuyCanvas;
import com.snek.fancyplayershops.graphics.ui.misc.elements.ProductDIsplay_ToggleableButton;
import com.snek.fancyplayershops.graphics.ui.misc.styles.ProductDisplay_TogglableButton_S;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.graphics.designs.ItemDesigns;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.graphics.composite.elements.PolylineSetElm;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class Buy_1sButton extends ProductDIsplay_ToggleableButton {

    public Buy_1sButton(final @NotNull ProductDisplay display) {
        super(display, null, "Buy 64 items", 1);

        // Create design
        final Div e = addChild(new PolylineSetElm(display.getLevel(), ItemDesigns.CoinPile));
        e.setSize(new Vector2f(FancyPlayerShops.LINE_H / BuyCanvas.BUY_BUTTONS_W * FancyPlayerShops.BOTTOM_ROW_CONTENT_SIZE, FancyPlayerShops.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }


    @Override
    public void spawn(@NotNull Vector3d pos, boolean animate) {
        updateDisplay();
        super.spawn(pos, animate);
    }


    public void updateDisplay() {
        getStyle(ProductDisplay_TogglableButton_S.class).setText(new Txt().get());
        flushStyle();
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);

        // Play sound and buy items
        final ProductDisplay display = GetDisplay.get(this);
        if(isActive()) Clickable.playSound(player);
        if(player.getUUID().equals(display.getOwnerUuid())) {
            display.retrieveItem(player, 64, true);
        }
        else {
            display.buyItem(player, 64, true);
        }
    }
}
