package com.snek.fancyplayershops.main;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;








public class ProductDisplayKey {
    private final int x;
    private final int y;
    private final int z;
    private final @NotNull Level level;


    public ProductDisplayKey(final @NotNull BlockPos _pos, final @NotNull Level level) {
        x = _pos.getX();
        y = _pos.getY();
        z = _pos.getZ();
        this.level = level;
    }


    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(obj instanceof ProductDisplayKey other) {
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
}
