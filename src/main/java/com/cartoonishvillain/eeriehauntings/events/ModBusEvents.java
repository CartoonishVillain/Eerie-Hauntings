package com.cartoonishvillain.eeriehauntings.events;

import com.cartoonishvillain.eeriehauntings.EerieHauntings;
import com.cartoonishvillain.eeriehauntings.capabilities.playercapability.IPlayerCapability;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import com.cartoonishvillain.eeriehauntings.capabilities.worldcapability.IWorldCapability;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EerieHauntings.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusEvents {
    @SubscribeEvent
    public static void CapabilityRegister(final RegisterCapabilitiesEvent event){
        event.register(IWorldCapability.class);
        event.register(IPlayerCapability.class);
    }
}
