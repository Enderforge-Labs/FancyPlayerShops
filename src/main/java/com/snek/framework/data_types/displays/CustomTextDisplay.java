package com.snek.framework.data_types.displays;

import java.lang.reflect.Method;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.framework.data_types.ui.TextAlignment;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;
import com.snek.framework.utils.scheduler.Scheduler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Display.TextDisplay;
import net.minecraft.world.level.Level;







/**
 * A wrapper for Minecraft's TextDisplay.
 * <p> This class allows for better customization and more readable code.
 */
public class CustomTextDisplay extends CustomDisplay {
    public @NotNull TextDisplay getRawDisplay() { return (TextDisplay)heldEntity; }


    // Component cache and flag used to remove the text when the opacity value is lower than 26
    public static final @NotNull Component EMPTY_TEXT = new Txt().get();
    private             @NotNull Component textCache = EMPTY_TEXT;
    private final boolean noTextUnderA26;


    // Opacity cache. This is used to understand if the entity is currently interpolating from less than 26 to 26 or more
    // or vice versa, avoiding making changes until the interpolation is complete.
    private final @NotNull int[] lastAlpha = new int[3];
    private long lastAlphaUpdate = 0;
    private boolean lastAlphaInitialized = false;
    private void shiftLastAlpha(final int _new) {
        if(lastAlphaUpdate >= Scheduler.getTickNum()) return;

        if(!lastAlphaInitialized) {
            lastAlphaInitialized = true;
            lastAlpha[2] = 0;
            lastAlpha[1] = 0;
            lastAlpha[0] = _new;
        }
        else {
            lastAlpha[2] = lastAlpha[1];
            lastAlpha[1] = lastAlpha[0];
            lastAlpha[0] = _new;
        }
    }


    /**
     * This method flushes the opacity cache and ensures the displayed text doesn't remain
     * in an incorrect state after safety delays performed during short animations.
     * <p> Must be called at the end of each animation tick. //FIXME this can cause issues if the transition ticks are not aligned wit the step size.
     */
    public void tick() {
        shiftLastAlpha(getTextOpacity());

        if(noTextUnderA26) {
            final boolean a0 = lastAlpha[0] < 26; //! Always equal to the current (or target) opacity
            final boolean a1 = lastAlpha[1] < 26;
            final boolean a2 = lastAlpha[2] < 26;
            if(a0 == a1 && a1 != a2) setText(textCache);
        }
    }




    // Private methods
    private static @NotNull Method method_getText;
    private static @NotNull Method method_getLineWidth;
    private static @NotNull Method method_getTextOpacity;
    private static @NotNull Method method_getBackground;
    private static @NotNull Method method_setText;
    private static @NotNull Method method_setLineWidth;
    private static @NotNull Method method_setTextOpacity;
    private static @NotNull Method method_setBackground;
    static {
        try {
            method_getText          = TextDisplay.class.getDeclaredMethod("getText");
            method_getLineWidth     = TextDisplay.class.getDeclaredMethod("getLineWidth");
            method_getTextOpacity   = TextDisplay.class.getDeclaredMethod("getTextOpacity");
            method_getBackground    = TextDisplay.class.getDeclaredMethod("getBackgroundColor");
            method_setText          = TextDisplay.class.getDeclaredMethod("setText",      Component.class);
            method_setLineWidth     = TextDisplay.class.getDeclaredMethod("setLineWidth",       int.class);
            method_setTextOpacity   = TextDisplay.class.getDeclaredMethod("setTextOpacity",    byte.class);
            method_setBackground    = TextDisplay.class.getDeclaredMethod("setBackgroundColor", int.class);
        } catch(final NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        method_getText.setAccessible(true);
        method_getLineWidth.setAccessible(true);
        method_getTextOpacity.setAccessible(true);
        method_getBackground.setAccessible(true);
        method_setText.setAccessible(true);
        method_setLineWidth.setAccessible(true);
        method_setTextOpacity.setAccessible(true);
        method_setBackground.setAccessible(true);
    }




    /**
     * Creates a new CustomTextDisplay using an existing TextDisplay.
     * @param _rawDisplay The display entity.
     * @param _noTextUnderA26 Whether the text should not be rendered when the opacity is lower than 26,
     *     as opposed to forcing a minimum opacity value.
     */
    public CustomTextDisplay(final @NotNull TextDisplay _rawDisplay, final boolean _noTextUnderA26) {
        super(_rawDisplay);
        noTextUnderA26 = _noTextUnderA26;
    }

    /**
     * Creates a new CustomTextDisplay in the specified world.
     * @param _world The world.
     * @param _noTextUnderA26 Whether the text should not be rendered when the opacity is lower than 26,
     *     as opposed to forcing a minimum opacity value.
     */
    public CustomTextDisplay(final @NotNull Level _world, final boolean _noTextUnderA26) {
        super(new TextDisplay(EntityType.TEXT_DISPLAY, _world));
        noTextUnderA26 = _noTextUnderA26;
    }


    /**
     * Creates a new CustomTextDisplay using an existing TextDisplay.
     * @param _rawDisplay The display entity.
     */
    public CustomTextDisplay(final @NotNull TextDisplay _rawDisplay) {
        this(_rawDisplay, true);
    }

    /**
     * Creates a new CustomTextDisplay in the specified world.
     * @param _world The world.
     */
    public CustomTextDisplay(final @NotNull Level _world) {
        this(_world, true);
    }




    /**
     * Sets a new text value to the entity.
     * <p> This is equivalent to changing the entity's "text" NBT.
     * @param text The new value.
     */
    public void setText(final @NotNull Component text) {
        if(noTextUnderA26 && lastAlpha[0] < 26 && lastAlpha[1] < 26) {
            Utils.invokeSafe(method_setText, heldEntity, EMPTY_TEXT);
        }
        else {
            Utils.invokeSafe(method_setText, heldEntity, text);
        }
        textCache = text.copy();
    }


    /**
     * Sets a new line width value to the entity.
     * <p> This is equivalent to changing the entity's "line_width" NBT.
     * @param width The new value.
     */
    public void setLineWidth(final int width) {
        Utils.invokeSafe(method_setLineWidth, heldEntity, width);
    }


    /**
     * Retrieves the entity's text value.
     * @return The current text.
     */
    public @NotNull Component getText() {
        return textCache;
    }


    /**
     * Retrieves the entity's line width value.
     * @return The current line width.
     */
    public int getLineWidth() {
        return (int)Utils.invokeSafe(method_getLineWidth, heldEntity);
    }


    /**
     * Returns the actual text the entity is displaying, as opposed to the cached value, meaning that
     * this method returns an empty Component when noTextUnderA26 is set to true and the opacity is less than 26.
     * @return The text value.
     */
    public @NotNull Component getTrueText() {
        return (Component)Utils.invokeSafe(method_getText, heldEntity);
    }




    /**
     * Sets the alpha value of the rendered text.
     * @param a The alpha value.
     *  <p> Values smaller than 26 are converted to 26 unless noTextUnderA26 is set to true, in which case the text is not rendered at all.
     *  <p> This is done because minecraft ignores these values and usually makes the text fully opaque instead of fully transparent, rendering animations jittery.
     *  <p> NOTICE:
     *  <p> Interpolation is broken. Opacity values are NOT converted back to 0-255 range
     *  <p> before interpolating, but the raw byte value (0 to 127, -128 to -1) is used instead.
     */
    public void setTextOpacity(int a) {
        shiftLastAlpha(a);
        lastAlphaUpdate = Scheduler.getTickNum();

        if(a < 26) {
            if(noTextUnderA26 && lastAlpha[1] < 26) {
                Utils.invokeSafe(method_setText, heldEntity, EMPTY_TEXT);
            }
            else {
                Utils.invokeSafe(method_setText, heldEntity, textCache);
                a = 26;
            }
        }
        else if(lastAlpha[1] >= 26 && lastAlpha[2] < 26) {
            Utils.invokeSafe(method_setText, heldEntity, textCache);
        }
        Utils.invokeSafe(method_setTextOpacity, getRawDisplay(), (byte)(a > 127 ? a - 256 : a));
    }


    /**
     * Retrieves the entity's text opacity value.
     * @return The current text opacity.
     */
    public int getTextOpacity() {
        final int a = (byte)Utils.invokeSafe(method_getTextOpacity, getRawDisplay());
        return a < 0 ? a + 256 : a;
    }


    /**
     * Sets a new background color value to the entity.
     * <p> This is equivalent to changing the entity's "background" NBT.
     * @param argb The new value.
     */
    public void setBackground(final @NotNull Vector4i argb) {
        Utils.invokeSafe(method_setBackground, getRawDisplay(), (argb.x << 24) | (argb.y << 16) | (argb.z << 8) | argb.w);
    }


    /**
     * Retrieves the entity's background color value.
     * @return The current background color.
     */
    public @NotNull Vector4i getBackground() {
        final int bg = (int)Utils.invokeSafe(method_getBackground, getRawDisplay());
        return new Vector4i((bg >> 24) & 0xFF, (bg >> 16) & 0xFF, (bg >> 8) & 0xFF, bg & 0xFF);
    }


    /**
     * Sets a new text alignment value to the entity.
     * <p> This is equivalent to changing the entity's "text alignment" NBT.
     * @param alignment The new value.
     */
    public void setTextAlignment(final @NotNull TextAlignment alignment) {
        final CompoundTag nbt = new CompoundTag();
        heldEntity.save(nbt);
        nbt.putString("alignment", alignment.asString());
        heldEntity.load(nbt);
    }
}
