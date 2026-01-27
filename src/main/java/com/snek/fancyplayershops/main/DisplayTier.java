package com.snek.fancyplayershops.main;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.utils.Utils;




public enum DisplayTier {

    // Craftable tiers
    T1(0, "Basic",            8l,     0,   0, "e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTkwYmI1ZWNmMzMxYTJjYTA2ODNlNzM4ZjA3Mjc2OWQzNmFhN2EzZmYzNzBkNjliMTQzN2Q2NjczNzVhY2MxZCJ9fX0"),
    T2(1, "Advanced",      1024l,     0,   0, "e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDI0N2U4N2M5MDBiZGEwOGNhMDZjOGU5MDg2MWUwODU5NzRkMTg0MmMxYmVkYTJmOGFiYjliZmIyNjM5MjE1YiJ9fX0"),
    T3(2, "Vault",        16384l,     4,   0, "e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2YzOWU3ZGZiMzk3YjYyODQ1ZDQwNmU1ZGE4ODA1NmM0ZTFlZDllYjY0ZTJiYjBmZmQ2NDk5ODZhZDhiNDg0OSJ9fX0"),
    T4(3, "Industrial",  262144l,   128,  32, "e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjk2NDE2MDIyMTI5ODY3YzAwOGM5ODgxNWQ3NDlkMjk4ZjU4NmRkZTRiOWFlODk1ZDdiMGJjNDk4NjRhNzA0YiJ9fX0"),
    T5(4, "Quantum",    4194304l, 16384, 256, "e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTBiNzQ2YTc2YzI4MjU4NWNiODU0ZDRiMzQ3ZTdlZjcxZTI2MzUxNDU4OTcyNzRiNWNlZjJiNWFjZTFmM2NiNiJ9fX0"),


    // Creative tier
    //! Maximum capacity can NEVER exceed the highest tier's capacity.
    //! This is because the max transaction cost is evaluated using it and not the special CREATIVE tier.
    //! Creative shops auto restock instantly, so there is no need for the capacity to be high.
    CREATIVE(999, "Creative", Long.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, "e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTBiNzQ2YTc2YzI4MjU4NWNiODU0ZDRiMzQ3ZTdlZjcxZTI2MzUxNDU4OTcyNzRiNWNlZjJiNWFjZTFmM2NiNiJ9fX0");
    //FIXME make creative tier auto restock.
    //FIXME set its default max stock to 1 million




    // Basic data
    private final int numericalId;
    private final @NotNull String name;
    private final long capacity;
    private final @NotNull String texture;

    // Features
    private final int restockSpeed;
    private final int wirelessDistance;
    private @NotNull List<@NotNull String> statsLines = new ArrayList<>();




    private DisplayTier(
        final int numericalId, final @NotNull String name, final long capacity,
        final int restockSpeed, final int wirelessDistance,
        final @NotNull String texture
    ) {
        this.numericalId = numericalId;
        this.name = name;
        this.capacity = capacity;
        this.texture = texture;
        this.restockSpeed = restockSpeed;
        this.wirelessDistance = wirelessDistance;


        statsLines.add(getName() + " tier");
        if(getNumericalId() == 999) {
            statsLines.add("Max capacity: "        + "Unlimited");
            statsLines.add("Restocking: "    + "Unlimited");
            statsLines.add("Distance: " + "Unlimited");
        }
        else {
            final String restockSpeedString = getRestockSpeed()     == 0 ? "No automatic restocking" : "Restocking: " + Utils.formatAmount(getRestockSpeed()) + "/cycle";
            final String wirelessDistString = getWirelessDistance() == 0 ? "No wireless restocking"  : "Distance: "   + Utils.formatAmount(getWirelessDistance()) + " blocks";
            statsLines.add("Max capacity: " + Utils.formatAmount(getCapacity()));
            statsLines.add(restockSpeedString);
            statsLines.add(wirelessDistString);
        }
    }




    public static @NotNull DisplayTier fromNumericalId(final int numericalId) {
        switch(numericalId) {
            case 0:   return T1;
            case 1:   return T2;
            case 2:   return T3;
            case 3:   return T4;
            case 4:   return T5;
            case 999: return CREATIVE;
            default: {
                assert Require.fail("Invalid tier index: " + numericalId);
                return T1; //! Never actually called
            }
        }
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

    public int getNumericalId() {
        return numericalId;
    }

    public static @NotNull DisplayTier getHighestTier() {
        return values()[values().length - 2];
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

    public @NotNull List<@NotNull String> getStatsLines() {
        return statsLines;
    }




    /**
     * Calculates the ID of the crafting recipe of the product display item of this tier.
     * <p>
     * This is equal to the lowercase name of the tier, preceded by {@code "product_display_"}.
     * <p>
     * This doesn't include the namespace.
     * @return The ID of this tier's crafting recipe.
     */
    public @NotNull String getId() {
        return "product_display_" + name().toLowerCase();
    }
}