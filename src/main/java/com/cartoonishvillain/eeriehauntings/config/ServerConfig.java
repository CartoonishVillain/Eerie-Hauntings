package com.cartoonishvillain.eeriehauntings.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ServerConfig {

    public static final String CATEGORY_HAUNTS = "Haunting Settings";
    public static final String CATEGORY_MECHANICS = "Haunting Mechanics";

    public ConfigHelper.ConfigValueListener<Double> NORMALHAUNTCHANCE;
    public ConfigHelper.ConfigValueListener<Double> VILLAGERHAUNTCHANCE;
    public ConfigHelper.ConfigValueListener<Double> PLAYERHAUNTCHANCE;
    public ConfigHelper.ConfigValueListener<Double> GOLEMHAUNTCHANCE;
    public ConfigHelper.ConfigValueListener<Double> PETHAUNTCHANCE;
    public ConfigHelper.ConfigValueListener<Double> MISCHAUNTCHANCE;
    public ConfigHelper.ConfigValueListener<Boolean> MEDIUMEFFECT;
    public ConfigHelper.ConfigValueListener<Boolean> STRONGEFFECT;
    public ConfigHelper.ConfigValueListener<Boolean> PHYSEFFECT;
    public ConfigHelper.ConfigValueListener<Integer> PHYSEFFECTRADIUS;
    public ConfigHelper.ConfigValueListener<Integer> MINIMUMEFFECTWAIT;
    public ConfigHelper.ConfigValueListener<Integer> MAXIMUMEFFECTWAIT;
    public ConfigHelper.ConfigValueListener<Integer> BOONSTRENGTH;

    public ConfigHelper.ConfigValueListener<Boolean> EASYMODE;
    public ConfigHelper.ConfigValueListener<Integer> CHALKDURATION;
    public ConfigHelper.ConfigValueListener<Boolean> ANGERGHOST;
    public ConfigHelper.ConfigValueListener<Boolean> BOON;


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
        this.PHYSEFFECT = subscriber.subscribe(builder.comment("Allows ghost actions to effect the nearby surroundings of players. Disabling may improve performance, but will also reduce how spooky the ghost can be.").define("physicalEvents", true));
        this.PHYSEFFECTRADIUS = subscriber.subscribe(builder.comment("The radius of which a ghost will look for physical items to interact with. Larger numbers are more resource heavy, and make the ghost stronger. Only matter if physicalEvents is set to true.").defineInRange("physicalEventScans", 5, 3, 16));
        this.MINIMUMEFFECTWAIT = subscriber.subscribe(builder.comment("The minimum wait time between ghost events in ticks").defineInRange("minimumEventWait", 400, 100, Integer.MAX_VALUE));
        this.MAXIMUMEFFECTWAIT = subscriber.subscribe(builder.comment("The maximum wait time between ghost events in ticks").defineInRange("maximumEventWait", 700, 100, Integer.MAX_VALUE));
        this.BOONSTRENGTH = subscriber.subscribe(builder.comment("Increase the potency of boons additively").defineInRange("boonstrength", 0, 0, 100));
        builder.pop();

        builder.comment("Modify ghost mechanics and the numbers behind the scenes").push(CATEGORY_MECHANICS);
        this.EASYMODE = subscriber.subscribe(builder.comment("Disables needing specific exorcism types to exorcise the ghost. Also automatically means the anger mechanic is disabled").define("easyMode", false));
        this.ANGERGHOST = subscriber.subscribe(builder.comment("Enables or Disables ghosts getting angry when the wrong exorcism type is used, ramping up more harsh effects until the next day.").define("anger", true));
        this.BOON = subscriber.subscribe(builder.comment("Enables or disables boons, a system where a ghost can provide a buff for an hour when an offering is burnt.").define("boon", true));
        this.CHALKDURATION = subscriber.subscribe(builder.comment("How many ingame days are players safe after they successfully use chalk.").defineInRange("chalkDuration", 10, 1, 60));
        builder.pop();
    }


}
