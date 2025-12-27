package com.snek.fancyplayershops.graphics.ui.change_shop;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.misc.elements.TitleElm;
import com.snek.fancyplayershops.graphics.ui.change_shop.elements.ChangeShop_ConfirmButton;
import com.snek.fancyplayershops.graphics.ui.change_shop.elements.ChangeShop_NameInput;
import com.snek.fancyplayershops.graphics.ui.core.elements.ProductCanvasBase;
import com.snek.fancyplayershops.graphics.ui.edit.elements.Edit_Sub_BackButton;
import com.snek.frameworklib.graphics.core.elements.CanvasBorder;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.core.elements.Elm;
import com.snek.frameworklib.graphics.interfaces.InputIndicatorCanvas;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.graphics.composite.elements.DualInputIndicator;
import com.snek.frameworklib.graphics.composite.elements.InputIndicator;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;








/**
 * A UI that allows the owner of the product display to change its shop.
 */
public class ChangeShopCanvas extends ProductCanvasBase implements InputIndicatorCanvas {
    public static final float CONFIRM_BUTTON_Y = 0.25f;
    private final @NotNull DualInputIndicator inputIndicator;


    // Instance data
    private final @NotNull ChangeShop_ConfirmButton confirmButton;
    private @NotNull String newShopName;
    public @NotNull String getNewShopName() { return newShopName; }



//TODO add ColorCodedString class in frameworklib datatypes
//TODO can be converted to a Component and strip format codes



    /**
     * Creates a new ChangeShopUi.
     * @param display The target product display.
     */
    public ChangeShopCanvas(final @NotNull ProductDisplay display) {

        // Call superconstructor
        super(display, 1f, FancyPlayerShops.LINE_H, FancyPlayerShops.SQUARE_BUTTON_SIZE);
        newShopName = display.getShop().getDisplayName(); //FIXME strip formatting codes before comparing
        Div e;


        // Add title
        e = bg.addChild(new TitleElm(display.getLevel(), recalculateTitle()));
        e.setPosY(1f - FancyPlayerShops.LINE_H * 1f);
        e.setSize(new Vector2f(TitleElm.DEFAULT_W, FancyPlayerShops.LINE_H));
        e.setAlignmentX(AlignmentX.CENTER);


        // Add player name input
        e = bg.addChild(new SimpleTextElm(display.getLevel()));
        e.setSize(new Vector2f(1f, FancyPlayerShops.LINE_H));
        e.setPosY(1f - FancyPlayerShops.LINE_H * 2f);
        e.setAlignmentX(AlignmentX.CENTER);
        ((Elm)e).getStyle(SimpleTextElmStyle.class).setText(new Txt("Shop name:").get());

        e = bg.addChild(new ChangeShop_NameInput(display, this));
        e.setSize(new Vector2f(1, FancyPlayerShops.LINE_H));
        e.setPosY(1f - FancyPlayerShops.LINE_H * 3f);
        e.setAlignmentX(AlignmentX.CENTER);


        // Add confirm button
        e = bg.addChild(new ChangeShop_ConfirmButton(display, this));
        e.setSize(new Vector2f(0.5f, FancyPlayerShops.LINE_H));
        e.setPosY(CONFIRM_BUTTON_Y);
        e.setAlignmentX(AlignmentX.CENTER);
        confirmButton = (ChangeShop_ConfirmButton)e;


        // Add input indicators
        e = bg.addChild(new DualInputIndicator(display.getLevel()));
        e.setSize(DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE);
        e.setPosY(FancyPlayerShops.SQUARE_BUTTON_SIZE + CanvasBorder.DEFAULT_HEIGHT);
        e.setAlignmentX(AlignmentX.CENTER);
        inputIndicator = (DualInputIndicator)e;


        // Add back button
        e = bg.addChild(new Edit_Sub_BackButton(display));
        e.setSize(new Vector2f(FancyPlayerShops.SQUARE_BUTTON_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.BOTTOM);


        // Force button color change
        confirmButton.updateColor(false);
    }




    /**
     * Checks if the new shop can be found or created and updates the stored shop name value. //FIXME check if the shop still exists when clicking (check isDeleted)
     * <p> Updates the confirmation button's color and sends feedback messages to the player.
     * @param s The display name of the new shop.
     */
    public void attemptSetNewShop(final @NotNull String s) {
        final ServerPlayer player = (ServerPlayer)canvas.getContext().getPlayer();
        final char c = s.charAt(0);
        if(c == '.' || c == ' ' || c == ',') {
            player.displayClientMessage(new Txt("Shop names can't start with \"" + c + "\"!").red().bold().get(), true);
            confirmButton.updateColor(false);
        }
        else if(Character.isDigit(c)) {
            player.displayClientMessage(new Txt("Shop names can't start with a number!").red().bold().get(), true);
            confirmButton.updateColor(false);
        }
        else {
            newShopName = s;
            confirmButton.updateColor(true);
        }
    }




    @Override
    public void onStockChange() {
        // Empty
    }



    public @NotNull Component recalculateTitle() {
        if(display.getItem().is(Items.AIR)) {
            return new Txt("Moving an empty product display to another shop").white().get();
        }
        else {
            return new Txt()
                .cat(new Txt("Moving \"").white())
                .cat(display.getStandaloneName())
                .cat(new Txt("\" to another shop").white())
            .get();
        }
    }




    @Override public @NotNull InputIndicator getLmbIndicator() { return inputIndicator.getLmbIndicator(); }
    @Override public @NotNull InputIndicator getRmbIndicator() { return inputIndicator.getRmbIndicator(); }
}
