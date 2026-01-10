package com.snek.fancyplayershops.graphics.ui.core.elements;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.core.styles.ProductCanvasBack_S;
import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.basic.elements.PanelElm;
import com.snek.frameworklib.graphics.core.UiCanvas;
import com.snek.frameworklib.graphics.core.UiContext;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








/**
 * An abstract canvas class used to create product display menus.
 * <p>
 * It stores a reference to the product display and handles the item display.
 */
public abstract class ProductCanvasBase extends UiCanvas {
    public static final float DEFAULT_HEIGHT = TITLE_H * 0.75f;
    public static final float DEFAULT_DISTANCE = 0.02f;


    // Temporary product display reference
    //! This is used to initialize the default canvas elements
    //! the overridden provider method is called before the local reference can be initialized by the subclass
    protected static ProductDisplay __tmp_display_ref;


    protected final @NotNull ProductDisplay display;
    public abstract @Nullable Div getDisclaimerElm();


    /**
     * Creates a new ProductDisplayCanvasBase.
     * @param display The target display.
     * @param defaultTitle The text to display in the title element.
     *     If null, no title element is created.
     *     This value can be later changed using {@link #updateTitle(Component)}
     * @param height The total height of the canvas.
     * @param heightTop The height of the top border.
     * @param heightBottom The height of the bottom border.
     */
    @SuppressWarnings("java:S3010")
    protected ProductCanvasBase(final @NotNull ProductDisplay display, final @Nullable Component defaultTitle, final float height, final float heightTop, final float heightBottom) {
        super(
            ((Supplier<UiContext>)()->{ __tmp_display_ref = display; return display.getUi(); }).get(),
            defaultTitle,
            height, heightTop, heightBottom
        );
        this.display = display;
    }

    /**
     * Creates a new ProductDisplayCanvasBase.
     * @param display The target display.
     * @param defaultTitle The text to display in the title element.
     *     If null, no title element is created.
     *     This value can be later changed using {@link #updateTitle(Component)}
     * @param height The total height of the canvas.
     * @param heightTop The height of the top border.
     * @param heightBottom The height of the bottom border.
     */
    @SuppressWarnings("java:S3010")
    protected ProductCanvasBase(final @NotNull ProductDisplay display, final @Nullable String defaultTitle, final float height, final float heightTop, final float heightBottom) {
        this(display, defaultTitle == null ? null : new Txt(defaultTitle).white().bold().get(), height, heightTop, heightBottom);
    }




    @Override
    public boolean forwardClick(final @NotNull Player player, final @NotNull ClickAction clickType) {

        // Click the shop first. This allows non-users to retrieve items by left clicking
        final boolean player_has_permission = display.onClick(player, clickType);

        // If the player is the user, click the canvas (which is now EditCanvas or BuyCanvas)
        if(player_has_permission) {
            return super.forwardClick(player, clickType);
        }

        // If the player is not the user, return true (click was consumed)
        else {
            return true;
        }
    }




    @Override
    protected void rotate(final float from, final float to, final boolean animate) {
        updateItemDisplayRot(from, to, animate);
        super.rotate(from, to, animate);
    }




    /**
     * Calculates the animations required to to go from a specified rotation to another, without rotating the item's model.
     * @param from The starting rotation.
     * @param to The new rotation.
     * @return The item display animation.
     */
    public static @NotNull Animation calcItemDisplayRotationAnimation(final float from, final float to) {
        final float rotation = (to - from);
        return new Animation(
            new Transition(CANVAS_ROTATION_TIME, Easings.cubicOut)
            .additiveTransform(new Transform().rotGlobalY(rotation).rotY(- rotation))
        );
    }



    protected void updateItemDisplayRot(final float from, final float to, final boolean animate) {
        final Animation animation = calcItemDisplayRotationAnimation(from, to);
        display.getItemDisplay().applyAnimation(animation, true, animate);
    }


    @Override
    public float getInteractionSizeTop() {
        return super.getInteractionSizeTop() + DEFAULT_DISTANCE + DEFAULT_HEIGHT;
    }



    @Override public PanelElm createNewBgElement  (final @NotNull ServerLevel level) { return new ProductCanvasBackground(__tmp_display_ref); }
    @Override public PanelElm createNewBackElement(final @NotNull ServerLevel level) { return new PanelElm(level, new ProductCanvasBack_S()); }
}