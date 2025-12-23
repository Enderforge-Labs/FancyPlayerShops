package com.snek.fancyplayershops.graphics.ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.snek.fancyplayershops.GetShop;
import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.core.styles.ShopCanvasBackground_S;
import com.snek.fancyplayershops.graphics.ui.edit.EditCanvas;
import com.snek.fancyplayershops.graphics.ui.edit.styles.Edit_ColorSelector_S;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.core.elements.Elm;
import com.snek.frameworklib.graphics.functional.elements.SimpleButtonElm;
import com.snek.frameworklib.graphics.basic.elements.FancyTextElm;
import com.snek.frameworklib.graphics.basic.elements.PanelElm;
import com.snek.frameworklib.graphics.basic.styles.FancyTextElmStyle;
import com.snek.frameworklib.graphics.basic.styles.PanelElmStyle;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class Edit_ColorSelector extends SimpleButtonElm {
    public static final float S = 0.4f;
    public static final float V = 0.8f;

    // Instance data
    private final float hue;
    public float getHue() { return hue; }
    private final @NotNull EditCanvas menu;




    /**
     * Creates a new EditUi_ColorSelector.
     * @param _shop The target shop.
     * @param hue The hue of the color theme.
     */
    public Edit_ColorSelector(final @NotNull ProductDisplay _shop, final float _hue, final @NotNull EditCanvas _menu) {
        super(_shop.getLevel(), null, "Change color theme", 1, new Edit_ColorSelector_S(_shop));
        hue = _hue;
        menu = _menu;
        getStyle(Edit_ColorSelector_S.class).setColor(Utils.HSVtoRGB(new Vector3f(hue, S, V)));
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);

        // Change theme hue
        final ProductDisplay shop = GetShop.get(this);
        shop.setColorThemeHue(hue);
        ShopManager.scheduleShopSave(shop);


        // Reset colored backgrounds of themed elements
        final Elm bg = menu.getBg();
        bg.applyAnimation(new Transition(2, Easings.sineOut).targetBgColor(bg.getStyle(ShopCanvasBackground_S.class).getDefaultColor()));
        for(final Div c : menu.getBg().getChildren()) {
            if(!(c instanceof Edit_ColorSelector)) {
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
