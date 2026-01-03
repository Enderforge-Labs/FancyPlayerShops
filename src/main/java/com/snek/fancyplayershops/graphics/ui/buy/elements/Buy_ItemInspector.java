package com.snek.fancyplayershops.graphics.ui.buy.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.graphics.misc.elements.Misc_BackButton;
import com.snek.fancyplayershops.graphics.ui.buy.styles.Buy_ItemInspector_S;
import com.snek.fancyplayershops.graphics.ui.inspect.InspectCanvas;
import com.snek.frameworklib.graphics.functional.elements.SimpleButtonElm;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;







/**
 * A button that allows the user of the display to view details about the item.
 */
public class Buy_ItemInspector extends SimpleButtonElm {
    final @NotNull Misc_BackButton backButton;


    /**
     * Creates a new BuyUi_ItemInspector.
     * @param display The target display.
     * @param _lmbActionNameOverride The text to display for the leftt click action in the input indicator. Can be null.
     * @param _lmbActionNameOverride The text to display for the right click action in the input indicator. Can be null.
     * @param _backButton The back button. This defines which menu the player is brought to when going back.
     */
    public Buy_ItemInspector(
        final @NotNull ProductDisplay display,
        final @Nullable String _lmbActionNameOverride, final @Nullable String _rmbActionNameOverride,
        final @NotNull Misc_BackButton _backButton
    ) {
        super(
            display.getLevel(),
            _lmbActionNameOverride != null ? _lmbActionNameOverride : (_rmbActionNameOverride != null ? "Inspect item" : null),
            _rmbActionNameOverride != null ? _rmbActionNameOverride : "Inspect item",
            0,
            new Buy_ItemInspector_S(display)
        );
        backButton = _backButton;
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);

        // Change canvas
        final ProductDisplay display = GetDisplay.get(this);
        display.changeCanvas(new InspectCanvas(display, backButton));
    }
}