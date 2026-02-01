package com.snek.fancyplayershops.data.display;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;






public class ProductDisplay_Key {
    private final int x;
    private final int y;
    private final int z;
    private final @NotNull Level level;
    private final @NotNull UUID uuid;


    public ProductDisplay_Key(final @NotNull BlockPos _pos, final @NotNull Level level) {
        x = _pos.getX();
        y = _pos.getY();
        z = _pos.getZ();
        this.level = level;
        this.uuid = calculateUUID();
    }


    @Override
    public boolean equals(final Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(obj instanceof final ProductDisplay_Key other) {
            return
                x == other.x &&
                y == other.y &&
                z == other.z &&
                level.equals(other.level)
            ;
        }
        return false;
    }


    @Override
    public int hashCode() {
        int result = Integer.hashCode(x);
        result = 31 * result + Integer.hashCode(y);
        result = 31 * result + Integer.hashCode(z);
        result = 31 * result + level.hashCode();
        return result;
    }


    /**
     * Calculates a deterministic UUID based on this key's position and level.
     */
    public @NotNull UUID calculateUUID() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // Add dimension key bytes
            String dimensionKey = level.dimension().location().toString();
            md.update(dimensionKey.getBytes(StandardCharsets.UTF_8));

            // Add coordinates bytes
            ByteBuffer buffer = ByteBuffer.allocate(12);
            buffer.putInt(x);
            buffer.putInt(y);
            buffer.putInt(z);
            md.update(buffer.array());

            byte[] hash = md.digest();

            // Convert first 16 bytes to UUID (version 5 format)
            long msb = 0;
            long lsb = 0;

            for(int i = 0; i < 8; i++) {
                msb = (msb << 8) | (hash[i] & 0xffL);
            }
            for(int i = 8; i < 16; i++) {
                lsb = (lsb << 8) | (hash[i] & 0xffL);
            }

            // Set version to 5 (clear bits 48-51, then set to 0101)
            msb &= 0xFFFFFFFFFFFF0FFFL;  // Clear version bits
            msb |= 0x0000000000005000L;  // Set to version 5

            // Set variant to 2 (10x in binary for RFC 4122)
            lsb &= 0x3FFFFFFFFFFFFFFFL;  // Clear variant bits
            lsb |= 0x8000000000000000L;  // Set variant to 10

            return new UUID(msb, lsb);
        }
        catch(NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 algorithm not available", e);
        }
    }


    public @NotNull UUID getUUID() {
        return uuid;
    }
}
