package com.snek.fancyplayershops.implementations.ui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;

import com.snek.fancyplayershops.Shop;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.displays.CustomItemDisplay;
import com.snek.framework.generated.FontSize;
import com.snek.framework.ui.elements.FancyTextElm;
import com.snek.framework.ui.elements.ItemElm;
import com.snek.framework.ui.elements.TextElm;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.ui.styles.FancyTextElmStyle;
import com.snek.framework.ui.styles.ItemElmStyle;
import com.snek.framework.ui.styles.TextElmStyle;
import com.snek.framework.utils.Easings;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.scheduler.Scheduler;
import com.snek.framework.utils.scheduler.TaskHandler;

import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.entity.decoration.DisplayEntity.ItemDisplayEntity;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;








/**
 * An item display that shows the item currently being sold by a shop.
 * Unconfigured shops show a barrier item.
 */
public class ShopItemDisplay extends ItemElm {
    Shop shop;
    FancyTextElm name;

    // Layout
    public static final float ENTITY_SHIFT_Y = 0.2f;
    public static final float NAME_SHIFT_Y = 0.4f;
    public static final float NAME_DISPLAY_WIDTH = 0.9f;

    // Task handlers. Used to cancel animations and other visual changes
    private @Nullable TaskHandler loopHandler = null;


    // The Y translation applied by the spawning animation
    public static final float FOCUS_HEIGHT = 0.05f;

    // Loop animation duration and rotation
    public static final int      LOOP_TIME   = 32;
    public static final float    LOOP_ROT    = (float)Math.toRadians(120);

    // Edit animation scale and transition
    public static final Vector3f EDIT_SCALE  = new Vector3f(0.4f);
    public static final Vector3f EDIT_MOVE   = new Vector3f(0, 0.25f, 0.25f).mul(1f - 0.5f).add(0, 0.1f, 0);




    // Focus and loop animations
    private final @NotNull Animation focusAnimation;
    private final @NotNull Animation unfocusAnimation;
    private final @NotNull Animation loopAnimation;

    // Edit animations
    private final @NotNull Animation enterEditAnimation;
    //! leaveEditAnimation not needed as the unfocus animation uses a target transform




    /**
     * Creates a new ShopItemDisplay.
     * @param _shop The target shop.
     * @param _display A CustomItemDisplay to use to display the item.
     */
    public ShopItemDisplay(@NotNull Shop _shop, @NotNull CustomItemDisplay _display) {
        super(_shop.getWorld(), _display, new ItemElmStyle());
        shop = _shop;
        updateDisplay();


        // Setup spawn and despawn animations
        focusAnimation = new Animation(
            new Transition(ElmStyle.S_TIME * 2, Easings.sineOut)
            .additiveTransform(
                new Transform()
                .moveY(FOCUS_HEIGHT)
                .rotY(LOOP_ROT / 2)
            )
        );
        unfocusAnimation = new Animation(
            new Transition(ElmStyle.D_TIME * 2, Easings.sineOut)
            .targetTransform(style.getTransform())
        );


        // Setup loop animation
        loopAnimation = new Animation(
            new Transition(LOOP_TIME, Easings.linear)
            .additiveTransform(new Transform().rotY(LOOP_ROT))
        );


        // Setup edit animiations
        enterEditAnimation = new Animation(
            new Transition(Shop.CANVAS_ANIMATION_DELAY, Easings.sineOut)
            .additiveTransform(
                new Transform()
                .scale(EDIT_SCALE)
                .move(EDIT_MOVE)
                .rotY(LOOP_ROT / 2)
            )
        );
    }




    /**
     * Creates a new ShopItemDisplay using existing display entities.
     * @param _targetShop The target shop.
     * @param _rawDisplay A vanilla ItemDisplayEntity to use to display the item.
     * @param _rawName1 One of the TextDisplayEntity entities that make up the name of the item.
     * @param _rawName2 One of the TextDisplayEntity entities that make up the name of the item.
     */
    public ShopItemDisplay(@NotNull Shop _targetShop, @NotNull ItemDisplayEntity _rawDisplay, @Nullable TextDisplayEntity _rawName1, @Nullable TextDisplayEntity _rawName2) {
        this(_targetShop, new CustomItemDisplay(_rawDisplay));
        if(_rawName1 != null) _rawName1.remove(RemovalReason.KILLED);
        if(_rawName2 != null) _rawName2.remove(RemovalReason.KILLED);
    }

    /**
     * Creates a new ShopItemDisplay.
     * @param _targetShop The target shop.
     */
    public ShopItemDisplay(@NotNull Shop _targetShop) {
        this(_targetShop, new CustomItemDisplay(_targetShop.getWorld()));
    }




    /**
     * Updates the displayed item reading data from the target shop.
     */
    public void updateDisplay(){
        ItemStack _item = shop.getItem();


        // Spawn or despawn the name entity if necessary
        if(!shop.isFocused()) {
            if(name == null) {
                name = new FancyTextElm(world);
                name.style.setViewRange(0.2f);
                name.style.setBillboardMode(BillboardMode.VERTICAL);
                shop.setItemDisplayNameUUID(name.getFgEntity().getUuid(), name.getBgEntity().getUuid());
                name.spawn(new Vector3d(entity.getPosCopy()).add(0, NAME_SHIFT_Y, 0));
                name.getFgEntity().setCustomName(null);
                name.getBgEntity().setCustomName(null);
            }
        }
        else if(name != null) {
            name.despawnNow();
            name = null;
            shop.setItemDisplayNameUUID(null, null);
        }


        // If the shop is unconfigured (item is AIR), display a barrier and EMPTY_SHOP_NAME as name
        if(_item.getItem() == Items.AIR) {
            ItemStack noItem = Items.BARRIER.getDefaultStack();
            noItem.setCustomName(Shop.EMPTY_SHOP_NAME);
            ((ItemElmStyle)style).setItem(noItem);
            if(name != null) {
                ((FancyTextElmStyle)name.style).setText(MinecraftUtils.getFancyItemName(noItem));
                name.setSize(new Vector2f(NAME_DISPLAY_WIDTH, 0.1f));
                name.flushStyle();
            }
        }


        // If the shop is configured, display the current item and its name
        else {
            ((ItemElmStyle)style).setItem(_item);

            // Get item name as a string
            final String fullName = MinecraftUtils.getFancyItemName(((ItemElmStyle)style).getItem()).getString();
            final StringBuilder truncatedName = new StringBuilder();

            // Wrap the name and calculate the amount tof lines
            int totLen = 0;
            int i;
            int ellipsisLen = FontSize.getWidth("…");
            for(i = 0; i < fullName.length(); ++i) {
                char c = fullName.charAt(i);
                totLen += FontSize.getWidth(String.valueOf(c));
                if((totLen + ellipsisLen) * FancyTextElmStyle.DEFAULT_TEXT_SCALE > TextElm.TEXT_PIXEL_BLOCK_RATIO * (NAME_DISPLAY_WIDTH - 0.1f)) {
                    break;
                }
                truncatedName.append(c);
            }

            // Set the new name and adjust the element height
            if(i < fullName.length()) truncatedName.append("…");
            if(name != null) {
                ((FancyTextElmStyle)name.style).setText(new Txt(truncatedName.toString()).get());
                name.setSize(new Vector2f(NAME_DISPLAY_WIDTH, 0.1f));
                name.flushStyle();
            }
        }


        // Update the entity
        flushStyle();
    }




    @Override
    protected Transform __calcTransform() {
        return super.__calcTransform()
            .rotY(shop.getDefaultRotation())
            .scale(0.8f)
        ;
    }




    /**
     * Enters the focus state, making the item more visible and starting the loop animation
     */
    public void enterFocusState(){

        // Hide custom name
        updateDisplay();

        // Start animations
        applyAnimation(focusAnimation);
        startLoopAnimation();
    }
    /**
     * Starts the loop animation.
     */
    public void startLoopAnimation() {
        loopHandler = Scheduler.loop(0, loopAnimation.getTotalDuration(), () -> applyAnimation(loopAnimation));
    }




    /**
     * Leaves the focus state.
     */
    public void leaveFocusState(){

        // Stop loop animation and start unfocus animation
        stopLoopAnimation();
        applyAnimation(unfocusAnimation);

        // Show custom name after animations end
        Scheduler.schedule(unfocusAnimation.getTotalDuration(), this::updateDisplay);
    }
    /**
     * Stops the loop animation.
     */
    public void stopLoopAnimation() {
        loopHandler.cancel();
        futureDataQueue.clear();
    }




    /**
     * Enters the edit state
     */
    public void enterEditState(){
        applyAnimation(enterEditAnimation);
    }




    /**
     * Leaves the edit state
     */
    public void leaveEditState(){
        // Empty
        //! leaveEditAnimation not needed as the unfocus animation uses a target transform
    }



    @Override
    public void spawn(Vector3d pos) {

        // Spawn the entity and remove tracking custom name
        super.spawn(new Vector3d(pos).add(0, ENTITY_SHIFT_Y, 0));
        entity.setCustomName(null);
        entity.setCustomNameVisible(false);

        // Force display update to spawn the name element
        updateDisplay();
    }



    @Override
    public void despawn() {
        super.despawn();
        if(name != null) name.despawnNow();
    }
}
