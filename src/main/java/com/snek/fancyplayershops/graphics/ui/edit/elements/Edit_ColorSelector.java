package com.snek.fancyplayershops.graphics.ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.core.styles.ProductCanvasBackground_S;
import com.snek.fancyplayershops.graphics.ui.edit.EditCanvas;
import com.snek.fancyplayershops.graphics.ui.edit.styles.Edit_ColorSelector_S;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.core.elements.Elm;
import com.snek.frameworklib.graphics.functional.elements.ButtonElm;
import com.snek.frameworklib.graphics.basic.elements.PanelTextElm;
import com.snek.frameworklib.graphics.basic.elements.PanelElm;
import com.snek.frameworklib.graphics.basic.styles.PanelTextStyle;
import com.snek.frameworklib.graphics.basic.styles.PanelStyle;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class Edit_ColorSelector extends ButtonElm {
    public static final float S = 0.4f;
    public static final float V = 0.8f;

    // Instance data
    private final float hue;
    public float getHue() { return hue; }
    private final @NotNull EditCanvas menu;




    /**
     * Creates a new EditUi_ColorSelector.
     * @param display The target product display.
     * @param hue The hue of the color theme.
     */
    public Edit_ColorSelector(final @NotNull ProductDisplay display, final float _hue, final @NotNull EditCanvas _menu) {
        super(display.getLevel(), null, "Change color theme", 1, (Edit_ColorSelector_S)new Edit_ColorSelector_S(display)
            .withColor(Utils.HSVtoRGB(new Vector3f(_hue, S, V)))
        );
        hue = _hue;
        menu = _menu;
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);

        // Change theme hue
        final ProductDisplay display = GetDisplay.get(this);
        display.setColorThemeHue(hue);
        ProductDisplayManager.scheduleDisplaySave(display);


        // Reset colored backgrounds of themed elements
        final Elm bg = menu.getBg();
        bg.applyAnimation(new Transition(2, Easings.sineOut).targetBgColor(bg.getStyle(ProductCanvasBackground_S.class).getDefaultColor()), false, true);
        for(final Div c : menu.getBg().getChildren()) {
            if(!(c instanceof Edit_ColorSelector)) {
                if(c instanceof final PanelTextElm e) {
                    e.applyAnimation(new Transition(2, Easings.sineOut).targetBgColor(e.getStyle(PanelTextStyle.class).getDefaultBgColor()), false, true);
                }
                else if(c instanceof final PanelElm e) {
                    e.applyAnimation(new Transition(2, Easings.sineOut).targetBgColor(e.getStyle(PanelStyle.class).getDefaultColor()), false, true);
                }
            }
        }
    }
}
