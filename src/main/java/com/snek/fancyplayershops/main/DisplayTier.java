package com.snek.fancyplayershops.main;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.debug.Require;




public enum DisplayTier {
    T1(0, "Basic",            8l,     0,   0, "e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTkwYmI1ZWNmMzMxYTJjYTA2ODNlNzM4ZjA3Mjc2OWQzNmFhN2EzZmYzNzBkNjliMTQzN2Q2NjczNzVhY2MxZCJ9fX0"),
    T2(1, "Advanced",      1024l,     0,   0, "e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDI0N2U4N2M5MDBiZGEwOGNhMDZjOGU5MDg2MWUwODU5NzRkMTg0MmMxYmVkYTJmOGFiYjliZmIyNjM5MjE1YiJ9fX0"),
    T3(2, "Vault",        16384l,     4,   0, "e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2YzOWU3ZGZiMzk3YjYyODQ1ZDQwNmU1ZGE4ODA1NmM0ZTFlZDllYjY0ZTJiYjBmZmQ2NDk5ODZhZDhiNDg0OSJ9fX0"),
    T4(3, "Industrial",  262144l,   128,  32, "e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjk2NDE2MDIyMTI5ODY3YzAwOGM5ODgxNWQ3NDlkMjk4ZjU4NmRkZTRiOWFlODk1ZDdiMGJjNDk4NjRhNzA0YiJ9fX0"),
    T5(4, "Quantum",    4194304l, 16384, 256, "e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTBiNzQ2YTc2YzI4MjU4NWNiODU0ZDRiMzQ3ZTdlZjcxZTI2MzUxNDU4OTcyNzRiNWNlZjJiNWFjZTFmM2NiNiJ9fX0");


    // Basic data
    private final int index;
    private final @NotNull String name;
    private final long capacity;
    private final @NotNull String texture;

    // Features
    private final int restockSpeed;
    private final int wirelessDistance;




    private DisplayTier(
        final int index, final @NotNull String name, final long capacity,
        final int restockSpeed, final int wirelessDistance,
        final @NotNull String texture
    ) {
        this.index = index;
        this.name = name;
        this.capacity = capacity;
        this.texture = texture;
        this.restockSpeed = restockSpeed;
        this.wirelessDistance = wirelessDistance;
    }

    public static @NotNull DisplayTier fromIndex(final int index) {
        assert Require.inRange(index, 0, values().length - 1, "tier index");
        return switch(index) {
            case 0  -> T1;
            case 1  -> T2;
            case 2  -> T3;
            case 3  -> T4;
            default -> T5;
        };
    }




    public @NotNull String getName() {
        return name;
    }

    public long getCapacity() {
        return capacity;
    }

    public @NotNull String getTexture() {
        return texture;
    }

    public int getIndex() {
        return index;
    }

    public static @NotNull DisplayTier getHighestTier() {
        return fromIndex(values().length - 1);
    }

    public static long getHighestCapacity() {
        return getHighestTier().getCapacity();
    }

    //TODO implement restock speed limits
    public int getRestockSpeed() {
        return restockSpeed;
    }

    //TODO implement wireless restocking
    public int getWirelessDistance() {
        return wirelessDistance;
    }




    /**
     * Calculates the ID of the crafting recipe of the product display item of this tier.
     * <p>
     * This doesn't include the namespace.
     * @return The ID of this tier's crafting recipe.
     */
    public @NotNull String getId() {
        return "product_display_" + name().toLowerCase();
    }
}