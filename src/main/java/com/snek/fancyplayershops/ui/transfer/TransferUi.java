package com.snek.fancyplayershops.ui.transfer;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.ShopCanvas;
import com.snek.fancyplayershops.ui.edit.EditUi;
import com.snek.fancyplayershops.ui.edit.EditUi_DeleteButton;
import com.snek.fancyplayershops.ui.edit.EditUi_GraphButton;
import com.snek.fancyplayershops.ui.edit.EditUi_MoveButton;
import com.snek.fancyplayershops.ui.edit.EditUi_OpenBuyMenuButton;
import com.snek.fancyplayershops.ui.misc.DualInputIndicator;
import com.snek.fancyplayershops.ui.misc.InputIndicator;
import com.snek.fancyplayershops.ui.misc.ShopFancyTextElm;
import com.snek.fancyplayershops.ui.misc.ShopUiBorder;
import com.snek.fancyplayershops.ui.misc.interfaces.InputIndicatorCanvas;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.elements.Elm;








/**
 * A UI that allows the owner of the shop to edit it.
 */
public class TransferUi extends ShopCanvas implements InputIndicatorCanvas {
    private final @NotNull Elm title;
    private final @NotNull DualInputIndicator inputIndicator;
    public @NotNull Elm getTitle() { return title; }


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
        title = (Elm)e;


        // Add player name input
        e = bg.addChild(new TransferUi_NameInput(_shop));
        e.setSize(new Vector2f(0.75f, ShopFancyTextElm.LINE_H));
        e.setPosY(1f - ShopFancyTextElm.LINE_H * 2f);
        e.setAlignmentX(AlignmentX.LEFT);


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


        // Add buttons
        e = bg.addChild(new EditUi_MoveButton(_shop));
        e.setSize(new Vector2f(SQUARE_BUTTON_SIZE));
        e.setPos(new Vector2f(EditUi.BOTTOM_ROW_SHIFT * -2f, 0));

        e = bg.addChild(new EditUi_GraphButton(_shop));
        e.setSize(new Vector2f(SQUARE_BUTTON_SIZE));
        e.setPos(new Vector2f(EditUi.BOTTOM_ROW_SHIFT * -1f, 0));

        e = bg.addChild(new EditUi_OpenBuyMenuButton(_shop));
        e.setSize(new Vector2f(SQUARE_BUTTON_SIZE));
        e.setPos(new Vector2f(EditUi.BOTTOM_ROW_SHIFT * 0f, 0));

        e = bg.addChild(new TransferUi_BackButton(_shop));
        e.setSize(new Vector2f(SQUARE_BUTTON_SIZE));
        e.setPos(new Vector2f(EditUi.BOTTOM_ROW_SHIFT * 1f, 0));

        e = bg.addChild(new EditUi_DeleteButton(_shop));
        e.setSize(new Vector2f(SQUARE_BUTTON_SIZE));
        e.setPos(new Vector2f(EditUi.BOTTOM_ROW_SHIFT * 2f, 0));
    }




    @Override public @NotNull InputIndicator getLmbIndicator() { return inputIndicator.getLmbIndicator(); }
    @Override public @NotNull InputIndicator getRmbIndicator() { return inputIndicator.getRmbIndicator(); }
}
