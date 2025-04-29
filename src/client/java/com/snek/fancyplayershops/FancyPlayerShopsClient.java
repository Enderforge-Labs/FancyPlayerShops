package com.snek.fancyplayershops;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import java.util.Objects;








/**
 * A client mod not meant for gameplay.
 *
 * Some aspects of text-based UI elements depend on the rendered size of the TextDisplayEntity, which depends on the font.
 * This mod is used to pre-calculate the width and height of each character of the active font.
 * This data is then used by the generated FontSize class to compute entity dimensiond in runtime.
 */
public class FancyPlayerShopsClient implements ClientModInitializer {
    private boolean isGenerated = false;


    /**
     * Checks whether the data has been generated.
     * @return True if the data has been generated, false otherwise.
     */
    public boolean getIsGenerated() {
        return isGenerated;
    }

    public void setIsGenerated(boolean isGenerated) {
        this.isGenerated = isGenerated;
    }


    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_WORLD_TICK.register(client -> {
            if(!isGenerated) {
                FontWidthGenerator.generate();
                isGenerated = true;
            }
        });
    }
}