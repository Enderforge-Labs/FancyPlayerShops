package com.snek.fancyplayershops.graphics.hud.stash;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.data_types.displays.CustomDisplay;
import com.snek.frameworklib.graphics.basic.elements.PanelElm;
import com.snek.frameworklib.graphics.core.elements.Elm;
import com.snek.frameworklib.graphics.core.styles.ElmStyle;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.graphics.interfaces.Hoverable;
import com.snek.frameworklib.graphics.layout.Div;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;

//FIXME remove. this was only used for testing
public class tmp_div extends PanelElm implements Hoverable {

    protected tmp_div(@NotNull ServerLevel world) {
        super(world);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void onHoverEnter(@NotNull Player player) {
    }

    @Override
    public void onHoverExit(@Nullable Player player) {
    }


}
