package com.snek.fancyplayershops.graphics.ui.buy.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.GetShop;
import com.snek.fancyplayershops.graphics.ui.buy.BuyCanvas;
import com.snek.fancyplayershops.graphics.ui.buy.styles.Buy_ItemInspector_S;
import com.snek.fancyplayershops.graphics.ui.inspect.InspectCanvas;
import com.snek.frameworklib.graphics.functional.elements.SimpleButtonElm;
import com.snek.frameworklib.graphics.layout.Div;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;







/**
 * A button that allows the user of the shop to view details about the item.
 */
public class Buy_ItemInspector extends SimpleButtonElm {
    final Div backButton;


    /**
     * Creates a new BuyUi_ItemInspector.
     * @param _shop The target shop.
     * @param _lmbActionNameOverride The text to display for the leftt click action in the input indicator. Can be null.
     * @param _lmbActionNameOverride The text to display for the right click action in the input indicator. Can be null.
     * @param _backButton The back button. This defines which menu the player is brought to when going back.
     */
    public Buy_ItemInspector(final @NotNull ProductDisplay _shop, final @Nullable String _lmbActionNameOverride, final @Nullable String _rmbActionNameOverride, final @NotNull Div _backButton) {
        super(
            _shop.getLevel(),
            _lmbActionNameOverride != null ? _lmbActionNameOverride : (_rmbActionNameOverride != null ? "Inspect item" : null),
            _rmbActionNameOverride != null ? _rmbActionNameOverride : "Inspect item",
            0,
            new Buy_ItemInspector_S(_shop)
        );
        backButton = _backButton;
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);

        // Save amount cache
        if(backButton instanceof Buy_Sub_BackButton b) {
            b.setAmountCache(((BuyCanvas)canvas).getAmount());
        }

        // Change canvas
        final ProductDisplay shop = GetShop.get(this);
        shop.changeCanvas(new InspectCanvas(shop, backButton));
    }
}