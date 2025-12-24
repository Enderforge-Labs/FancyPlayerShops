package com.snek.fancyplayershops;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.graphics.ui.ProductDisplayContext;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.graphics.core.Context;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.UtilityClassBase;




public final class GetDisplay extends UtilityClassBase {
    public static @NotNull ProductDisplay get(final @NotNull Div _this) {
        assert Require.nonNull(_this, "element");

        assert Require.condition(_this.getCanvas() != null, "GetDisplay.get() used on orphan element");
        final Context context = _this.getCanvas().getContext();

        assert Require.instanceOf(context, ProductDisplayContext.class, "element's context");
        return ((ProductDisplayContext)context).getDisplay();
    }
}
