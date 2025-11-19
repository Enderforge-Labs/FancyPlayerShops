package com.snek.fancyplayershops.graphics.ui.edit.elements;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.edit.styles.EditUi_SquareButton_S;
import com.snek.fancyplayershops.graphics.ui.misc.elements.SimpleShopButton;
import com.snek.fancyplayershops.graphics.ui.transfer.elements.TransferUi;
import com.snek.frameworklib.data_types.ui.AlignmentX;
import com.snek.frameworklib.data_types.ui.AlignmentY;
import com.snek.frameworklib.data_types.ui.PolylineData;
import com.snek.frameworklib.graphics.core.Canvas;
import com.snek.frameworklib.graphics.core.Div;
import com.snek.frameworklib.graphics.composite.elements.PolylineSetElm;
import com.snek.frameworklib.utils.SpaceUtils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class EditUi_TransferButton extends SimpleShopButton {
    private static final @NotNull List<@NotNull Vector2f> design0 = new ArrayList<>();
    static {
        for(int i = 0; i < 8; ++i) {
            design0.add(SpaceUtils.rotateVec2(new Vector2f(0, 0.2f), (float)Math.toRadians(45) * (i + 0.5f)).add(0.5f, 0.85f));
        }
    }
    private static final @NotNull PolylineData[] design = new PolylineData[] {
        new PolylineData(
            Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA,
            Canvas.TOOLBAR_FG_WIDTH, 0.03f,
            new Vector2f(0.0f, 0.0f),
            new Vector2f(0.0f, 0.2f),
            new Vector2f(0.2f, 0.4f),
            new Vector2f(0.8f, 0.4f),
            new Vector2f(1.0f, 0.2f),
            new Vector2f(1.0f, 0.0f)
        ),
        new PolylineData(
            Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA,
            Canvas.TOOLBAR_FG_WIDTH, 0.03f,
            design0.get(0),
            design0.get(1),
            design0.get(2),
            design0.get(3),
            design0.get(4),
            design0.get(5),
            design0.get(6),
            design0.get(7),
            design0.get(0)
        )
    };




    public EditUi_TransferButton(final @NotNull Shop _shop) {
        super(_shop, null, "Transfer ownership", 1,  new EditUi_SquareButton_S(_shop));

        // Create design
        final Div e = addChild(new PolylineSetElm(_shop.getWorld(), design));
        e.setSize(new Vector2f(Canvas.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        shop.changeCanvas(new TransferUi(shop));
    }
}
