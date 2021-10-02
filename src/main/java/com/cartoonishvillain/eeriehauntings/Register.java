package com.cartoonishvillain.eeriehauntings;

import com.cartoonishvillain.eeriehauntings.items.EMFCounter;
import com.cartoonishvillain.eeriehauntings.items.Radio;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Register {
    public static final DeferredRegister<SoundEvent> SOUND_EVENT = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, EerieHauntings.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EerieHauntings.MODID);

    public static void init(){
        SOUND_EVENT.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<SoundEvent> MEDIUMSTRENGTHSOUNDS = SOUND_EVENT.register("medium_sounds", () -> new SoundEvent(new ResourceLocation(EerieHauntings.MODID, "medium_sounds")));
    public static final RegistryObject<SoundEvent> STRONGSTRENGTHSOUNDS = SOUND_EVENT.register("strong_sounds", () -> new SoundEvent(new ResourceLocation(EerieHauntings.MODID, "strong_sounds")));
    public static final RegistryObject<SoundEvent> EMFCOUNTERSOUNDS = SOUND_EVENT.register("emfcounter", () -> new SoundEvent(new ResourceLocation(EerieHauntings.MODID, "emfcounter")));
    public static final RegistryObject<SoundEvent> RADIOSOUND = SOUND_EVENT.register("radio", () -> new SoundEvent(new ResourceLocation(EerieHauntings.MODID, "radio")));


    public static final RegistryObject<Item> EMFCOUNTER = ITEMS.register("emf_counter", () -> new EMFCounter(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> OLDRADIO = ITEMS.register("old_radio", () -> new Radio(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));


}
