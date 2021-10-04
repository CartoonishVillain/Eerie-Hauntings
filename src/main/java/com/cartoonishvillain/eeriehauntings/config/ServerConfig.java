package com.cartoonishvillain.eeriehauntings.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ServerConfig {

    public static final String CATEGORY_HAUNTS = "Haunting Settings";

    public ConfigHelper.ConfigValueListener<Double> NORMALHAUNTCHANCE;
    public ConfigHelper.ConfigValueListener<Double> VILLAGERHAUNTCHANCE;
    public ConfigHelper.ConfigValueListener<Double> PLAYERHAUNTCHANCE;
    public ConfigHelper.ConfigValueListener<Double> GOLEMHAUNTCHANCE;
    public ConfigHelper.ConfigValueListener<Double> PETHAUNTCHANCE;
    public ConfigHelper.ConfigValueListener<Double> MISCHAUNTCHANCE;
    public ConfigHelper.ConfigValueListener<Boolean> MEDIUMEFFECT;
    public ConfigHelper.ConfigValueListener<Boolean> STRONGEFFECT;
    public ConfigHelper.ConfigValueListener<Integer> MINIMUMEFFECTWAIT;
    public ConfigHelper.ConfigValueListener<Integer> MAXIMUMEFFECTWAIT;
    public ConfigHelper.ConfigValueListener<Integer> BOONSTRENGTH;


    public ServerConfig(ForgeConfigSpec.Builder builder, ConfigHelper.Subscriber subscriber){
        builder.comment("Modify ghost behaviors and the numbers behind the scenes").push(CATEGORY_HAUNTS);
        this.NORMALHAUNTCHANCE = subscriber.subscribe(builder.comment("The percent chance a player is to be haunted on a given night by default. If the player isn't haunted at night, this is the value they will start off on for the next night.").defineInRange("defaultHauntChance", 1.0, 0.0, 100));
        this.VILLAGERHAUNTCHANCE = subscriber.subscribe(builder.comment("The percentage increase in haunt chance a player experiences when they kill a villager.").defineInRange("villagerHauntChance", 10.0, 0, 100));
        this.PLAYERHAUNTCHANCE = subscriber.subscribe(builder.comment("The percentage increase in haunt chance a player experiences when they kill another player.").defineInRange("playerHauntChance", 25.0, 0, 100));
        this.GOLEMHAUNTCHANCE = subscriber.subscribe(builder.comment("The percentage increase in haunt chance a player experiences when they kill a golem.").defineInRange("golemHauntChance", 7.5, 0, 100));
        this.PETHAUNTCHANCE = subscriber.subscribe(builder.comment("The percentage increase in haunt chance a player experiences when they kill a tamable entity.").defineInRange("petHauntChance", 12.5, 0, 100));
        this.MISCHAUNTCHANCE = subscriber.subscribe(builder.comment("The percentage increase in haunt chance a player experiences when they kill anything outside of the other categories.").defineInRange("miscHauntChance", 1.0, 0, 100));
        this.MEDIUMEFFECT = subscriber.subscribe(builder.comment("Enables or disables medium strength events (blindness, slowness, weakness) (replaces with weak sound events)").define("mediumStrengthEvents", true));
        this.STRONGEFFECT = subscriber.subscribe(builder.comment("Enables or disables strong strength events (levitation, hunger, nausea) (replaces with weak sound events)").define("strongStrengthEvents", true));
        this.MINIMUMEFFECTWAIT = subscriber.subscribe(builder.comment("The minimum wait time between ghost events in ticks").defineInRange("minimumEventWait", 400, 100, Integer.MAX_VALUE));
        this.MAXIMUMEFFECTWAIT = subscriber.subscribe(builder.comment("The maximum wait time between ghost events in ticks").defineInRange("maximumEventWait", 700, 100, Integer.MAX_VALUE));
        this.BOONSTRENGTH = subscriber.subscribe(builder.comment("Increase the potency of boons additively").defineInRange("boonstrength", 0, 0, 100));
    }


}