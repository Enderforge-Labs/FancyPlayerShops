package com.snek.framework.ui.elements;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.data_types.containers.Pair;
import com.snek.framework.data_types.displays.CustomDisplay;
import com.snek.framework.data_types.displays.CustomItemDisplay;
import com.snek.framework.data_types.displays.CustomTextDisplay;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.ui.styles.ItemElmStyle;

import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;








/**
 * An element that can display items.
 */
public class ItemElm extends Elm {
    private ItemElmStyle getStyle() { return (ItemElmStyle)style; }




    // Item transform exceptions
    private static final Map<String, Pair<ModelTransformationMode, Transform>> transformExceptions = new HashMap<>(Map.ofEntries(
        Map.entry(Items.TRIDENT.getTranslationKey(), Pair.from(
            ModelTransformationMode.GUI,
            new Transform()
        )),
        Map.entry(Items.SHIELD.getTranslationKey(),  Pair.from(
            ModelTransformationMode.GROUND,
            new Transform().scale(2.5f).moveY(-0.15f).rotY((float)Math.PI)
        ))
    ));


    // Tag transform exceptions
    private static final Map<TagKey<Item>, Pair<ModelTransformationMode, Transform>> tagTransformExceptions = new HashMap<>(Map.ofEntries(
        Map.entry(ItemTags.BANNERS, Pair.from(
            ModelTransformationMode.NONE,
            new Transform().scale(0.6f).moveY(-0.08f).rotY((float)Math.PI)
        ))
    ));








    /**
     * Creates a new ItemElm using an existing entity and a custom style.
     * @param _world The world in which to place the element.
     * @param _entity The display entity.
     * @param _style The custom style.
     */
    protected ItemElm(@NotNull ServerWorld _world, @NotNull CustomDisplay _entity, @NotNull ElmStyle _style) {
        super(_world, _entity, _style);
        ((CustomItemDisplay)getEntity()).setDisplayType(ModelTransformationMode.NONE);
    }


    /**
     * Creates a new ItemElm using a custom style.
     * @param _world The world in which to place the element.
     * @param _style The custom style.
     */
    protected ItemElm(@NotNull ServerWorld _world, @NotNull ElmStyle _style) {
        this(_world, new CustomItemDisplay(_world), _style);
    }


    /**
     * Creates a new ItemElm using the default style.
     * @param _world The world in which to place the element.
     */
    public ItemElm(@NotNull ServerWorld _world){
        this(_world, new CustomTextDisplay(_world), new ItemElmStyle());
    }




    @Override
    public void flushStyle() {
        super.flushStyle();
        CustomItemDisplay e2 = (CustomItemDisplay)entity;
        { Flagged<ItemStack> f = getStyle().getFlaggedItem(); if(f.isFlagged()) { e2.setItemStack(f.get()); f.unflag(); }}
    }




    @Override
    protected Transform __calcTransform() {

        // Retrieve parent transformation and exception. Item exceptions have priority over tag exceptions
        Transform t = super.__calcTransform();
        Pair<ModelTransformationMode, Transform> exception = transformExceptions.get(getStyle().getItem().getItem().getTranslationKey());
        if(exception == null) for (var entry : tagTransformExceptions.entrySet()) {
            if(getStyle().getItem().isIn(entry.getKey())) {
                exception = entry.getValue();
                break;
            }
        }


        // Update the entity's display type and apply the exception's transformation to the parent one if needed
        ((CustomItemDisplay)getEntity()).setDisplayType(exception == null ? ModelTransformationMode.NONE : exception.first);
        return exception == null ? t : t.apply(exception.second); //FIXME shield doesn't go up enough when the edit animation is triggered. prob has to do with they Y translation
    }
}
