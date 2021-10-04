package com.cartoonishvillain.eeriehauntings.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ClientConfig {

    public static final String CATEGORY_HAUNTS = "Haunting Visuals";

    public ConfigHelper.ConfigValueListener<Boolean> SHADERS;


    public ClientConfig(ForgeConfigSpec.Builder builder, ConfigHelper.Subscriber subscriber){
        builder.comment("Modify ghost behaviors and the numbers behind the scenes").push(CATEGORY_HAUNTS);
        this.SHADERS = subscriber.subscribe(builder.comment("Enables or disables the old mojang shaders when strong events happen. No intensive shaders are used, just some quick effects.").define("strongShaders", true));
        builder.pop();
    }


}
