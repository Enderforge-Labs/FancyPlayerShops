package com.snek.framework.utils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.item.PotionItem;
import net.minecraft.item.SkullItem;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Identifier;
import net.minecraft.util.UserCache;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
















/**
 * A utility class providing functions to handle Minecraft classes and data.
 */
public abstract class MinecraftUtils {
    private MinecraftUtils() {}

    public static final UUID HEAD_OWNER_UUID = UUID.fromString("e58d5427-a51e-4ea5-9938-20fa7bd90e52");








    /**
     * Returns an ItemStack containing a player head with texture the specified skin ID.
     * @param skin The Base-64 skin ID.
     * @return The head as an ItemStack of count 1.
     */
    public static ItemStack createCustomHead(String skin) {

        // Create the texture list NBT using the provided Base64 texture ID
        NbtCompound NBT_texture = new NbtCompound();
        NBT_texture.putString("Value", skin);
        NbtList NBT_textures = new NbtList();
        NBT_textures.add(NBT_texture);

        // Create the properties NBT
        NbtCompound NBT_properties = new NbtCompound();
        NBT_properties.put("textures", NBT_textures);

        // Create the skullOwner NBT using a hard-coded UUID and the properties NBT
        //! A UUID is required for heads to display the custom texture, even if it's invalid.
        NbtCompound NBT_skullOwner = new NbtCompound();
        NBT_skullOwner.putUuid("Id", HEAD_OWNER_UUID);
        NBT_skullOwner.put("Properties", NBT_properties);

        // Create the ItemStack and return it
        ItemStack head = new ItemStack(Items.PLAYER_HEAD, 1);
        head.getOrCreateNbt().put("SkullOwner", NBT_skullOwner);
        return head;
    }








    /**
     * Returns an ItemStack containing a player head with owner the player that matches the specified UUID.
     *     The player can be offline, as long as they have joined the server at least once in the past.
     * @param uuid The player's UUID.
     * @param server The server instance.
     * @return The player's head as an ItemStack of count 1, or null if the player never joined this server.
     */
    public static @Nullable ItemStack getOfflinePlayerHead(@NotNull UUID uuid, @NotNull MinecraftServer server) {

        // Fetch player profile cache
        final Optional<GameProfile> profile = server.getUserCache().getByUuid(uuid);
        if(profile.isEmpty()) return null;
        final NbtCompound ownerTag = NbtHelper.writeGameProfile(new NbtCompound(), profile.get());

        // Create ItemStack with the retrieved data
        ItemStack head = new ItemStack(Items.PLAYER_HEAD);
        head.getOrCreateNbt().put("SkullOwner", ownerTag);
        return head;
    }
    //FIXME skins of offline players aren't actually retrieved because the texture data is not loaded.
    //FIXME Caching it manually might be necessary. if cached, use createCustomHead with the base-64 texture value instead of the player cache








    /**
     * Returns the name of the player that matches the specified UUID.
     *     The player can be offline, as long as they have joined the server at least once in the past.
     * @param uuid The player's UUID.
     * @param server The server instance.
     * @return The player's name as a string, or null if the player never joined this server.
     */
    public static @Nullable String getOfflinePlayerName(@NotNull UUID uuid, @NotNull MinecraftServer server) {
        Optional<GameProfile> profile = server.getUserCache().getByUuid(uuid);
        if(profile.isEmpty()) return null;
        return profile.get().getName();
    }








    /**
     * Plays a sound on the client of the specified player.
     * Other players won't be able to hear it.
     * @param player The player.
     * @param sound The sound to play.
     * @param volume The sound's volume.
     * @param pitch The sound's pitch.
     */
    public static void playSoundClient(@NotNull PlayerEntity player, @NotNull SoundEvent sound, float volume, float pitch) {
        ((ServerPlayerEntity) player).networkHandler.sendPacket(
            new PlaySoundS2CPacket(
                RegistryEntry.of(sound),
                SoundCategory.BLOCKS,
                player.getX(), player.getY(), player.getZ(),
                volume, pitch, player.getWorld().getRandom().nextLong()
            )
        );
    }








    /**
     * Returns the custom name of an item. If the item has no custom name, the default name is returned.
     *     Potions and Tipped Arrows include the first of their effects.
     *     Enchanted Books include the first of their enchantments.
     *     Music Discs, Banner Patterns, Monster Spawners and Smithing templates show their type.
     * @param item The item.
     * @return The name of the item.
     */
    public static @NotNull Text getFancyItemName(@NotNull ItemStack item) {

        // Custom names
        if(item.hasCustomName()) {
            return item.getName();
        }


        // Spawners
        if(item.getItem() == Items.SPAWNER && item.hasNbt()) {
            NbtCompound nbt = item.getNbt();
            if(nbt.contains("BlockEntityTag", NbtElement.COMPOUND_TYPE)) {
                NbtCompound blockTag = nbt.getCompound("BlockEntityTag");
                if(blockTag.contains("SpawnData", NbtElement.COMPOUND_TYPE)) {
                    NbtCompound spawnData = blockTag.getCompound("SpawnData");
                    if(spawnData.contains("entity", NbtElement.COMPOUND_TYPE)) {
                        NbtCompound entity = spawnData.getCompound("entity");
                        Identifier id = new Identifier(entity.getString("id"));
                        EntityType<?> entityType = Registries.ENTITY_TYPE.get(id);
                        return new Txt().cat(entityType.getName()).cat(new Txt(" Spawner").white()).get();
                    }
                }
            }
            //! Fallback to default name
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
     * Returns the custom name of an item. If the item has no custom name, the default name is returned.
     * @param item The item.
     * @return The name of the item.
     */
    public static @NotNull Text getItemName(@NotNull ItemStack item) {

        // Custom names
        if(item.hasCustomName()) {
            return item.getName();
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
