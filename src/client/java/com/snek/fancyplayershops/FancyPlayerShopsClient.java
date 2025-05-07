package com.snek.fancyplayershops;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;








/**
 * A client mod not meant for gameplay.
 * <p>
 * Some aspects of text-based UI elements depend on the rendered size of the TextDisplay, which depends on the font.
 * This mod is used to pre-calculate the width and height of each character of the active font.
 * This data is then used by the generated FontSize class to compute entity dimensions in runtime.
 */
public class FancyPlayerShopsClient implements ClientModInitializer {
    private boolean generated = false;


    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_WORLD_TICK.register(client -> {
            if(!generated) {
                FontWidthGenerator.generate();
                generated = true;
            }
        });
    }
}