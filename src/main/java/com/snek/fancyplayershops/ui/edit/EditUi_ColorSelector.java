package com.snek.fancyplayershops.ui.edit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.edit.styles.EditUi_ColorSelector_S;
import com.snek.fancyplayershops.ui.misc.ShopButton;
import com.snek.fancyplayershops.ui.misc.styles.UiCanvasBackground_S;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.elements.Elm;
import com.snek.framework.ui.elements.FancyTextElm;
import com.snek.framework.ui.elements.PanelElm;
import com.snek.framework.ui.elements.styles.FancyTextElmStyle;
import com.snek.framework.ui.elements.styles.PanelElmStyle;
import com.snek.framework.utils.Easings;
import com.snek.framework.utils.Utils;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class EditUi_ColorSelector extends ShopButton {
    public static final float S = 0.4f;
    public static final float V = 0.8f;

    // Instance data
    private final float hue;
    public float getHue() { return hue; }
    private final @NotNull EditUi menu;




    /**
     * Creates a new EditUi_ColorSelector.
     * @param _shop The target shop.
     * @param hue The hue of the color theme.
     */
    protected EditUi_ColorSelector(final @NotNull Shop _shop, final float _hue, final @NotNull EditUi _menu) {
        super(_shop, null, "Change color theme", 1, new EditUi_ColorSelector_S(_shop));
        hue = _hue;
        menu = _menu;
        getStyle(EditUi_ColorSelector_S.class).setBgColor(Utils.HSVtoRGB(new Vector3f(hue, S, V)));
    }


    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        // Empty
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {

        // Change theme hue
        shop.setColorThemeHue(hue);
        ShopManager.saveShop(shop);


        // Reset colored backgrounds of themed elements
        final Elm bg = menu.getBg();
        bg.applyAnimation(new Transition(2, Easings.sineOut).targetBgColor(bg.getStyle(UiCanvasBackground_S.class).getDefaultColor()));
        for(final Div c : menu.getBg().getChildren()) {
            if(!(c instanceof EditUi_ColorSelector)) {
                if(c instanceof FancyTextElm e) {
                    e.applyAnimation(new Transition(2, Easings.sineOut).targetBgColor(e.getStyle(FancyTextElmStyle.class).getDefaultBgColor()));
                }
                else if(c instanceof PanelElm e) {
                    e.applyAnimation(new Transition(2, Easings.sineOut).targetBgColor(e.getStyle(PanelElmStyle.class).getDefaultColor()));
                }
            }
        }
    }
}
