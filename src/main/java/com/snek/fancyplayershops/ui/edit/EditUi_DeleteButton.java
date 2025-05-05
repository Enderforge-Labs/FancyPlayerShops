package com.snek.fancyplayershops.ui.edit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.edit.styles.EditUi_SquareButton_S;
import com.snek.fancyplayershops.ui.misc.ShopButton;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.composite.PolylineData;
import com.snek.framework.ui.composite.PolylineSetElm;
import com.snek.framework.utils.Txt;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;








public class EditUi_DeleteButton extends ShopButton {
    private static final @NotNull PolylineData[] design = new PolylineData[] {
        new PolylineData(new Vector3i(255, 255, 255), 255, 0.15f, 0.05f,
            new Vector2f(0.1f, 0.1f),
            new Vector2f(0.9f, 0.9f)
        ),
        new PolylineData(new Vector3i(255, 255, 255), 255, 0.15f, 0.05f,
            new Vector2f(0.1f, 0.9f),
            new Vector2f(0.9f, 0.1f)
        )
    };




    public EditUi_DeleteButton(final @NotNull Shop _shop){
        super(_shop, null, "Delete shop", 1,  new EditUi_SquareButton_S());

        // Create design
        final Div e = addChild(new PolylineSetElm(_shop.getWorld(), design));
        e.setSize(new Vector2f(EditUi.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }


    @Override
    public void updateDisplay(final @Nullable Text textOverride) {
        // Empty
    }


    @Override
    public boolean onClick(final @NotNull PlayerEntity player, final @NotNull ClickType click) {
        final boolean r = super.onClick(player, click);
        if(r) {
            player.sendMessage(new Txt("SHOP DELETED").get());
        }
        return r;
    }
}
