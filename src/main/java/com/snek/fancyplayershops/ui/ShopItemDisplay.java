package com.snek.fancyplayershops.ui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;

import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.styles.SimpleNameDisplay_S;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.displays.CustomItemDisplay;
import com.snek.framework.generated.FontSize;
import com.snek.framework.ui.elements.FancyTextElm;
import com.snek.framework.ui.elements.ItemElm;
import com.snek.framework.ui.elements.styles.ElmStyle;
import com.snek.framework.ui.elements.styles.FancyTextElmStyle;
import com.snek.framework.ui.elements.styles.ItemElmStyle;
import com.snek.framework.ui.elements.styles.TextElmStyle;
import com.snek.framework.utils.Easings;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;
import com.snek.framework.utils.scheduler.Scheduler;
import com.snek.framework.utils.scheduler.TaskHandler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Display.BillboardConstraints;
import net.minecraft.world.entity.Display.ItemDisplay;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;








/**
 * An item display that shows the item currently being sold by a shop.
 * <p> Unconfigured shops show a barrier item.
 */
public class ShopItemDisplay extends ItemElm {
    public static final @NotNull String ITEM_DISPLAY_CUSTOM_NAME = FancyPlayerShops.MOD_ID + ".ui.itemdisplay";
    private final @NotNull  Shop         shop;
    private       @Nullable FancyTextElm name;

    // Layout
    public static final float ENTITY_SHIFT_Y = 0.2f;
    public static final float NAME_SHIFT_Y = 0.4f;
    public static final float NAME_DISPLAY_WIDTH = 0.9f;

    // Task handlers. Used to cancel animations and other visual changes
    private @Nullable TaskHandler loopHandler = null;


    // The Y translation applied by the spawning animation
    public static final float FOCUS_HEIGHT = 0.05f;

    // Animation duration and loop rotation
    public static final int   S_TIME = ElmStyle.S_TIME * 2;
    public static final int   D_TIME = ElmStyle.D_TIME * 2;
    public static final int   LOOP_TIME   = 32;
    public static final float LOOP_ROT    = (float)Math.toRadians(120);

    // Edit animation scale and transition
    public static final @NotNull Vector3f EDIT_SCALE  = new Vector3f(0.4f);
    public static final @NotNull Vector3f EDIT_MOVE   = new Vector3f(0, 0.25f, 0.25f).mul(1f - 0.5f).add(0, 0.15f, 0);




    // Setup focus animation (unfocus animation is created dynamically)
    private static final @NotNull Animation focusAnimation = new Animation(
        new Transition(S_TIME, Easings.sineOut)
        .additiveTransform(
            new Transform()
            .moveY(FOCUS_HEIGHT)
            .rotY(LOOP_ROT / 2)
        )
    );
    private @NotNull Animation unfocusAnimation;


    // Setup loop animation
    private static final @NotNull Animation loopAnimation = new Animation(
        new Transition(LOOP_TIME, Easings.linear)
        .additiveTransform(new Transform().rotY(LOOP_ROT))
    );


    // Setup edit animiations
    //! leaveEditAnimation not needed as the unfocus animation uses a target transform
    private static final @NotNull Animation enterEditAnimation = new Animation(
        new Transition(Shop.CANVAS_ANIMATION_DELAY, Easings.sineOut)
        .additiveTransform(
            new Transform()
            .scale(EDIT_SCALE)
            .move(EDIT_MOVE)
            .rotY(LOOP_ROT / 2)
        )
    );








    /**
     * Creates a new ShopItemDisplay.
     * @param _shop The target shop.
     * @param _display A CustomItemDisplay to use to display the item.
     */
    public ShopItemDisplay(final @NotNull Shop _shop, final @NotNull CustomItemDisplay _display) {
        super(_shop.getWorld(), _display, new ItemElmStyle());
        shop = _shop;
        //! updateDisplay call is in spawn()


        // Setup unfocus animation
        unfocusAnimation = new Animation(
            new Transition(D_TIME, Easings.sineOut)
            .targetTransform(getStyle().getTransform())
        );
    }




    /**
     * Creates a new ShopItemDisplay using existing display entities.
     * @param _targetShop The target shop.
     * @param _rawDisplay A vanilla ItemDisplayEntity to use to display the item.
     * @param _rawName1 One of the TextDisplay entities that make up the name of the item.
     * @param _rawName2 One of the TextDisplay entities that make up the name of the item.
     */
    public ShopItemDisplay(final @NotNull Shop _targetShop, final @NotNull ItemDisplay _rawDisplay) {
        this(_targetShop, new CustomItemDisplay(_rawDisplay));
    }


    /**
     * Creates a new ShopItemDisplay.
     * @param _targetShop The target shop.
     */
    public ShopItemDisplay(final @NotNull Shop _targetShop) {
        this(_targetShop, new CustomItemDisplay(_targetShop.getWorld()));
    }




    /**
     * Updates the displayed item reading data from the target shop.
     */
    public void updateDisplay() {
        final ItemStack _item = shop.getItem();


        // Spawn or despawn the name entity if necessary
        if(!shop.isFocused()) spawnNameEntity();
        else                despawnNameEntity();


        // If the shop is unconfigured (item is AIR), display a barrier and EMPTY_SHOP_NAME as name
        if(_item.getItem() == Items.AIR) {
            final ItemStack noItem = Items.BARRIER.getDefaultInstance();
            noItem.setHoverName(Shop.EMPTY_SHOP_NAME);
            getStyle(ItemElmStyle.class).setItem(noItem);
            if(name != null) {
                name.getStyle(FancyTextElmStyle.class).setText(MinecraftUtils.getFancyItemName(noItem));
                name.setSize(new Vector2f(NAME_DISPLAY_WIDTH, 0.1f));
                name.flushStyle();
            }
        }


        // If the shop is configured, display the current item and its name
        else {
            getStyle(ItemElmStyle.class).setItem(_item);

            // Get item name as a string
            final String fullName = Utils.formatPriceShort(shop.getPrice()) + " - " + MinecraftUtils.getFancyItemName(getStyle(ItemElmStyle.class).getItem()).getString();
            final StringBuilder truncatedName = new StringBuilder();

            // Wrap the name and calculate the amount of lines
            int i;
            float totLen = 0;
            final float ellipsisLen = FontSize.getWidth("…");
            for(i = 0; i < fullName.length(); ++i) {
                final char c = fullName.charAt(i);
                totLen += FontSize.getWidth(String.valueOf(c));
                if((totLen + ellipsisLen) * TextElmStyle.DEFAULT_TEXT_SCALE > NAME_DISPLAY_WIDTH - 0.1f) {
                    break;
                }
                truncatedName.append(c);
            }

            // Set the new name and adjust the element height
            if(i < fullName.length()) truncatedName.append("…");
            if(name != null) {
                name.getStyle(FancyTextElmStyle.class).setText(new Txt(truncatedName.toString()).get());
                name.setSize(new Vector2f(NAME_DISPLAY_WIDTH, 0.1f));
                name.flushStyle();
            }
        }


        // Update the entity
        //! Flag the transform to make sure items with different base transforms are recalculated without waiting for animations
        getStyle().editTransform();
        flushStyle();
    }




    @Override
    protected @NotNull Transform __calcTransform() {
        return super.__calcTransform()
            .rotY(shop.getDefaultRotation())
            .scale(0.4f)
        ;
    }




    /**
     * Enters the focus state, making the item more visible and starting the loop animation
     */
    public void enterFocusState() {

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
        if(loopHandler != null) loopHandler.cancel();
        loopHandler = Scheduler.loop(0, loopAnimation.getTotalDuration(), () -> applyAnimation(loopAnimation));
    }




    /**
     * Leaves the focus state.
     */
    public void leaveFocusState() {

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
        if(loopHandler != null) loopHandler.cancel();
    }




    /**
     * Enters the edit state
     */
    public void enterEditState() {
        applyAnimation(enterEditAnimation);
    }




    /**
     * Leaves the edit state
     */
    public void leaveEditState() {
        //! leaveEditAnimation not needed as the unfocus animation uses a target transform
    }




    @Override
    public void spawn(final @NotNull Vector3d pos) {

        // Spawn the entity and remove tracking custom name
        super.spawn(new Vector3d(pos).add(0, ENTITY_SHIFT_Y, 0));
        getEntity().setCustomName(new Txt(ITEM_DISPLAY_CUSTOM_NAME).get());
        getEntity().setCustomNameVisible(false);

        // Force display update to spawn the name element
        updateDisplay();
    }



    @Override
    public void despawn() {
        super.despawn();
        if(name != null) name.despawnNow();
    }




    /**
     * Spawns the name text display if needed.
     */
    public void spawnNameEntity(){
        if(name == null) {
            name = new FancyTextElm(world, new SimpleNameDisplay_S(shop));
            name.getStyle().setViewRange(0.2f);
            name.getStyle().setBillboardMode(BillboardConstraints.VERTICAL);
            name.spawn(new Vector3d(getEntity().getPosCopy()).add(0, NAME_SHIFT_Y, 0));
        }
    }




    /**
     * Despawns the name text display if needed.
     */
    public void despawnNameEntity(){
        if(name != null) {
            name.despawnNow();
            name = null;
        }
    }




    /**
     * Forcibly sets the item display of a loaded shop when the display entity is loaded into the world.
     * <p> Must be called on entity load event.
     * @param entity The loaded entity.
     */
    public static void onEntityLoad(final @NotNull Entity entity) {
        if(entity instanceof ItemDisplay) {
            if(
                entity.level() != null &&
                entity.hasCustomName() &&
                entity.getCustomName().getString().equals(ITEM_DISPLAY_CUSTOM_NAME)
            ) {
                //! Force data loading in case this event gets called before the scheduled data loading
                ShopManager.loadShops();

                // Remove entity
                if(!entity.isRemoved()) {
                    entity.remove(RemovalReason.KILLED);

                    // Respawn shop item display if needed
                    final Shop shop = ShopManager.findShop(entity.blockPosition(), entity.level());
                    if(shop != null) {
                        shop.invalidateItemDisplay();
                    }
                }
            }
        }
    }
}
