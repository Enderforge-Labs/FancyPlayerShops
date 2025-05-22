package com.snek.fancyplayershops.main;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;








public class ShopKey {
    private final int x;
    private final int y;
    private final int z;
    private final @NotNull Level world;


    public ShopKey(final @NotNull BlockPos _pos, final @NotNull Level _world) {
        x = _pos.getX();
        y = _pos.getY();
        z = _pos.getZ();
        world = _world;
    }


    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(obj instanceof ShopKey other) {
            return
                x == other.x &&
                y == other.y &&
                z == other.z &&
                world.equals(other.world)
            ;
        }
        return false;
    }


    @Override
    public int hashCode() {
        int result = Integer.hashCode(x);
        result = 31 * result + Integer.hashCode(y);
        result = 31 * result + Integer.hashCode(z);
        result = 31 * result + world.hashCode();
        return result;
    }
}
