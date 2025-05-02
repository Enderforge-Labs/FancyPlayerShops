package com.snek.framework.ui.elements;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.InterpolatedData;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.TransitionStep;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.data_types.containers.IndexedArrayDeque;
import com.snek.framework.data_types.displays.CustomDisplay;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.elements.styles.ElmStyle;
import com.snek.framework.ui.interfaces.Hoverable;
import com.snek.framework.utils.Easing;
import com.snek.framework.utils.SpaceUtils;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.scheduler.Scheduler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
















/**
 * An abstract class that represents a visible UI Element.
 */
public abstract class Elm extends Div {
    public static final @NotNull String ENTITY_CUSTOM_NAME = FancyPlayerShops.MOD_ID + ".ui.displayentity";
    public static final int QUEUE_LINGER_TICKS = 4;
    // ^ Additional update ticks the element stays in the update queue for after all of its steps have been processed.


    // Animation handling
    public    static final int TRANSITION_REFRESH_TIME = 2;                         // The time between transition updates. Measured in ticks
    private   static final @NotNull List<Elm> elmUpdateQueue = new ArrayList<>();   // The list of instances with pending transition steps
    protected        final @NotNull IndexedArrayDeque<InterpolatedData> futureDataQueue = new IndexedArrayDeque<>(); // The list of transition steps to apply to this instance in the next ticks. 1 for each update tick
    private boolean isQueued = false;                                               // Whether this instance is queued for updates. Updated manually
    private int queueLingerTicks = 0;

    // Forced imperceptible changes applied to the entity's interpolated data.
    //! Minecraft's display entity rendering system repeats ticks if the new value doesn't differ from the old one.
    //! EPSILON is either added or subtracted from the target value intermittently in order to minimize error build ups and prevent visual tick duplicates.
    //! 0.000001 is the minimum value difference Minecraft can recognize as a change.
    public static final float EPSILON = 0.0000011f;
    protected int epsilonPolarity = 1;




    // In-world data
    protected final @NotNull ServerWorld   world;     // The world this Elm will be spawned in
    private   final @NotNull CustomDisplay entity;    // The display entity held by this element
    private   final @NotNull ElmStyle      style;     // The style of the element
    protected       boolean isSpawned = false;        // Whether the element has been spawned into the world
    private         boolean isHovered = false;        // Whether the element is being hovered on by a player's crosshair. //! Only valid in Hoverable instances
    public          boolean isSpawned() { return isSpawned; }




    /**
     * Retrieves the display held by this element.
     * @return The custom display.
     */
    public CustomDisplay getEntity() {
        return entity;
    }
    /**
     * Retrieves the custom display held by this element as the specified subclass.
     * @param type The sublass to cast the custom display to.
     * @return The custom display casted to the specified class.
     */
    public <T> @NotNull T getEntity(final @NotNull Class<T> type) {
        if(type.isInstance(entity)) return type.cast(entity);
        else throw new ClassCastException("Cannot cast entity from " + entity.getClass().getName() + " to " + type.getName());
    }


    /**
     * Retrieves the style used by this element.
     * @return The style.
     */
    public ElmStyle getStyle() {
        return style;
    }
    /**
     * Retrieves the style used by this element as the specified subclass.
     * @param type The sublass to cast the style to.
     * @return The style casted to the specified class.
     */
    public <T> @NotNull T getStyle(final @NotNull Class<T> type) {
        if(type.isInstance(style)) return type.cast(style);
        else throw new ClassCastException("Cannot cast style from " + style.getClass().getName() + " to " + type.getName());
    }








    /**
     * Creates a new Elm using an existing CustomDisplay and a custom style.
     * @param _world The world in which to place the element.
     * @param _entity The display entity.
     * @param _style The custom style.
     */
    protected Elm(final @NotNull ServerWorld _world, final @NotNull CustomDisplay _entity, final @NotNull ElmStyle _style) {
        super();
        world  = _world;
        entity = _entity;
        style  = _style;
        style.resetAll();
    }




    /**
     * Flushes changeable style values to the entity.
     * This does not start an interpolation.
     */
    protected void flushStyle() {
        epsilonPolarity *= -1;

        // Apply transform
        { final Flagged<Transform> f = style.getFlaggedTransform();
        if(f.isFlagged()) {
            entity.setTransformation(__calcTransform().moveZ(EPSILON * epsilonPolarity).toMinecraftTransform());
            f.unflag();
        }}

        // Apply view range
        { final Flagged<Float> f = style.getFlaggedViewRange();
        if(f.isFlagged()) {
            entity.setViewRange(f.get());
            f.unflag();
        }}

        // Apply billboard mode
        { final Flagged<BillboardMode> f = style.getFlaggedBillboardMode();
        if(f.isFlagged()) {
            entity.setBillboardMode(f.get());
            f.unflag();
        }}
    }




    @Override
    protected void updateAbsPosSelf() {
        final Vector2f oldPos = new Vector2f(getAbsPos());
        super.updateAbsPosSelf();
        if(!getAbsPos().equals(oldPos)) {
            style.editTransform();
            flushStyle();
        }
        //! This check's sole purpose is to prevent unneeded transform updates and comparisons
    }


    @Override
    protected void updateZIndexSelf() {
        final int oldZIndex = getZIndex();
        super.updateZIndexSelf();
        if(getZIndex() != oldZIndex) {
            style.editTransform();
            flushStyle();
        }
        //! This check's sole purpose is to prevent unneeded transform updates and comparisons
    }


    @Override
    public int getLayerCount() {
        return 1;
    }


    /**
     * Calculates the final transform to apply to the entity.
     * <p> This takes into account the element's position, alignment options, Z-index and visual transform.
     * @return The transform.
     */
    protected @NotNull Transform __calcTransform() {
        return style.getTransform().copy()
            .move(getAbsPos().x, getAbsPos().y, getZIndex() * 0.001f) //TODO move Z layer spacing to config file
        ;
    }








    /**
     * Instantly calculates animation steps and adds this element to the update queue.
     * <p> Partial steps at the end of the animation are expanded to cover the entire step.
     * @param animation The animation to apply.
     */
    @Override
    public void applyAnimation(final @NotNull Animation animation) {
        super.applyAnimation(animation);

        // Add element to the update queue and update the queued state
        if(!isQueued) {
            elmUpdateQueue.add(this);
            isQueued = true;
            queueLingerTicks = QUEUE_LINGER_TICKS;
        }

        // Apply each transition one at a time
        int shift = 0;
        for(final Transition transition : animation.getTransitions()) {
            shift += __applyAnimationTransition(transition, shift);
        }
    }




    @Override
    public void applyAnimationNow(final @NotNull Animation animation) {
        super.applyAnimationNow(animation);

        // Apply each transition one at a time
        for(final Transition transition : animation.getTransitions()) {
            __applyAnimationTransitionNow(transition);
        }
    }


    /**
     * Helper function.
     * <p> Instantly calculates the result of a single transition and applies it to the element.
     * @param t The transition to apply.
     */
    protected void __applyAnimationTransitionNow(final @NotNull Transition t) {

        // Calculate step and apply it instantly
        final TransitionStep step = t.createStep(1);
        final InterpolatedData data = __generateInterpolatedData();
        data.apply(step);
        __applyTransitionStep(data);
        flushStyle();

        // Update existing future data if present. Instantly start the interpolation otherwise
        if(futureDataQueue.isEmpty()) {
            entity.setInterpolationDuration(0);
            entity.setStartInterpolation();
        }
        else {
            for(InterpolatedData d : futureDataQueue) {
                d.apply(step);
            }
        }
    }




    /**
     * Helper function.
     * <p> Instantly calculates the steps of a single transition and adds them to this element's future data.
     * @param transition The transition to apply.
     * @param shift the amount of future data to skip before applying this transition.
     * @return The amount of future data this transition affected.
     */
    private int __applyAnimationTransition(final @NotNull Transition transition, final int shift) {


        // Calculate transition as a list of steps
        final List<TransitionStep> animationSteps = new ArrayList<>();
        final int time = transition.getDuration();            // The duration of this transition
        final Easing e = transition.getEasing();
        for(int i = 0; i == 0 || i < time; i += TRANSITION_REFRESH_TIME) {
            final float factor = (float)e.compute(Math.min(1d, (double)(i + TRANSITION_REFRESH_TIME) / time));
            animationSteps.add(transition.createStep(factor));
        }


        // Create the necessary amount of future data before applying the steps
        futureDataQueue.getOrAdd(
            shift + animationSteps.size() - 1,
            () -> {
                return futureDataQueue.isEmpty() ?
                __generateInterpolatedData() :
                __generateInterpolatedData(futureDataQueue.size() - 1);
            }
        );


        // Update existing future data
        int j = 0;
        for(; j < animationSteps.size(); ++j) {
            futureDataQueue.get(j + shift).apply(animationSteps.get(j));
        }

        // If the amount of future data is larger than the amount of steps, apply the last step to the remaining data
        final TransitionStep lastStep = animationSteps.get(animationSteps.size() - 1);
        for(; j + shift < futureDataQueue.size(); ++j) {
            futureDataQueue.get(j + shift).apply(lastStep);
        }


        // Return transition width
        return animationSteps.size();
    }




    /**
     * Helper function.
     * <p> Applies a single future data to the element.
     * @param d The future data value.
     */
    protected void __applyTransitionStep(final @NotNull InterpolatedData d) {
        if(d.hasTransform()) { style.setTransform(d.getTransform()); }
    }


    /**
     * Helper function.
     * <p> Generates a base future data from the current values of the element.
     * @return The generated future data.
     */
    protected @NotNull InterpolatedData __generateInterpolatedData() {
        return new InterpolatedData(
            style.getTransform().copy(),
            null,
            null,
            null
        );
    }
    /**
     * Helper function.
     * <p> Generates a base future data from the values stored in an element of the future data queue.
     * @param index The index of the element to read values from.
     * @return The generated future data.
     */
    protected @NotNull InterpolatedData __generateInterpolatedData(final int index) {
        return new InterpolatedData(
            futureDataQueue.get(index).getTransform().copy(),
            null,
            null,
            null
        );
    }




    /**
     * Retrieves a copy of the last transform queued in the future data queue.
     * @return The last transform in the future data queue, or the current transform if the queue is empty.
     */
    public @NotNull Transform genLastTransform(){
        return futureDataQueue.isEmpty() ? getStyle().getTransform().copy() : futureDataQueue.peekLast().getTransform().copy();
    }




    @Override
    public void spawn(final @NotNull Vector3d pos) {
        if(isSpawned) return;

        // Flush previous changes to the entity to avoid bad interpolations and spawn the entity into the world
        flushStyle();
        final Animation primerAnimation = style.getPrimerAnimation();
        if(primerAnimation != null) {
            applyAnimationNow(primerAnimation);
        }
        entity.spawn(world, pos);


        // Set tracking custom name
        entity.setCustomNameVisible(false);
        entity.setCustomName(new Txt(ENTITY_CUSTOM_NAME).get());


        // Handle animations
        final Animation animation = style.getSpawnAnimation();
        if(animation != null) {
            applyAnimation(animation);
        }


        // Call superclass spawn and set spawned flag to true
        super.spawn(pos);
        isSpawned = true;
    }




    @Override
    public void despawn() {
        if(!isSpawned) return;

        // Call superclass spawn and set spawned flag to false
        super.despawn();
        isSpawned = false;

        // Handle animations
        final Animation animation = style.getDespawnAnimation();
        if(animation != null) {
            applyAnimation(animation);

            // Remove entity from the world after a delay
            Scheduler.schedule(animation.getTotalDuration(), this::despawnNow);
        }
        else {
            despawnNow();
        }
    }




    @Override
    public void despawnNow() {
        super.despawnNow();
        isSpawned = false;
        entity.despawn();
    }




    /**
     * Processes the first step of the scheduled transitions of this Elm.
     * @return false if the element has been removed from the update queue, true otherwise.
     */
    protected boolean stepTransition() {

        // Apply step and update the entity
        if(!futureDataQueue.isEmpty()) {
            __applyTransitionStep(futureDataQueue.removeFirst());
        }
        flushStyle();
        entity.setInterpolationDuration(TRANSITION_REFRESH_TIME);
        entity.setStartInterpolation();


        // Remove the element from the update queue if no steps are left and linger ticks have ran out
        if(futureDataQueue.isEmpty()) {
            if(queueLingerTicks > 0) {
                --queueLingerTicks;
            }
            else {
                elmUpdateQueue.remove(this);
                isQueued = false;
            }
            return false;
        }
        return true;
    }




    /**
     * Processes the first step of the scheduled transitions of all the queued elements.
     * <p> Must be called at the end of the tick every TRANSITION_REFRESH_TIME ticks.
     */
    public static void processUpdateQueue() {

        for(int i = 0; i < elmUpdateQueue.size();) {
            if(elmUpdateQueue.get(i).stepTransition()) ++i;
        }
    }




    /**
     * Updates the new hover state of the element and executes the specified callbacks.
     * @param player The player to check the view of. Can be null.
     */
    public void updateHoverState(final @Nullable PlayerEntity player) {
        if(this instanceof Hoverable h) {
            boolean hoverStateNext;


            // Calculate next hover state
            if(player == null) {
                hoverStateNext = false;
            }
            else {
                hoverStateNext = checkIntersection(player);
            }


            // Update current state and run hover state change callbacks if needed
            if(isHovered != hoverStateNext) {
                isHovered = hoverStateNext;
                if(isHovered) {
                    h.onHoverEnter(player);
                }
                else {
                    h.onHoverExit(player);
                }
            }


            // Call hover tick callback
            if(isHovered) {
                h.onHoverTick(player);
            }


            // Call check tick callback
            if(isHovered) {
                h.onCheckTick(player);
            }
        }
    }








    /**
     * Checks if a player is looking at this element.
     * <p> More specifically, it checks if the view vector of the player intersects
     *     with the bounding box of this UI element, from any direction or distance.
     * @param player The player.
     * @return true if the player is looking at this element, false otherwise.
     */
    public boolean checkIntersection(final @NotNull PlayerEntity player) {
        if(!isSpawned || style.getBillboardMode() != BillboardMode.FIXED) return false;
        final Transform t = __calcTransform();


        // Calculate the world coordinates of the display's origin. //! Left rotation and scale are ignored as they doesn't affect this
        final Vector3f origin =
            new Vector3f(t.getPos())
            .rotate(t.getGlobalRot())
            .add(entity.getPosCopy())
        ;


        // Calculate corner X position relative to the origin using the entity's local coordinate system
        final Vector3f shiftX = new Vector3f(getAbsSize().x / 2, 0, 0);
        shiftX.rotate(t.getRot()).rotate(t.getGlobalRot());


        // Check view intersection with the display's box
        final Vector3f corner1 = new Vector3f(origin).sub(shiftX);
        final Vector3f corner2 = new Vector3f(origin).add(shiftX);
        final Vector3f corner3 = new Vector3f(origin).add(shiftX).add(0, getAbsSize().y, 0);
        final Vector3f corner4 = new Vector3f(origin).sub(shiftX).add(0, getAbsSize().y, 0);
        return SpaceUtils.checkLineRectangleIntersection(
            player.getEyePos().toVector3f(),
            player.getRotationVec(1f).toVector3f(),
            new Vector3f[]{ corner1, corner2, corner3, corner4 }
        );
    }




    /**
     * Checks for stray displays and purges them.
     * <p> Must be called on entity load event.
     * @param entity The entity.
     */
    public static void onEntityLoad(final @NotNull Entity entity) {
        if(entity instanceof DisplayEntity) {
            if(
                entity.getWorld() != null &&
                entity.getCustomName() != null &&
                entity.getCustomName().getString().equals(ENTITY_CUSTOM_NAME)
            ) {
                entity.remove(RemovalReason.KILLED);
            }
        }
    }
}