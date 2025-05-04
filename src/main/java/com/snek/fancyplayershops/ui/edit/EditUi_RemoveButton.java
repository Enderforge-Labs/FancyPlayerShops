package com.snek.fancyplayershops.ui.edit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.edit.styles.EditUi_SquareButton_S;
import com.snek.fancyplayershops.ui.misc.ShopButton;
import com.snek.fancyplayershops.ui.misc.styles.ShopButton_S;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.composite.PolylineData;
import com.snek.framework.ui.composite.PolylineSetElm;
import com.snek.framework.ui.elements.Elm;

import net.minecraft.text.Text;








public class EditUi_RemoveButton extends ShopButton {


    public EditUi_RemoveButton(final @NotNull Shop _shop, final @NotNull EditUi_SquareButton_S _style){
        super(_shop, null, "Remove shop", 1, _style);

        final Div e = addChild(new PolylineSetElm(_shop.getWorld(),
            new PolylineData(new Vector3i(255, 255, 255), 255, 0.1f,
                new Vector2f(0.2f, 1f),
                new Vector2f(0.3f, 0f),
                new Vector2f(0.7f, 0f),
                new Vector2f(0.8f, 1f)
            )
        ));
        // e.setSize(new Vector2f(0.8f, 0.8f)); //TODO
        // e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }

    public EditUi_RemoveButton(final @NotNull Shop _shop){
        this(_shop, new EditUi_SquareButton_S());
    }




    @Override
    public void updateDisplay(final @Nullable Text textOverride) {
        // Empty
    }
}
