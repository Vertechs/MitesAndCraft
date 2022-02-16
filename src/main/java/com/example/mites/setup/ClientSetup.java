package com.example.mites.setup;

import com.example.mites.client.PowerMakerScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {

    public static void init(FMLClientSetupEvent event) {
        // que on main thread, setups run in parallel, register not thread safe
        event.enqueueWork(() -> MenuScreens.register(Registration.POWER_MAKER_CONTAINER.get(), PowerMakerScreen::new));
    }
}
