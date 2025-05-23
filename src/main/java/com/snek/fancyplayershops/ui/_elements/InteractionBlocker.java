package com.snek.fancyplayershops.ui._elements;

import java.lang.reflect.Method;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;
import com.snek.framework.utils.scheduler.Scheduler;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;








/**
 * A special interaction entity that is used to block unwanted players interactions.
 * <p> This stops client-side clicks on blocks and entities behind the targeted UI
 * <p> and client-side item use events, preventing annoying UI flashes and visual artifacts.
 */
public class InteractionBlocker {

    // Despawn delay, avoids accidental clicks after the UI is removed
    public static final int DESPAWN_DELAY = 10;


    // Private methods of InteractionEntity
    private static @NotNull Method method_setWidth;
    private static @NotNull Method method_setHeight;
    static {
        try {
            method_setWidth  = Interaction.class.getDeclaredMethod("setWidth",  float.class);
            method_setHeight = Interaction.class.getDeclaredMethod("setHeight", float.class);
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        method_setWidth.setAccessible(true);
        method_setHeight.setAccessible(true);
    }


    // Entity names and command source identifier
    public static final String ENTITY_CUSTOM_NAME               = FancyPlayerShops.MOD_ID + ".ui.interactionblocker";
    public static final String ENTITY_CUSTOM_NAME_UNINITIALIZED = ENTITY_CUSTOM_NAME + ".uninitialized";
    public static final String COMMAND_SOURCE_NAME              = ENTITY_CUSTOM_NAME + ".patch";


    // In-world data
    private final @NotNull Interaction entity;
    private final @NotNull Level world;




    /**
     * Creates a new InteractionBlocker.
     * @param _world The world to create this interaction in.
     */
    public InteractionBlocker(final @NotNull Level _world, final float w, final float h) {
        world = _world;
        entity = new Interaction(EntityType.INTERACTION, world);
        Utils.invokeSafe(method_setWidth,  entity, w);
        Utils.invokeSafe(method_setHeight, entity, h);
    }




    /**
     * Checks for stray interaction entities and purges them.
     * <p> Must be called on entity load event.
     * @param entity The entity.
     */
    public static void onEntityLoad(@NotNull Entity entity) {
        if(entity instanceof Interaction) {
            final Level world = entity.level();
            if(
                world != null &&
                entity.hasCustomName() &&
                entity.getCustomName().getString().equals(ENTITY_CUSTOM_NAME)
            ) {
                entity.remove(RemovalReason.KILLED);
            }
        }
    }




    /**
     * Spawns the interaction entity into the world.
     * @param pos The coordinates at which to spawn the entity.
     */
    public void spawn(final @NotNull Vector3d pos) {

        // Spawn the entity, move it to the specified coords and set a temporary name to allow the command to recognize it
        world.addFreshEntity(entity);
        entity.setPos(pos.x, pos.y, pos.z);
        entity.setCustomNameVisible(false);
        entity.setCustomName(new Txt(ENTITY_CUSTOM_NAME_UNINITIALIZED).get());


        //!  ╭────────────────────────────────────────────────────────────────────────────────────────────────────────────────╮
        //!  │                                                                                                              //!
        //!  │                        Manually update the entity using a vanilla data modify command.                       //!
        //!  │                                Use a custom source to silence command feedback.                              //!
        //!  │                                                                                                              //!
        //!  │            This workaround is used to fix a 1.20.1 issue that makes interactions spawned by the mod          //!
        //!  │                        unable to detect player clicks until their nbt data is modified.                      //!
        //!  │                                                                                                              //!
        //!  │                                                                                                              //!
        /*!  │    // Create the custom command source and use DUMMY as output to silence it                                 //!
        /*!  │  */final MinecraftServer server = FancyPlayerShops.getServer();                                              //!
        /*!  │  */final ServerLevel world = (ServerLevel)entity.level();                                                    //!
        /*!  │  */final CommandSourceStack source = new CommandSourceStack(                                                 //!
        /*!  │  */    CommandSource.NULL, Vec3.ZERO, Vec2.ZERO, world,                                                      //!
        /*!  │  */    4, COMMAND_SOURCE_NAME, new Txt(COMMAND_SOURCE_NAME).get(), server, (Entity)null                      //!
        /*!  │  */);                                                                                                        //!
        //!  │                                                                                                              //!
        //!  │                                                                                                              //!
        /*!  │    // Execute the command using the custom command source                                                    //!
        /*!  │  */try {                                                                                                     //!
        /*!  │  */    server.getCommands().getDispatcher().execute(                                                         //!
        /*!  │  */        "execute as @e[type=minecraft:interaction,name=" + ENTITY_CUSTOM_NAME_UNINITIALIZED + "] " +      //!
        /*!  │  */        "run data modify entity @s Air set value 1000",                                                   //!
        /*!  │  */        source                                                                                            //!
        /*!  │  */    );                                                                                                    //!
        /*!  │  */} catch (CommandSyntaxException e) {                                                                      //!
        /*!  │  */    e.printStackTrace();                                                                                  //!
        /*!  │  */}                                                                                                         //!
        //!  │                                                                                                              //!
        //!  ╰────────────────────────────────────────────────────────────────────────────────────────────────────────────────╯


        // Replace the temporary name with the actual custom name. This name is the one that will be used to purge stray interactions
        entity.setCustomName(new Txt(ENTITY_CUSTOM_NAME).get());
    }




    /**
     * Teleports the interaction entity to the specified position.
     * @param pos The position.
     */
    public void teleport(final @NotNull Vector3d pos) {
        entity.teleportTo(pos.x, pos.y, pos.z);
    }




    /**
     * Removes the interaction entity from the world
     */
    public void despawn() {
        Scheduler.schedule(DESPAWN_DELAY, () -> {
            entity.remove(RemovalReason.KILLED);
        });
    }
}
