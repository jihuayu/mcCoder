package com.jihuayu.mccoder;

import com.jihuayu.mccoder.json.loader.ItemLoader;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(McCoder.MOD_ID)
public class McCoder {
    public static final String MOD_ID = "mccoder";
    private static final Logger LOGGER = LogManager.getLogger();

    public McCoder() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {

    }
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> e) {
            LOGGER.warn(e.toString());
            ItemLoader.getItems().forEach(i->{
                LOGGER.warn(i.toString());
                e.getRegistry().register(i);
            });
        }
    }
}
