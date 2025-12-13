package com.snek.fancyplayershops.graphics.ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.GetShop;
import com.snek.fancyplayershops.graphics.ui.edit.EditCanvas;
import com.snek.fancyplayershops.graphics.ui.edit.styles.Edit_SquareButton_S;
import com.snek.fancyplayershops.graphics.ui.misc.elements.SimpleShopButton;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.data_types.graphics.PolylineData;
import com.snek.frameworklib.graphics.core.Canvas;
import com.snek.frameworklib.graphics.functional.elements.SimpleButtonElm;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.composite.elements.PolylineSetElm;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class Edit_Sub_BackButton extends SimpleButtonElm {
    //FIXME make common designs public in frameworklib
    private static final @NotNull PolylineData[] design = {
        new PolylineData(
            Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA,
            Canvas.TOOLBAR_FG_WIDTH, 0.06f,
            new Vector2f(0.6f,  0.3f),
            new Vector2f(1.0f,  0.3f),
            new Vector2f(1.0f,  0.7f),
            new Vector2f(0.05f, 0.7f)
        ),
        new PolylineData(
            Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA,
            Canvas.TOOLBAR_FG_WIDTH, 0.06f,
            new Vector2f(0.2f, 0.9f),
            new Vector2f(0.0f, 0.7f),
            new Vector2f(0.2f, 0.5f)
        )
    };




    public Edit_Sub_BackButton(final @NotNull Shop _shop) {
        super(_shop.getWorld(), null, "Go back", 1,  new Edit_SquareButton_S(_shop));

        // Create design
        final Div e = addChild(new PolylineSetElm(_shop.getWorld(), design));
        e.setSize(new Vector2f(Canvas.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        super.onClick(player, click);

        // Change canvas
        final Shop shop = GetShop.get(this);
        shop.changeCanvas(new EditCanvas(shop));
    }
}
