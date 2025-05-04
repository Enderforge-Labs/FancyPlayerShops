package com.snek.fancyplayershops.ui.edit;

import java.util.ArrayList;
import java.util.List;

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
import com.snek.framework.utils.SpaceUtils;
import com.snek.framework.utils.Txt;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;








public class EditUi_MoveButton extends ShopButton {


    public EditUi_MoveButton(final @NotNull Shop _shop){
        super(_shop, null, "Move shop", 1,  new EditUi_SquareButton_S());


        // Define the lines
        final List<PolylineData> polylines = new ArrayList<>();
        for(int i = 0; i < 4; ++i) {
            polylines.add(new PolylineData(new Vector3i(255, 255, 255), 255, 0.15f, 0.03f,
                SpaceUtils.rotateVec2(new Vector2f(-0.2f, 0.4f), (float)Math.toRadians(90) * i).add(0.5f, 0.5f),
                SpaceUtils.rotateVec2(new Vector2f(+0.0f, 0.5f), (float)Math.toRadians(90) * i).add(0.5f, 0.5f),
                SpaceUtils.rotateVec2(new Vector2f(+0.2f, 0.4f), (float)Math.toRadians(90) * i).add(0.5f, 0.5f)
            ));
        }


        // Create line set element
        final Div e = addChild(new PolylineSetElm(_shop.getWorld(),
            polylines.get(0),
            polylines.get(1),
            polylines.get(2),
            polylines.get(3)
        ));
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
            player.sendMessage(new Txt("SHOP MOVED").get());
        }
        return r;
    }
}
