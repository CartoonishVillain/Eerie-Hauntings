package com.cartoonishvillain.eeriehauntings.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommonConfig {

    public ConfigHelper.ConfigValueListener<Boolean> TORMENTCOMPAT;
    public ConfigHelper.ConfigValueListener<Double> TORMENTCOMPATVALUE;

    public static final String CATEGORY_COMPAT = "Haunting Compatability";



    public CommonConfig(ForgeConfigSpec.Builder builder, ConfigHelper.Subscriber subscriber) {
        builder.comment("Modify compatability mod features").push(CATEGORY_COMPAT);
        this.TORMENTCOMPAT = subscriber.subscribe(builder.comment("Enables or disables compat with Torment, a fellow SpookyJam 2021 mod. Players with higher level of torment get their haunt chances multiplied by the value in tormentCompatRate per point of torment. This does nothing without the mod installed.").define("eerieTormentCompat", true));
        this.TORMENTCOMPATVALUE = subscriber.subscribe(builder.comment("The multiplier each point of torment provides to the strength of the observed effect.").defineInRange("tormentCompatRate", 0.025, 0, 1));
        builder.pop();
    }
}
