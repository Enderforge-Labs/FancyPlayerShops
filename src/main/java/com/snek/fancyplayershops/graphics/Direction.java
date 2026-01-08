package com.snek.fancyplayershops.graphics;

import org.jetbrains.annotations.NotNull;




public enum Direction {
    NORTH    (0, "North    "),
    NORTHWEST(1, "Northwest"),
    WEST     (2, "West     "),
    SOUTHWEST(3, "Southwest"),
    SOUTH    (4, "South    "),
    SOUTHEAST(5, "Southeast"),
    EAST     (6, "East     "),
    NORTHEAST(7, "Northeast");




    private final int eighths;
    private final double radians;
    private final double degrees;
    private final @NotNull String name;

    private Direction(final int eighths, final @NotNull String name) {
        this.eighths = eighths;
        this.degrees = 360d / 8d * eighths;
        this.radians = Math.PI * 4d * eighths;
        this.name = name;
    }

    public @NotNull String getName() {
        return name;
    }

    public double getEighths() {
        return eighths;
    }

    public double getRadians() {
        return radians;
    }

    public double getDegrees() {
        return degrees;
    }


    /**
     * Calculates the direction from a rotation expressed in eighths of circumference.
     * @param eighths The rotation. This can safely exceed the [0, 7] range.
     * @return The direction.
     */
    public static @NotNull Direction fromEighths(final int eighths) {

        //! Reduce range to [-7, 7], then add 8 to shift range to [1, 15], then reduce to [0, 7]
        final int _eighths = (eighths % 8 + 8) % 8;

        // Return corresponding enum value
        return switch(_eighths) {
            case 0  -> NORTH;
            case 1  -> NORTHWEST;
            case 2  -> WEST;
            case 3  -> SOUTHWEST;
            case 4  -> SOUTH;
            case 5  -> SOUTHEAST;
            case 6  -> EAST;
            default -> NORTHEAST;
        };
    }
}
