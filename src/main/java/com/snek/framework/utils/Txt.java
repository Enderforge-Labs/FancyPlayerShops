package com.snek.framework.utils;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;
import org.joml.Vector4i;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;







/**
 * A simpler but more readable minecraft.text.MutableText.
 * <p> Use .get() to create a MutableText from this object's data.
 */
public class Txt {
    private MutableComponent rawText;
    private Style style;




    /**
     * Creates a new empty Txt.
     */
    public Txt() {
        rawText = Component.empty();
        style = Style.EMPTY;
    }

    /**
     * Creates a new Txt using a String content.
     * @param s The String.
     */
    public Txt(final @NotNull String s) {
        rawText = Component.literal(s);
        style = Style.EMPTY;
    }

    /**
     * Creates a new Txt using a copy of a MutableText as content.
     * @param s The MutableText.
     */
    public Txt(final @NotNull MutableComponent s) {
        rawText = s.copy();
        style = rawText.getStyle();
    }

    /**
     * Creates a new Txt using a copy of a Component as content.
     * @param s The Component.
     */
    public Txt(final @NotNull Component s) {
        rawText = s.copy();
        style = rawText.getStyle();
    }




    /**
     * Creates a copy of this Txt.
     * @return A copy of this Txt.
     */
    public @NotNull Txt copy() {
        rawText.setStyle(style);
        return new Txt(rawText.copy());
    }



    public static final @NotNull Vector3i COLOR_BLACK      = new Vector3i(  0,   0,   0);
    public static final @NotNull Vector3i COLOR_BLUE       = new Vector3i(  0,   0, 170);
    public static final @NotNull Vector3i COLOR_GREEN      = new Vector3i(  0, 170,   0);
    public static final @NotNull Vector3i COLOR_AQUA       = new Vector3i(  0, 170, 170);
    public static final @NotNull Vector3i COLOR_DARKRED    = new Vector3i(170,   0,   0);
    public static final @NotNull Vector3i COLOR_PURPLE     = new Vector3i(170,   0, 170);
    public static final @NotNull Vector3i COLOR_GOLD       = new Vector3i(255, 170,   0);
    public static final @NotNull Vector3i COLOR_LIGHTGRAY  = new Vector3i(170, 170, 170);
    public static final @NotNull Vector3i COLOR_GRAY       = new Vector3i( 85,  85,  85);
    public static final @NotNull Vector3i COLOR_LIGHTBLUE  = new Vector3i( 85,  85, 255);
    public static final @NotNull Vector3i COLOR_LIME       = new Vector3i( 85, 255,  85);
    public static final @NotNull Vector3i COLOR_CYAN       = new Vector3i( 85, 255, 255);
    public static final @NotNull Vector3i COLOR_RED        = new Vector3i(255,  85,  85);
    public static final @NotNull Vector3i COLOR_MAGENTA    = new Vector3i(255,  85, 255);
    public static final @NotNull Vector3i COLOR_YELLOW     = new Vector3i(255, 255,  85);
    public static final @NotNull Vector3i COLOR_WHITE      = new Vector3i(245, 245, 245);
    public static final @NotNull Vector3i COLOR_PURE_WHITE = new Vector3i(255, 255, 255);


    public @NotNull Txt black    () { return color(COLOR_BLACK     ); }
    public @NotNull Txt blue     () { return color(COLOR_BLUE      ); }
    public @NotNull Txt green    () { return color(COLOR_GREEN     ); }
    public @NotNull Txt aqua     () { return color(COLOR_AQUA      ); }
    public @NotNull Txt darkRed  () { return color(COLOR_DARKRED   ); }
    public @NotNull Txt purple   () { return color(COLOR_PURPLE    ); }
    public @NotNull Txt gold     () { return color(COLOR_GOLD      ); }
    public @NotNull Txt lightGray() { return color(COLOR_LIGHTGRAY ); }
    public @NotNull Txt gray     () { return color(COLOR_GRAY      ); }
    public @NotNull Txt lightBlue() { return color(COLOR_LIGHTBLUE ); }
    public @NotNull Txt lime     () { return color(COLOR_LIME      ); }
    public @NotNull Txt cyan     () { return color(COLOR_CYAN      ); }
    public @NotNull Txt red      () { return color(COLOR_RED       ); }
    public @NotNull Txt magenta  () { return color(COLOR_MAGENTA   ); }
    public @NotNull Txt yellow   () { return color(COLOR_YELLOW    ); }
    public @NotNull Txt white    () { return color(COLOR_PURE_WHITE); }
    public @NotNull Txt pureWhite() { return color(COLOR_WHITE     ); }




    /**
     * Sets the color of the text.
     * @param r The Red value. 0-255.
     * @param g The Green value. 0-255.
     * @param b The Blue value. 0-255.
     * @return This.
     */
    public @NotNull Txt color(final int r, final int g, final int b) {
        style = style.withColor((r << 16) | (g << 8) | b);
        return this;
    }

    /**
     * Sets the color of the text.
     * @param rgb A vector representing the Red, Green and Blue values. 0-255.
     * @return This.
     */
    public @NotNull Txt color(final @NotNull Vector3i rgb) {
        style = style.withColor((rgb.x << 16) | (rgb.y << 8) | rgb.z);
        return this;
    }


    /**
     * Sets the color of the text.
     * @param argb A vector representing the Alpha, Red, Green and Blue values. 0-255, Alpha is ignored.
     * @return This.
     */
    public @NotNull Txt color(final @NotNull Vector4i argb) {
        style = style.withColor((argb.y << 16) | (argb.z << 8) | argb.w);
        return this;
    }

    /**
     * Sets the color of the text.
     * @param rgb An int representing the Red, Green and Blue values, each using 1 byte of the int. 0-255. The first byte is ignored.
     * @return This.
     */
    public @NotNull Txt color(final int rgb) {
        style = style.withColor(rgb);
        return this;
    }




    public @NotNull Txt bold           () { style = style.withBold         (true); return this; }
    public @NotNull Txt italic         () { style = style.withItalic       (true); return this; }
    public @NotNull Txt obfuscated     () { style = style.withObfuscated   (true); return this; }
    public @NotNull Txt strikethrough  () { style = style.withStrikethrough(true); return this; }

    public @NotNull Txt noBold         () { style = style.withBold         (false); return this; }
    public @NotNull Txt noItalic       () { style = style.withItalic       (false); return this; }
    public @NotNull Txt noObfuscated   () { style = style.withObfuscated   (false); return this; }
    public @NotNull Txt noStrikethrough() { style = style.withStrikethrough(false); return this; }




    /**
     * Concatenates a Component to this Txt.
     * @param s The Component value.
     * @return This.
     */
    public @NotNull Txt cat(final @NotNull Component s) {
        rawText.append(s);
        return this;
    }

    /**
     * Concatenates a Txt to this Txt.
     * @param s The Txt value.
     * @return This.
     */
    public @NotNull Txt cat(final @NotNull Txt s) {
        rawText.append(s.get());
        return this;
    }

    /**
     * Concatenates a String to this Txt.
     * @param s The String value.
     * @return This.
     */
    public @NotNull Txt cat(final @NotNull String s) {
        return this.cat(Component.literal(s));
    }




    /**
     * Returns a copy of this Txt as a Minecraft Component.
     * @return The Component value.
     */
    public @NotNull Component get() {
        rawText.setStyle(style);
        return rawText;
    }
}
