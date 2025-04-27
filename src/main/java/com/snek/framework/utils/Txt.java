package com.snek.framework.utils;

import org.joml.Vector3i;
import org.joml.Vector4i;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;








/**
 * A simpler but more readable minecraft.text.MutableText.
 * Use .get() to create a MutableText from this object's data.
 */
public class Txt {
    private MutableText rawText;
    private Style style;


    public Txt() {
        rawText = Text.empty();
        style = Style.EMPTY;
    }
    public Txt(String s) {
        rawText = Text.literal(s);
        style = Style.EMPTY;
    }
    public Txt(MutableText s) {
        rawText = s.copy();
        style = rawText.getStyle();
    }
    public Txt(Text s) {
        rawText = s.copy();
        style = rawText.getStyle();
    }

    public Txt copy() {
        rawText.setStyle(style);
        return new Txt(rawText.copy());
    }



    public static final Vector3i COLOR_BLACK     = new Vector3i(  0,   0,   0);
    public static final Vector3i COLOR_BLUE      = new Vector3i(  0,   0, 170);
    public static final Vector3i COLOR_GREEN     = new Vector3i(  0, 170,   0);
    public static final Vector3i COLOR_AQUA      = new Vector3i(  0, 170, 170);
    public static final Vector3i COLOR_DARKRED   = new Vector3i(170,   0,   0);
    public static final Vector3i COLOR_PURPLE    = new Vector3i(170,   0, 170);
    public static final Vector3i COLOR_GOLD      = new Vector3i(255, 170,   0);
    public static final Vector3i COLOR_LIGHTGRAY = new Vector3i(170, 170, 170);
    public static final Vector3i COLOR_GRAY      = new Vector3i( 85,  85,  85);
    public static final Vector3i COLOR_LIGHTBLUE = new Vector3i( 85,  85, 255);
    public static final Vector3i COLOR_LIME      = new Vector3i( 85, 255,  85);
    public static final Vector3i COLOR_CYAN      = new Vector3i( 85, 255, 255);
    public static final Vector3i COLOR_RED       = new Vector3i(255,  85,  85);
    public static final Vector3i COLOR_MAGENTA   = new Vector3i(255,  85, 255);
    public static final Vector3i COLOR_YELLOW    = new Vector3i(255, 255,  85);
    public static final Vector3i COLOR_WHITE     = new Vector3i(255, 255, 255);


    public Txt black    () { return color(COLOR_BLACK    ); }
    public Txt blue     () { return color(COLOR_BLUE     ); }
    public Txt green    () { return color(COLOR_GREEN    ); }
    public Txt aqua     () { return color(COLOR_AQUA     ); }
    public Txt darkRed  () { return color(COLOR_DARKRED  ); }
    public Txt purple   () { return color(COLOR_PURPLE   ); }
    public Txt gold     () { return color(COLOR_GOLD     ); }
    public Txt lightGray() { return color(COLOR_LIGHTGRAY); }
    public Txt gray     () { return color(COLOR_GRAY     ); }
    public Txt lightBlue() { return color(COLOR_LIGHTBLUE); }
    public Txt lime     () { return color(COLOR_LIME     ); }
    public Txt cyan     () { return color(COLOR_CYAN     ); }
    public Txt red      () { return color(COLOR_RED      ); }
    public Txt magenta  () { return color(COLOR_MAGENTA  ); }
    public Txt yellow   () { return color(COLOR_YELLOW   ); }
    public Txt white    () { return color(COLOR_WHITE    ); }




    public Txt color(int r, int g, int b) {
        style = style.withColor((r << 16) | (g << 8) | b);
        return this;
    }
    public Txt color(Vector3i rgb) {
        style = style.withColor((rgb.x << 16) | (rgb.y << 8) | rgb.z);
        return this;
    }
    public Txt color(Vector4i rgb) { //ARGB //TODO comment method
        style = style.withColor((rgb.y << 16) | (rgb.z << 8) | rgb.w);
        return this;
    }
    public Txt color (int rgb) { style = style.withColor        (rgb ); return this; }
    public Txt bold         () { style = style.withBold         (true); return this; }
    public Txt italic       () { style = style.withItalic       (true); return this; }
    public Txt obfuscated   () { style = style.withObfuscated   (true); return this; }
    public Txt strikethrough() { style = style.withStrikethrough(true); return this; }

    public Txt noBold         () { style = style.withBold         (false); return this; }
    public Txt noItalic       () { style = style.withItalic       (false); return this; }
    public Txt noObfuscated   () { style = style.withObfuscated   (false); return this; }
    public Txt noStrikethrough() { style = style.withStrikethrough(false); return this; }




    public Txt cat(Text s) {
        rawText.append(s);
        return this;
    }

    public Txt cat(Txt s) {
        rawText.append(s.get());
        return this;
    }

    public Txt cat(String s) {
        return this.cat(Text.literal(s));
    }

    public Text get() {
        rawText.setStyle(style);
        return rawText;
    }
}
