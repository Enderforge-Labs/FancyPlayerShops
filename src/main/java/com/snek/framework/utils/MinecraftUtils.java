package com.snek.framework.utils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.serialization.JsonOps;

import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.LingeringPotionItem;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.phys.Vec3;
















/**
 * A utility class providing functions to handle Minecraft classes and data.
 */
public abstract class MinecraftUtils {
    private MinecraftUtils() {}

    public static final @NotNull UUID HEAD_OWNER_UUID = UUID.fromString("e58d5427-a51e-4ea5-9938-20fa7bd90e52");








    /**
     * Checks if any of the keys or string values of a Tag contain the provided substring, recursively.
     * @param tag The tag to check.
     * @param substring The substring to look for.
     * @return True if tag contains the substring anywhere in its tree, false otherwise.
     */
    public static boolean nbtContainsSubstring(Tag tag, String substring) {
        if(tag == null) return false;


        if(tag instanceof CompoundTag c) {
            for(String key : c.getAllKeys()) {
                if(key.contains(substring)) return true;
                if(nbtContainsSubstring(c.get(key), substring)) return true;
            }
        }
        else if(tag instanceof ListTag l) {
            for(Tag e : l) {
                if(nbtContainsSubstring(e, substring)) return true;
            }
        }
        else if(tag instanceof StringTag s) {
            if(s.getAsString().contains(substring)) return true;
        }
        return false;
    }








    /**
     * Calculates the UUID of an ItemStack.
     * <p> This can be used to identify item stacks that can merge. Mergeable stacks always have the same UUID,
     *     while those that cannot be merged have a different one.
     * @param stack The item stack.
     * @return The UUID.
     */
    public static UUID calcItemUUID(ItemStack stack) {
        final CompoundTag nbt = stack.getTag();

        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString().getBytes(StandardCharsets.UTF_8));
            if(nbt != null) {
                digest.update(nbt.toString().getBytes(StandardCharsets.UTF_8));
            }
            final byte[] hash = digest.digest();
            final ByteBuffer buffer = ByteBuffer.wrap(Arrays.copyOf(hash, 16));
            return new UUID(buffer.getLong(), buffer.getLong());
        } catch(final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }








    /**
     * Computes the serialized form of an ItemStack.
     * @return The serialized ItemStack as a String.
     * @throws RuntimeException if the item cannot be serialized.
     */
    public static @NotNull String serializeItem(final @NotNull ItemStack item) {
        final var result = ItemStack.CODEC.encode(item, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).result();
        if(result.isEmpty()) {
            throw new RuntimeException("Could not serialize shop item");
        }
        return result.get().toString();
    }




    /**
     * Computes the ItemStack form of a serialized item.
     * @return The deserialized ItemStack.
     * @throws RuntimeException if the item cannot be deserialized.
     */
    public static @NotNull ItemStack deserializeItem(final @NotNull String serializedItem) {
        final var result = ItemStack.CODEC.decode(JsonOps.INSTANCE, JsonParser.parseString(serializedItem)).result();
        if(result.isEmpty()) {
            throw new RuntimeException("Could not deserialize shop item");
        }
        return result.get().getFirst();
    }







    /**
     * Attempts to give an item to a player.
     * @param player The player.
     * @param item The item to give.
     * <p> Partial insertions modify the count of the item stack to indicate the amount of items that didn't fit in the inventory.
     * @return True if all of the items could be given to the player, false othersise (not enough space in inventory).
     */
    public static boolean attemptGive(final @NotNull Player player, final @NotNull ItemStack item) {
        return player.getInventory().add(item);
    }








    /**
     * Returns an ItemStack containing a player head with texture the specified skin ID.
     * @param skin The Base-64 skin ID.
     * @return The head as an ItemStack of count 1.
     */
    public static @NotNull ItemStack createCustomHead(final @NotNull String skin) {

        // Create the texture list NBT using the provided Base64 texture ID
        final CompoundTag NBT_texture = new CompoundTag();
        NBT_texture.putString("Value", skin);
        final ListTag NBT_textures = new ListTag();
        NBT_textures.add(NBT_texture);

        // Create the properties NBT
        final CompoundTag NBT_properties = new CompoundTag();
        NBT_properties.put("textures", NBT_textures);

        // Create the skullOwner NBT using a hard-coded UUID and the properties NBT
        //! A UUID is required for heads to display the custom texture, even if it's invalid.
        final CompoundTag NBT_skullOwner = new CompoundTag();
        NBT_skullOwner.putUUID("Id", HEAD_OWNER_UUID);
        NBT_skullOwner.put("Properties", NBT_properties);

        // Create the ItemStack and return it
        final ItemStack head = new ItemStack(Items.PLAYER_HEAD, 1);
        head.getOrCreateTag().put("SkullOwner", NBT_skullOwner);
        return head;
    }








    /**
     * Returns an ItemStack containing a player head with owner the player that matches the specified UUID.
     * <p> The player can be offline, as long as they have joined the server at least once in the past.
     * @param uuid The player's UUID.
     * @param server The server instance.
     * @return The player's head as an ItemStack of count 1, or null if the player never joined this server.
     */
    public static @Nullable ItemStack getOfflinePlayerHead(final @NotNull UUID uuid, final @NotNull MinecraftServer server) {

        // Fetch player profile cache
        final Optional<GameProfile> profile = server.getProfileCache().get(uuid);
        if(profile.isEmpty()) return null;
        final GameProfile gp = profile.get();

        // Create the texture list NBT using the provided Base64 texture ID
        final ListTag NBT_textures = new ListTag();
        for(Property property : gp.getProperties().get("textures")) {
            final CompoundTag NBT_texture = new CompoundTag();
            NBT_texture.putString("Value", property.getValue());
            NBT_textures.add(NBT_texture);
        }

        // Create the properties NBT
        final CompoundTag NBT_properties = new CompoundTag();
        NBT_properties.put("textures", NBT_textures);

        // Create SkullPwner NBT tag
        final CompoundTag NBT_skullOwner = new CompoundTag();
        NBT_skullOwner.putUUID("Id", gp.getId());
        NBT_skullOwner.put("Properties", NBT_properties);

        // Create ItemStack with the retrieved data
        final ItemStack head = new ItemStack(Items.PLAYER_HEAD);
        head.getOrCreateTag().put("SkullOwner", NBT_skullOwner);
        return head;
    }
    //FIXME skins of offline players aren't actually retrieved because the texture data is not loaded.
    //FIXME Caching it manually might be necessary. if cached, use createCustomHead with the base-64 texture value instead of the player cache








    /**
     * Returns the name of the player that matches the specified UUID.
     * <p> The player can be offline, as long as they have joined the server at least once in the past.
     * @param uuid The player's UUID.
     * @param server The server instance.
     * @return The player's name as a string, or null if the player never joined this server.
     */
    public static @Nullable String getOfflinePlayerName(final @NotNull UUID uuid, final @NotNull MinecraftServer server) {
        final Optional<GameProfile> profile = server.getProfileCache().get(uuid);
        if(profile.isEmpty()) return null;
        return profile.get().getName();
    }








    /**
     * Plays a sound on the client of the specified player.
     * <p> Other players won't be able to hear it.
     * @param player The player.
     * @param sound The sound to play.
     * @param volume The sound's volume.
     * @param pitch The sound's pitch.
     */
    public static void playSoundClient(final @NotNull Player player, final @NotNull SoundEvent sound, final float volume, final float pitch) {
        ((ServerPlayer)player).connection.send(
            new ClientboundSoundPacket(
                Holder.direct(sound),
                SoundSource.BLOCKS,
                player.getX(), player.getY(), player.getZ(),
                volume, pitch,
                player.level().getRandom().nextLong()
            )
        );
    }




    /**
     * Sends a slot update to the client of the specified player.
     * @param player The player.
     * @param slot The ID of the slot to update.
     * @param stack The item stack to set.
     */
    public static void sendClientSlotUpdate(final @NotNull Player player, final int slot, final @NotNull ItemStack stack) {
        ((ServerPlayer)player).connection.send(new ClientboundContainerSetSlotPacket(
            InventoryMenu.CONTAINER_ID,
            player.containerMenu.getStateId(),
            slot,
            stack
        ));
    }








    /**
     * Returns the custom name of an item. If the item has no custom name, the default name is returned.
     * <p> Potions and Tipped Arrows include the first of their effects.
     * <p> Enchanted Books include the first of their enchantments.
     * <p> Music Discs, Banner Patterns, Monster Spawners and Smithing templates show their type.
     * @param item The item.
     * @return The name of the item.
     */
    public static @NotNull Component getFancyItemName(final @NotNull ItemStack item) {

        // Custom names
        if(item.hasCustomHoverName()) {
            return getItemName(item);
        }


        // Spawners
        if(item.getItem() == Items.SPAWNER && item.hasTag()) {
            final CompoundTag nbt = item.getTag();
            if(nbt.contains("BlockEntityTag", Tag.TAG_COMPOUND)) {
                final CompoundTag blockTag = nbt.getCompound("BlockEntityTag");
                if(blockTag.contains("SpawnData", Tag.TAG_COMPOUND)) {
                    final CompoundTag spawnData = blockTag.getCompound("SpawnData");
                    if(spawnData.contains("entity", Tag.TAG_COMPOUND)) {
                        final CompoundTag entity = spawnData.getCompound("entity");
                        final ResourceLocation id = new ResourceLocation(entity.getString("id"));
                        final EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(id);
                        return new Txt().cat(entityType.getDescription()).cat(new Txt(" Spawner").white()).get();
                    }
                }
            }
            //! Fallback to default name
        }


        // Potions
        else if(item.getItem() instanceof PotionItem e) {
            final Potion potion = PotionUtils.getPotion(item);
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
                final List<MobEffectInstance> effects = potion.getEffects();
                if(effects.isEmpty()) {
                    return new Txt(prefix + " Potion").get();
                }
                final MobEffect effect = effects.get(0).getEffect();
                if(effects.size() == 1) {
                    return new Txt(prefix + " Potion of ").cat(effect.getDisplayName()).get();
                }
                else {
                    return new Txt(prefix + " Potion of ").cat(effect.getDisplayName()).cat(new Txt(" & " + (effects.size() - 1) + " more").white()).get();
                }
            }
        }


        // Player heads
        else if(item.getItem() instanceof PlayerHeadItem e) {
            return new Txt(e.getName(item)).white().get();
        }


        // Fallback
        return getItemName(item);
    }








    /**
     * Returns the custom name of an item. If the item has no custom name, the default name is returned.
     * @param item The item.
     * @return The name of the item.
     */
    public static @NotNull Component getItemName(final @NotNull ItemStack item) {

        // Custom names
        if(item.hasCustomHoverName()) {
            final Component name = item.getHoverName();
            return name.getStyle().getColor() == null ? new Txt(name).white().get() : name;
        }

        // Fallback
        return new Txt(item.getItem().getName(item)).white().get();
    }








    /**
     * Converts entity coordinates (double) to block coordinates (int).
     * <p> Minecraft's block grid is weird and simply truncating the decimal part is not enough to convert coordinates.
     * @param pos
     * @return
     */
    public static @NotNull Vec3i doubleToBlockCoords(final @NotNull Vec3 pos) {
        return new Vec3i(
            pos.x < 0 ? (int)(Math.floor(pos.x) - 0.1) : (int) pos.x,
            pos.y < 0 ? (int)(Math.floor(pos.y) - 0.1) : (int) pos.y,
            pos.z < 0 ? (int)(Math.floor(pos.z) - 0.1) : (int) pos.z
        );
    }
}
