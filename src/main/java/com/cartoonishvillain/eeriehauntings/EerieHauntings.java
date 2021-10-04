package com.cartoonishvillain.eeriehauntings;

import com.cartoonishvillain.eeriehauntings.config.ConfigHelper;
import com.cartoonishvillain.eeriehauntings.config.ServerConfig;
import com.cartoonishvillain.eeriehauntings.networking.lightsoundpackets.LightClientSoundMessenger;
import com.cartoonishvillain.eeriehauntings.networking.mediumsoundpackets.MediumClientSoundMessenger;
import com.cartoonishvillain.eeriehauntings.networking.shaderupdatepacket.ShaderUpdateMessenger;
import com.cartoonishvillain.eeriehauntings.networking.strongsoundpackets.StrongClientSoundMessenger;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("eeriehauntings")
public class EerieHauntings
{
    public static ArrayList<SoundEvent> lowEndSounds = new ArrayList<SoundEvent>(List.of(SoundEvents.GLASS_BREAK, SoundEvents.CREEPER_PRIMED, SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, SoundEvents.WITHER_BREAK_BLOCK, SoundEvents.CHEST_OPEN, SoundEvents.WITHER_SPAWN, SoundEvents.WITHER_AMBIENT, SoundEvents.LAVA_EXTINGUISH, SoundEvents.PISTON_CONTRACT, SoundEvents.PISTON_EXTEND, SoundEvents.TNT_PRIMED, SoundEvents.GENERIC_EXPLODE, SoundEvents.ENDER_DRAGON_GROWL, SoundEvents.AMBIENT_CAVE, SoundEvents.AMBIENT_CAVE));
    public static final String MODID = "eeriehauntings";
    public static ServerConfig serverConfig;
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public EerieHauntings() {
        serverConfig = ConfigHelper.register(ModConfig.Type.SERVER, ServerConfig::new);
        LightClientSoundMessenger.register();
        MediumClientSoundMessenger.register();
        StrongClientSoundMessenger.register();
        ShaderUpdateMessenger.register();
        Register.init();
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
