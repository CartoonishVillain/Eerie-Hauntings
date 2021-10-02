package com.cartoonishvillain.eeriehauntings;

import com.cartoonishvillain.eeriehauntings.networking.ClientSoundMessenger;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("eeriehauntings")
public class EerieHauntings
{
    public static ArrayList<SoundEvent> lowEndSounds = new ArrayList<SoundEvent>(List.of(SoundEvents.GLASS_BREAK, SoundEvents.CREEPER_PRIMED, SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, SoundEvents.WITHER_BREAK_BLOCK, SoundEvents.CHEST_OPEN, SoundEvents.WITHER_SPAWN, SoundEvents.WITHER_AMBIENT, SoundEvents.LAVA_EXTINGUISH, SoundEvents.PISTON_CONTRACT, SoundEvents.PISTON_EXTEND, SoundEvents.TNT_PRIMED, SoundEvents.GENERIC_EXPLODE, SoundEvents.ENDER_DRAGON_GROWL, SoundEvents.AMBIENT_CAVE, SoundEvents.AMBIENT_CAVE));
    public static final String MODID = "eeriehauntings";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public EerieHauntings() {
        ClientSoundMessenger.register();
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code

    }

}
