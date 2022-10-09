package me.emafire003.dev.coloredglowlib.events;

import me.emafire003.dev.coloredglowlib.ColoredGlowLib;
import me.emafire003.dev.coloredglowlib.ColoredGlowLibMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


import static me.emafire003.dev.coloredglowlib.ColoredGlowLibMod.LOGGER;

public class CGLEvents {
    @Mod.EventBusSubscriber(modid = ColoredGlowLibMod.MOD_ID)
    public static class ForgeEvents {

        @SubscribeEvent
        public static void onServerStops(ServerStoppedEvent event) {
            try{
                ColoredGlowLibMod.getLib().saveDataToFile();
            }catch (Exception e){
                LOGGER.error("There was an error while trying to save the data file!");
                e.printStackTrace();
            }
        }

        @SubscribeEvent
        public static void onServerStarts(ServerStartedEvent event) {
            ColoredGlowLib lib = ColoredGlowLibMod.getLib();
            lib.getValuesFromFile();
            lib.server_registered = true;
            //updateData(minecraftServer);
            lib.sendDataPacketsToPlayers(event.getServer());
        }

        private static int tickCounter = 0;

        @SubscribeEvent
        public static void onServerTicks(TickEvent.ServerTickEvent event) {
            if(tickCounter != -1){
                tickCounter++;
            }
            if(tickCounter==20*ColoredGlowLibMod.getLib().getDelayBetweenSendingPackets()){
                //updateData(minecraftServer);
                ColoredGlowLibMod.getLib().sendDataPacketsToPlayers(event.getServer());
                tickCounter = 0;
            }
        }
    }

}
