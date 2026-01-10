package com.snek.fancyplayershops.graphics.ui.change_shop;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.misc.elements.Misc_BackButton;
import com.snek.fancyplayershops.graphics.ui.change_shop.elements.ChangeShop_ConfirmButton;
import com.snek.fancyplayershops.graphics.ui.change_shop.elements.ChangeShop_NameInput;
import com.snek.fancyplayershops.graphics.ui.core.elements.ProductCanvasBase;
import com.snek.fancyplayershops.graphics.ui.edit.EditCanvas;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.interfaces.InputIndicatorCanvas;
import com.snek.frameworklib.graphics.basic.elements.TextElm;
import com.snek.frameworklib.graphics.basic.styles.TextStyle;
import com.snek.frameworklib.graphics.composite.elements.DualInputIndicator;
import com.snek.frameworklib.graphics.composite.elements.InputIndicator;
import com.snek.frameworklib.graphics.core.Canvas;
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
        super(display, culateTitle(display), 1f, TITLE_H, TOOLBAR_H);
        newShopName = display.getShop().getDisplayName(); //FIXME strip formatting codes before comparing
        Div e;


        // Add player name input
        e = bg.addChild(new TextElm(display.getLevel(), new TextStyle()
            .withText(new Txt("Shop name:").get())
        ));
        e.setSize(new Vector2f(1f, TITLE_H));
        e.setPosY(1f - TITLE_H * 2f);
        e.setAlignmentX(AlignmentX.CENTER);

        e = bg.addChild(new ChangeShop_NameInput(display, this));
        e.setSize(new Vector2f(1, TITLE_H));
        e.setPosY(1f - TITLE_H * 3f);
        e.setAlignmentX(AlignmentX.CENTER);


        // Add confirm button
        e = bg.addChild(new ChangeShop_ConfirmButton(display, this));
        e.setSize(new Vector2f(0.5f, TITLE_H));
        e.setPosY(CONFIRM_BUTTON_Y);
        e.setAlignmentX(AlignmentX.CENTER);
        confirmButton = (ChangeShop_ConfirmButton)e;


        // Add input indicators
        e = bg.addChild(new DualInputIndicator(display.getLevel()));
        e.setSize(DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE);
        e.setPosY(TOOLBAR_H + Canvas.DEFAULT_BORDER_H);
        e.setAlignmentX(AlignmentX.CENTER);
        inputIndicator = (DualInputIndicator)e;



        // Add buttons
        setToolbarButtons(new Div[] {
            new Misc_BackButton(context, () -> context.changeCanvas(new EditCanvas(display)))
        });


        //FIXME add item inspector element


        // Force button color change
        confirmButton.updateColor(false);
    }


//TODO add the current shop to this canvas




    /**
     * Checks if the new shop can be found or created and updates the stored shop name value.
     * <p> Updates the confirmation button's color and sends feedback messages to the player.
     * @param s The display name of the new shop.
     */
    public void attemptSetNewShop(final @NotNull String s) {
        final ServerPlayer player = (ServerPlayer)canvas.getContext().getPlayer();
        final char c = s.charAt(0);
        if(c == '.' || c == ' ' || c == ',') {
            player.displayClientMessage(new Txt("Shop names can't start with \"" + c + "\"!").red().bold().get(), true);
            failNameValidation();
        }
        else if(Character.isDigit(c)) {
            player.displayClientMessage(new Txt("Shop names can't start with a number!").red().bold().get(), true);
            failNameValidation();
        }
        else {
            newShopName = s;
            confirmButton.updateColor(true);
        }
    }
    private void failNameValidation() {
        newShopName = display.getShop().getDisplayName();
        confirmButton.updateColor(false);
    }




    public static @NotNull Component culateTitle(final @NotNull ProductDisplay display) {
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
    @Override public @Nullable Div getDisclaimerElm() { return null; }
}
