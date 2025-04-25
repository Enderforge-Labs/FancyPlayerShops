package com.snek.framework.utils;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.item.PotionItem;
import net.minecraft.item.SkullItem;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
















/**
 * A utility class providing functions to handle Minecraft classes and data.
 */
public abstract class MinecraftUtils {
    private MinecraftUtils(){}








    /**
     * Returns the custom name of an item. If the item has no custom name, the default name is returned.
     * Potions include their effect in the name.
     * @param item The item.
     * @return The name of the item.
     */
    public static @NotNull Text getItemName(@NotNull ItemStack item) {

        // Custom names
        if(item.hasCustomName()) {
            return item.getName();
        }


        // Potions
        else if(item.getItem() instanceof PotionItem e) {
            final Potion potion = PotionUtil.getPotion(item);
            final String prefix = e instanceof SplashPotionItem ? "Splash" : (e instanceof LingeringPotionItem ? "Lingering" : "");

            // Water bottle special case
            if(potion == Potions.WATER) {
                return new Txt(prefix + " Water Bottle").get();
            }

            // Turtle master special case
            if(potion == Potions.TURTLE_MASTER || potion == Potions.LONG_TURTLE_MASTER || potion == Potions.STRONG_TURTLE_MASTER) {
                return new Txt(prefix + " Potion of the Turtle Master").get();
            }

            // Empty potions special cases
            if(potion == Potions.MUNDANE) return new Txt(prefix + " Mundane Potion").get();
            if(potion == Potions.THICK  ) return new Txt(prefix + " Thick Potion"  ).get();
            if(potion == Potions.AWKWARD) return new Txt(prefix + " Awkward Potion").get();

            // Actual potions
            else {
                if(potion == Potions.EMPTY) {
                    return new Txt(prefix + " Potion").get();
                }
                final List<StatusEffectInstance> effects = potion.getEffects();
                if(effects.isEmpty()) {
                    return new Txt(prefix + " Potion").get();
                }
                final StatusEffect effect = effects.get(0).getEffectType();
                if(effects.size() == 1) {
                    return new Txt(prefix + " Potion of ").cat(effect.getName()).get();
                }
                else {
                    return new Txt(prefix + " Potion of ").cat(effect.getName()).cat(new Txt(" & " + (effects.size() - 1) + " more").white()).get();
                }
            }
        }


        // Player heads
        else if(item.getItem() instanceof SkullItem e) {
            return new Txt().cat(e.getName(item)).get();
        }


        // Fallback
        return item.getItem().getName();
    }








    /**
     * Converts entity coordinates (double) to block coordinates (int).
     * Minecraft's block grid is weird and simply truncating the decimal part is not enough to convert coordinates.
     * @param pos
     * @return
     */
    public static @NotNull Vec3i doubleToBlockCoords(@NotNull Vec3d pos) {
        int x = pos.x < 0 ? (int)(Math.floor(pos.x) - 0.1) : (int) pos.x;
        int y = pos.y < 0 ? (int)(Math.floor(pos.y) - 0.1) : (int) pos.y;
        int z = pos.z < 0 ? (int)(Math.floor(pos.z) - 0.1) : (int) pos.z;
        return new Vec3i(x, y, z);
    }
}
