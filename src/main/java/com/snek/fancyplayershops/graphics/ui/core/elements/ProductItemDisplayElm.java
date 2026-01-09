package com.snek.fancyplayershops.graphics.ui.core.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;

import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.core.styles.SimpleNameDisplay_S;
import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.basic.elements.PanelTextElm;
import com.snek.frameworklib.graphics.basic.elements.ItemElm;
import com.snek.frameworklib.graphics.core.Canvas;
import com.snek.frameworklib.graphics.core.styles.Style;
import com.snek.frameworklib.graphics.basic.styles.PanelTextStyle;
import com.snek.frameworklib.graphics.basic.styles.ItemStyle;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;
import com.snek.frameworklib.utils.scheduler.LoopTaskHandler;
import com.snek.frameworklib.utils.scheduler.Scheduler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Display.ItemDisplay;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;








/**
 * An item display that shows the item currently being sold by a product display.
 * <p> Unconfigured product displays show a barrier item.
 */
public class ProductItemDisplayElm extends ItemElm {
    public static final @NotNull String ITEM_DISPLAY_CUSTOM_NAME = FancyPlayerShops.MOD_ID + ".ui.itemdisplay";
    private final @NotNull  ProductDisplay productDisplay;
    private       @Nullable PanelTextElm name;

    // Layout
    public static final float NAME_SHIFT_Y = 0.6f;
    public static final float NAME_DISPLAY_WIDTH = 0.9f;

    // Task handlers. Used to cancel animations and other visual changes
    private @Nullable LoopTaskHandler loopAnimationHandler = null;


    // The Y translation applied by the spawning animation
    public static final float FOCUS_HEIGHT = 0.05f;

    // Animation duration and loop rotation
    public static final int   S_TIME = Style.S_TIME * 2;
    public static final int   D_TIME = Style.D_TIME * 2;
    public static final int   LOOP_TIME   = 32;
    public static final float LOOP_ROT    = (float)Math.toRadians(120);

    // Edit animation scale and transition
    public static final @NotNull Vector3f EDIT_SCALE  = new Vector3f(0.4f);
    public static final @NotNull Vector3f EDIT_MOVE   = new Vector3f(0, 0.25f, 0.25f).mul(1f - 0.5f).add(0, 0.30f, 0);




    // Setup focus animation (unfocus animation is created dynamically)
    private static final @NotNull Animation focusAnimation = new Animation(
        new Transition(S_TIME, Easings.sineOut)
        .additiveTransform(
            new Transform()
            .moveY(FOCUS_HEIGHT)
            .rotY(LOOP_ROT / 2)
        )
    );
    private final @NotNull Animation unfocusAnimation;


    // Setup loop animation
    private static final @NotNull Animation loopAnimation = new Animation(
        new Transition(LOOP_TIME, Easings.linear)
        .additiveTransform(new Transform().rotY(LOOP_ROT))
    );


    // Setup edit animiations
    //! leaveEditAnimation not needed as the unfocus animation uses a target transform
    private static final @NotNull Animation enterEditAnimation = new Animation(
        new Transition(Canvas.CANVAS_ROTATION_TIME, Easings.cubicOut)
        .additiveTransform(
            new Transform()
            .scale(EDIT_SCALE)
            .move(EDIT_MOVE)
            .rotY(LOOP_ROT / 2)
        )
    );




    /**
     * Creates a new ProductItemDisplay.
     * @param targetProductDisplay The target product display.
     */
    public ProductItemDisplayElm(final @NotNull ProductDisplay targetProductDisplay) {
        super(targetProductDisplay.getLevel(), new ItemStyle());
        productDisplay = targetProductDisplay;
        //! updateDisplay call is in spawn()


        // Setup unfocus animation
        unfocusAnimation = new Animation(
            new Transition(D_TIME, Easings.sineOut)
            .targetTransform(getStyle().getTransform())
        );
    }




    /**
     * Updates the displayed item reading data from the target product display.
     */
    public void updateDisplay() {
        final ItemStack _item = productDisplay.getItem();


        // Spawn or despawn the name entity if necessary
        if(!productDisplay.isFocused()) spawnNameEntity();
        else                despawnNameEntity();


        // If the product display is unconfigured (item is AIR), display a barrier and EMPTY_PRODUCT_DISPLAY_NAME as name
        if(_item.is(Items.AIR)) {
            final ItemStack noItem = Items.BARRIER.getDefaultInstance();
            getStyle(ItemStyle.class).setItem(noItem);
            if(name != null) {
                name.getStyle(PanelTextStyle.class).setText(new Txt(ProductDisplay.EMPTY_PRODUCT_DISPLAY_NAME).white().get());
                name.flushStyle();
            }
        }


        // If the product display is configured, display the current item and its name
        else {
            getStyle(ItemStyle.class).setItem(_item);
            if(name != null) {
                final String fullName = Utils.formatPriceShort(productDisplay.getPrice()) + " - " + productDisplay.getStandaloneName();
                name.getStyle(PanelTextStyle.class).setText(new Txt(fullName).white().get());
                name.flushStyle();
            }
        }


        // Update the entity
        //! Flag the transform to make sure items with different base transforms are recalculated without waiting for animations
        getStyle().editTransform();
        flushStyle();
    }




    @Override
    public @NotNull Transform __calcTransform() {
        return super.__calcTransform()
            .rotY(productDisplay.getDefaultRotation() * (float)Math.PI / 4)
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
        applyAnimation(focusAnimation, false, true); // Height change
        startLoopAnimation();                        // Loop movement
    }
    /**
     * Starts the loop animation.
     */
    public void startLoopAnimation() {
        if(loopAnimationHandler != null) loopAnimationHandler.cancel();
        loopAnimationHandler = Scheduler.loop(0, loopAnimation.getTotalDuration(), () -> applyAnimation(loopAnimation, false, true));
    }




    /**
     * Leaves the focus state.
     */
    public void leaveFocusState() {

        // Stop loop animation and start unfocus animation
        stopLoopAnimation();
        applyAnimation(unfocusAnimation, false, true);

        // Show custom name after animations end
        Scheduler.schedule(unfocusAnimation.getTotalDuration(), this::updateDisplay);
    }
    /**
     * Stops the loop animation.
     */
    public void stopLoopAnimation() {
        if(loopAnimationHandler != null) loopAnimationHandler.cancel();
    }




    /**
     * Enters the edit state
     */
    public void enterEditState() {

        // Adjust global rotation
        final @NotNull ProductCanvasBase activeCanvas = productDisplay.getActiveCanvas();
        activeCanvas.updateItemDisplayRot(0, activeCanvas.getContext().getRotation(), true);

        // Local position shift
        applyAnimation(enterEditAnimation, false, true);
    }




    /**
     * Leaves the edit state
     */
    public void leaveEditState() {
        //! leaveEditAnimation not needed as the unfocus animation uses a target transform
    }




    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {

        // Spawn the entity and remove tracking custom name
        super.spawn(pos, animate);
        getEntity().setCustomName(new Txt(ITEM_DISPLAY_CUSTOM_NAME).get());
        getEntity().setCustomNameVisible(false);

        // Force display update to spawn the name element
        updateDisplay();
    }



    @Override
    public void despawn(final boolean animate) {
        super.despawn(animate);
        if(name != null) name.despawn(false);
    }




    /**
     * Spawns the name text display if needed.
     */
    public void spawnNameEntity() {
        if(name == null) {
            name = new PanelTextElm(level, new SimpleNameDisplay_S());
            name.setSize(new Vector2f(NAME_DISPLAY_WIDTH, 0.1f));
            name.spawn(new Vector3d(getEntity().getPosCopy()).add(0, NAME_SHIFT_Y, 0), true);
        }
    }




    /**
     * Despawns the name text display if needed.
     */
    public void despawnNameEntity() {
        if(name != null) {
            name.despawn(false);
            name = null;
        }
    }




    /**
     * Forcibly sets the item display of a loaded shop when the display entity is loaded into the level.
     * <p> Must be called on entity load event.
     * @param entity The loaded entity.
     */
    public static void onEntityLoad_item(final @NotNull Entity entity) {
        if(entity instanceof ItemDisplay) {
            if(
                entity.level() != null &&
                entity.hasCustomName() &&
                entity.getCustomName().getString().equals(ITEM_DISPLAY_CUSTOM_NAME)
            ) {
                //! Force data loading in case this event gets called before the scheduled data loading
                ProductDisplayManager.loadDisplays();

                // Remove entity
                if(!entity.isRemoved()) {
                    entity.remove(RemovalReason.KILLED);

                    // Respawn shop item display if needed
                    final ProductDisplay shop = ProductDisplayManager.findDisplay(entity.blockPosition(), entity.level());
                    if(shop != null) {
                        shop.invalidateItemDisplay();
                    }
                }
            }
        }
    }
}
