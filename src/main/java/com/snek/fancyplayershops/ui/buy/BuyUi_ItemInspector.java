package com.snek.fancyplayershops.ui.buy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.buy.styles.BuyUi_ItemInspector_S;
import com.snek.fancyplayershops.ui.inspect.InspectUi;
import com.snek.fancyplayershops.ui.misc.ShopButton;
import com.snek.framework.ui.Div;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








/**
 * A button that allows the user of the shop to view details about the item.
 */
public class BuyUi_ItemInspector extends ShopButton {
    final Div backButton;


    /**
     * Creates a new BuyUi_ItemInspector.
     * @param _shop The target shop.
     * @param _lmbActionNameOverride The text to display for the leftt click action in the input indicator. Can be null.
     * @param _lmbActionNameOverride The text to display for the right click action in the input indicator. Can be null.
     * @param _backButton The back button. This defines which menu the player is brought to when going back.
     */
    public BuyUi_ItemInspector(final @NotNull Shop _shop, final @Nullable String _lmbActionNameOverride, final @Nullable String _rmbActionNameOverride, final @NotNull Div _backButton) {
        super(
            _shop,
            _lmbActionNameOverride != null ? _lmbActionNameOverride : (_rmbActionNameOverride != null ? "Inspect item" : null),
            _rmbActionNameOverride != null ? _rmbActionNameOverride : "Inspect item",
            0,
            new BuyUi_ItemInspector_S(_shop)
        );
        backButton = _backButton;
    }


    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        // Empty
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        shop.changeCanvas(new InspectUi(shop, backButton));
        playButtonSound(player);
    }
}
//TODO make this a subclass of item inspector
//TODO     item inspectors show the price graph, the mod the item comes from, the name, the description
//TODO     it also lets you open an inventory to see it as a real item