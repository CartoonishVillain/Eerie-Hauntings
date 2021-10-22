package com.cartoonishvillain.eeriehauntings;

import com.cartoonishvillain.eeriehauntings.entities.projectiles.SoulBallProjectile;
import com.cartoonishvillain.eeriehauntings.items.*;
import com.cartoonishvillain.eeriehauntings.items.ghosthuntingitems.*;
import com.cartoonishvillain.eeriehauntings.items.rewarditems.CalciteChalk;
import com.cartoonishvillain.eeriehauntings.items.rewarditems.GhostlyInstrument;
import com.cartoonishvillain.eeriehauntings.items.rewarditems.SoulBall;
import com.cartoonishvillain.eeriehauntings.items.rewarditems.UnearthlyDagger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Register {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, EerieHauntings.MODID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENT = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, EerieHauntings.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EerieHauntings.MODID);

    public static void init(){
        SOUND_EVENT.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<SoundEvent> MEDIUMSTRENGTHSOUNDS = SOUND_EVENT.register("medium_sounds", () -> new SoundEvent(new ResourceLocation(EerieHauntings.MODID, "medium_sounds")));
    public static final RegistryObject<SoundEvent> STRONGSTRENGTHSOUNDS = SOUND_EVENT.register("strong_sounds", () -> new SoundEvent(new ResourceLocation(EerieHauntings.MODID, "strong_sounds")));
    public static final RegistryObject<SoundEvent> EMFCOUNTERSOUNDS = SOUND_EVENT.register("emfcounter", () -> new SoundEvent(new ResourceLocation(EerieHauntings.MODID, "emfcounter")));
    public static final RegistryObject<SoundEvent> RADIOSOUND = SOUND_EVENT.register("radio", () -> new SoundEvent(new ResourceLocation(EerieHauntings.MODID, "radio")));


    public static final RegistryObject<Item> EMFCOUNTER = ITEMS.register("emf_counter", () -> new EMFCounter(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).stacksTo(1)));
    public static final RegistryObject<Item> OLDRADIO = ITEMS.register("old_radio", () -> new Radio(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).stacksTo(1)));
    public static final RegistryObject<Item> UNEARTHLYSHARD = ITEMS.register("unearthly_shard", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistryObject<Item> UNEARTHLYGEM = ITEMS.register("unearthly_gem", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistryObject<Item> INCENSESTICK = ITEMS.register("incense_stick", () -> new IncenseStick(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> PURIFIEDWATER = ITEMS.register("purified_water", () -> new PurifiedWater(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> REDSTONEDREAMCATCHER = ITEMS.register("redstone_dream_catcher", () -> new RedstoneDreamCatcher(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));


    public static final RegistryObject<Item> CALCITECHALK = ITEMS.register("calcite_chalk", () -> new CalciteChalk(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> UNEARTHLYOFFERING = ITEMS.register("unearthly_offering", () -> new Offering(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> GHOSTLYINSTRUMENT = ITEMS.register("ghostly_instrument", () -> new GhostlyInstrument(new Item.Properties().durability(128).tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> AMPLIFIEDGHOSTLYINSTRUMENT = ITEMS.register("amplified_ghostly_instrument", () -> new GhostlyInstrument(new Item.Properties().durability(128).tab(CreativeModeTab.TAB_MISC)));



    public static final RegistryObject<Item> UNEARTHLYDAGGER = ITEMS.register("unearthly_dagger", () -> new UnearthlyDagger(Materials.UNEARTHLY, 3, -1.5f, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));

    public static final RegistryObject<Item> SOULBALL = ITEMS.register("soulball", () -> new SoulBall(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<EntityType<SoulBallProjectile>> SOULBALLPROJECTILE = ENTITY_TYPES.register("soulballprojectile", () -> EntityType.Builder.<SoulBallProjectile>of(SoulBallProjectile::new, MobCategory.MISC).sized(0.25f, 0.25f).updateInterval(10).build(new ResourceLocation(EerieHauntings.MODID, "soulballprojectile").toString()));





}
