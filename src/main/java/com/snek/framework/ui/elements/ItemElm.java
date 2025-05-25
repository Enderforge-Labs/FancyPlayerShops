package com.snek.framework.ui.elements;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.data_types.containers.Pair;
import com.snek.framework.data_types.displays.CustomDisplay;
import com.snek.framework.data_types.displays.CustomItemDisplay;
import com.snek.framework.data_types.displays.CustomTextDisplay;
import com.snek.framework.data_types.ui.TextAlignment;
import com.snek.framework.ui.elements.styles.ElmStyle;
import com.snek.framework.ui.elements.styles.ItemElmStyle;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;








/**
 * An element that can display items.
 */
public class ItemElm extends Elm {
    private @NotNull CustomItemDisplay getThisEntity() { return getEntity(CustomItemDisplay.class); }
    private @NotNull ItemElmStyle      getThisStyle () { return getStyle (ItemElmStyle     .class); }




    // Item transform exceptions
    private static final @NotNull Map<
        @NotNull String,
        @Nullable Pair<
            @NotNull ItemDisplayContext,
            @NotNull Transform
        >
    > transformExceptions = new HashMap<>(Map.ofEntries(
        Map.entry(Items.TRIDENT.getDescriptionId(), Pair.from(
            ItemDisplayContext.GUI,
            new Transform()
        )),
        Map.entry(Items.SHIELD.getDescriptionId(),  Pair.from(
            ItemDisplayContext.GROUND,
            new Transform().scale(2.5f).moveY(-0.15f).rotY((float)Math.PI)
        ))
    ));


    // Tag transform exceptions
    private static final @NotNull Map<
        @NotNull TagKey<@NotNull Item>,
        @Nullable Pair<
            @NotNull ItemDisplayContext,
            @NotNull Transform
        >
    > tagTransformExceptions = new HashMap<>(Map.ofEntries(
        Map.entry(ItemTags.BANNERS, Pair.from(
            ItemDisplayContext.NONE,
            new Transform().scale(0.6f).moveY(-0.08f).rotY((float)Math.PI)
        )),
        Map.entry(ItemTags.BEDS, Pair.from(
            ItemDisplayContext.GROUND,
            new Transform().scale(2.5f).moveY(-0.14f)
        ))
    ));








    /**
     * Creates a new ItemElm using an existing entity and a custom style.
     * @param _world The world in which to place the element.
     * @param _entity The display entity.
     * @param _style The custom style.
     */
    protected ItemElm(final @NotNull ServerLevel _world, final @NotNull CustomDisplay _entity, final @NotNull ElmStyle _style) {
        super(_world, _entity, _style);
        getThisEntity().setDisplayType(ItemDisplayContext.NONE);
    }


    /**
     * Creates a new ItemElm using a custom style.
     * @param _world The world in which to place the element.
     * @param _style The custom style.
     */
    protected ItemElm(final @NotNull ServerLevel _world, final @NotNull ElmStyle _style) {
        this(_world, new CustomItemDisplay(_world), _style);
    }


    /**
     * Creates a new ItemElm using the default style.
     * @param _world The world in which to place the element.
     */
    public ItemElm(final @NotNull ServerLevel _world) {
        this(_world, new CustomItemDisplay(_world), new ItemElmStyle());
    }




    @Override
    public void flushStyle() {

        // Apply item stack
        { final Flagged<ItemStack> f = getThisStyle().getFlaggedItem();
        if(f.isFlagged()) {
            getThisEntity().setItemStack(f.get());
            f.unflag();
        }}


        // Handle transform calculations separately
        {
            final Flagged<Transform> f = getThisStyle().getFlaggedTransform();
            if(f.isFlagged()) {
                final Transform t = __calcTransform();
                t.moveY(t.getScale().y / 2f);
                getThisEntity().setTransformation(t.moveZ(EPSILON * epsilonPolarity).toMinecraftTransform());
                f.unflag();
            }
        }


        // Handle the other inherited values normally
        super.flushStyle();
    }




    @Override
    protected @NotNull Transform __calcTransform() {

        // Retrieve parent transformation and exception. Item exceptions have priority over tag exceptions
        Pair<ItemDisplayContext, Transform> exception = transformExceptions.get(getThisStyle().getItem().getItem().getDescriptionId());
        if(exception == null) for(var entry : tagTransformExceptions.entrySet()) {
            if(getThisStyle().getItem().is(entry.getKey())) {
                exception = entry.getValue();
                break;
            }
        }

        // Update the entity's display type and apply the exception's transformation to the parent one if needed
        getThisEntity().setDisplayType(exception == null ? ItemDisplayContext.NONE : exception.getFirst());
        final Transform t = super.__calcTransform();
        return exception == null ? t : t.apply(exception.getSecond());
        //FIXME shield and other y-translated items don't go up enough when the edit animation is triggered
        //FIXME ^ y translation doesn't scale with y size so the final translation looks greater on smaller scales
    }
}