package com.snek.fancyplayershops.ui.transfer;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.ShopCanvas;
import com.snek.fancyplayershops.ui.edit.EditUiSub_BackButton;
import com.snek.fancyplayershops.ui.misc.DualInputIndicator;
import com.snek.fancyplayershops.ui.misc.InputIndicator;
import com.snek.fancyplayershops.ui.misc.ShopFancyTextElm;
import com.snek.fancyplayershops.ui.misc.ShopUiBorder;
import com.snek.fancyplayershops.ui.misc.interfaces.InputIndicatorCanvas;
import com.snek.fancyplayershops.ui.transfer.styles.TransferUi_NameInput_S;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.TextAlignment;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.elements.Elm;
import com.snek.framework.ui.elements.TextElm;
import com.snek.framework.ui.elements.styles.TextElmStyle;
import com.snek.framework.utils.Txt;








/**
 * A UI that allows the owner of the shop to edit it.
 */
public class TransferUi extends ShopCanvas implements InputIndicatorCanvas {
    private final @NotNull DualInputIndicator inputIndicator;


    // Temporary canvas data
    private @NotNull UUID newOwnerUUID;
    public @NotNull UUID getNewOwnerUUID() { return newOwnerUUID; }
    public void setNewOwnerUUID(final @NotNull UUID _newOwnerUUID) { newOwnerUUID = _newOwnerUUID; }







    /**
     * Creates a new TransferUi.
     * @param _shop The target shop.
     */
    public TransferUi(final @NotNull Shop _shop) {

        // Call superconstructor
        super(_shop, 1f, ShopFancyTextElm.LINE_H, SQUARE_BUTTON_SIZE);
        newOwnerUUID = _shop.getOwnerUuid();
        Div e;


        // Add title
        e = bg.addChild(new TransferUi_Title(_shop));
        e.setPosY(1f - ShopFancyTextElm.LINE_H * 1f);
        e.setSizeY(ShopFancyTextElm.LINE_H);
        e.setAlignmentX(AlignmentX.CENTER);


        // Add player name input
        e = bg.addChild(new TextElm(_shop.getWorld()));
        e.setSize(new Vector2f(0.75f, ShopFancyTextElm.LINE_H));
        e.setPosY(1f - ShopFancyTextElm.LINE_H * 2f);
        e.setAlignmentX(AlignmentX.CENTER);
        ((Elm)e).getStyle(TextElmStyle.class).setTextAlignment(TextAlignment.CENTER);
        ((Elm)e).getStyle(TextElmStyle.class).setText(new Txt("New owner:").get());

        e = bg.addChild(new TransferUi_NameInput(_shop));
        e.setSize(new Vector2f(0.75f, ShopFancyTextElm.LINE_H));
        e.setPosY(1f - ShopFancyTextElm.LINE_H * 3f);
        e.setAlignmentX(AlignmentX.CENTER);
        ((Elm)e).getStyle(TransferUi_NameInput_S.class).setTextAlignment(TextAlignment.CENTER);


        // Add confirm button
        e = bg.addChild(new TransferUi_ConfirmButton(_shop));
        e.setSize(new Vector2f(0.5f, ShopFancyTextElm.LINE_H));
        e.setPosY(0.25f);
        e.setAlignmentX(AlignmentX.CENTER);


        // Add input indicators
        e = bg.addChild(new DualInputIndicator(_shop));
        e.setSize(DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE);
        e.setPosY(SQUARE_BUTTON_SIZE + ShopUiBorder.DEFAULT_HEIGHT);
        e.setAlignmentX(AlignmentX.CENTER);
        inputIndicator = (DualInputIndicator)e;


        // Add back button
        e = bg.addChild(new EditUiSub_BackButton(_shop));
        e.setSize(new Vector2f(SQUARE_BUTTON_SIZE));
        e.setAlignmentX(AlignmentX.CENTER);
        e.setPosY(0);
    }




    @Override public @NotNull InputIndicator getLmbIndicator() { return inputIndicator.getLmbIndicator(); }
    @Override public @NotNull InputIndicator getRmbIndicator() { return inputIndicator.getRmbIndicator(); }
}
