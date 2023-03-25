package com.cartoonishvillain.eeriehauntings.events;

import com.cartoonishvillain.eeriehauntings.EerieHauntings;
import com.cartoonishvillain.eeriehauntings.capabilities.playercapability.IPlayerCapability;
import com.cartoonishvillain.eeriehauntings.capabilities.playercapability.PlayerCapability;
import com.cartoonishvillain.eeriehauntings.capabilities.worldcapability.WorldCapability;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import com.cartoonishvillain.eeriehauntings.capabilities.worldcapability.IWorldCapability;
import net.minecraftforge.fml.common.Mod;

import static com.cartoonishvillain.eeriehauntings.Register.*;

@Mod.EventBusSubscriber(modid = EerieHauntings.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusEvents {
    @SubscribeEvent
    public static void CapabilityRegister(final RegisterCapabilitiesEvent event){
        event.register(IWorldCapability.class);
        event.register(IPlayerCapability.class);
        PlayerCapability.INSTANCE = CapabilityManager.get(new CapabilityToken<IPlayerCapability>() {});
        WorldCapability.INSTANCE = CapabilityManager.get(new CapabilityToken<IWorldCapability>() {});
    }

    @SubscribeEvent
    public static void TabRegister(final CreativeModeTabEvent.BuildContents event) {
        if (event.getTab() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(EMFCOUNTER);
            event.accept(OLDRADIO);
            event.accept(INCENSESTICK);
            event.accept(PURIFIEDWATER);
            event.accept(REDSTONEDREAMCATCHER);
            event.accept(CALCITECHALK);
            event.accept(UNEARTHLYOFFERING);
            event.accept(GHOSTLYINSTRUMENT);
            event.accept(AMPLIFIEDGHOSTLYINSTRUMENT);
        }

        if (event.getTab() == CreativeModeTabs.COMBAT) {
            event.accept(UNEARTHLYDAGGER);
        }

        if (event.getTab() == CreativeModeTabs.INGREDIENTS) {
            event.accept(UNEARTHLYSHARD);
            event.accept(UNEARTHLYGEM);
        }
    }
}
