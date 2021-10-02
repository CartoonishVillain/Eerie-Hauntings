package com.cartoonishvillain.eeriehauntings;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Register {
    public static final DeferredRegister<SoundEvent> SOUND_EVENT = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, EerieHauntings.MODID);

    public static void init(){
        SOUND_EVENT.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<SoundEvent> MEDIUMSTRENGTHSOUNDS = SOUND_EVENT.register("medium_sounds", () -> new SoundEvent(new ResourceLocation(EerieHauntings.MODID, "medium_sounds")));
    public static final RegistryObject<SoundEvent> STRONGSTRENGTHSOUNDS = SOUND_EVENT.register("strong_sounds", () -> new SoundEvent(new ResourceLocation(EerieHauntings.MODID, "strong_sounds")));

}
