package com.cartoonishvillain.eeriehauntings.events;

import com.cartoonishvillain.eeriehauntings.EerieHauntings;
import com.cartoonishvillain.eeriehauntings.capabilities.playercapability.PlayerCapability;
import com.cartoonishvillain.eeriehauntings.capabilities.playercapability.PlayerCapabilityManager;
import com.cartoonishvillain.eeriehauntings.capabilities.worldcapability.WorldCapability;
import com.cartoonishvillain.eeriehauntings.capabilities.worldcapability.WorldCapabilityManager;
import com.cartoonishvillain.eeriehauntings.networking.lightsoundpackets.LightClientSoundMessenger;
import com.cartoonishvillain.eeriehauntings.networking.lightsoundpackets.LightClientSoundPacket;
import com.cartoonishvillain.eeriehauntings.networking.mediumsoundpackets.MediumClientSoundMessenger;
import com.cartoonishvillain.eeriehauntings.networking.mediumsoundpackets.MediumClientSoundPacket;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = EerieHauntings.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)

public class ForgeBusEvents {


    @SubscribeEvent
    public static void worldRegister(AttachCapabilitiesEvent<Level> event){
        WorldCapabilityManager provider = new WorldCapabilityManager();
        event.addCapability(new ResourceLocation(EerieHauntings.MODID, "worldtimer"), provider);
    }

    @SubscribeEvent
    public static void playerRegister(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject() instanceof Player) {
            PlayerCapabilityManager provider = new PlayerCapabilityManager();
            event.addCapability(new ResourceLocation(EerieHauntings.MODID, "haunting"), provider);
        }
    }


    @SubscribeEvent
    public static void dayNightCheck(TickEvent.WorldTickEvent event) {
        if (!event.world.isClientSide()) {
            event.world.getCapability(WorldCapability.INSTANCE).ifPresent(h -> {
                if(h.isNight() && event.world.isDay()) {
                    h.setisNight(false);
                    //Day Time.
                    ResetGhostAngers(event.world.getServer());

                }
                else if(!h.isNight() && !event.world.isDay()){
                    h.setisNight(true);
                    //Night Time
                    HauntCheck(event.world.getServer());
                }
            });

        }
    }

    @SubscribeEvent
    public static void playerGhostEvent(TickEvent.PlayerTickEvent event){
        float tickcount = event.player.tickCount % 600;
        //event.player.displayClientMessage(new TextComponent(Float.toString(tickcount)), true);
        if(!event.player.level.isClientSide() && event.player.tickCount % 600 == 0){
            //TODO: This is kinda hacky and not great. But it is how I can stop double ticking of events. Find another way eventually.
            event.player.tickCount++;
            event.player.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                if(h.getIsHaunted()) {
                    if (event.player.level.isDay()) {
                        lightEffect((ServerPlayer) event.player);

                    } else {
                        //TODO: Night events
                        if(!h.getAnger()){
                            int chance = event.player.getRandom().nextInt(10);
                            if(chance <= 5){
                                lightEffect((ServerPlayer) event.player);
                            }
                            else if(chance <= 7){
                                //TODO: Moderate Effects.
                                moderateEffect((ServerPlayer) event.player);
                            }
                            else if(chance <= 8){
                                //TODO: Moderate + Light
                                lightEffect((ServerPlayer) event.player);
                                moderateEffect((ServerPlayer) event.player);
                            }
                            else {
                                //TODO: Strong Effect.
                            }

                        }

                        //TODO: Night angry events
                    }
                }
            });
        }
    }


    private static void HauntCheck(MinecraftServer server){
        List<ServerPlayer> players = server.getPlayerList().getPlayers();
        for(ServerPlayer player : players){
            player.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                //if the player isn't haunted and isn't protected from haunting, attempt to haunt
                if(!h.getIsHaunted() && h.getProtectedDays() == 0){
                    //check haunt chance with randomizer
                    if (player.getRandom().nextInt(100) < h.getHauntChance()){
                        //player is haunted! Attatch random ghost type.
                        h.setHaunted(true);
                        h.setGhostType(player.getRandom().nextInt(3)+1);
                    }
                }
                //reset haunt chances for all.
                h.setHauntChance(2);
            });
        }
    }

    private static void ResetGhostAngers(MinecraftServer server){
        List<ServerPlayer> players = server.getPlayerList().getPlayers();
        for(ServerPlayer player : players){
            player.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                h.setAnger(false);
            });
    }
    }

    private static void lightEffect(ServerPlayer player){
        int soundID = player.getRandom().nextInt(EerieHauntings.lowEndSounds.size());
        LightClientSoundMessenger.sendTo(new LightClientSoundPacket(player.getId(), soundID), player);
    }

    private static void moderateEffect(ServerPlayer player){
        int random = player.getRandom().nextInt(3);
        switch (random){
            case 0 -> {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 350, 0));
                player.displayClientMessage(new TranslatableComponent("ghost.moderateslow.alert"), false);
            }
            case 1 -> {
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 350, 0));
                player.displayClientMessage(new TranslatableComponent("ghost.moderateblind.alert"), false);
            }
            case 2 -> {
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 350, 0));
                player.displayClientMessage(new TranslatableComponent("ghost.moderateweakness.alert"), false);
            }
        }
        MediumClientSoundMessenger.sendTo(new MediumClientSoundPacket(player.getId()), player);
    }

    private static void broadcast(MinecraftServer server, Component translationTextComponent){
        server.getPlayerList().broadcastMessage(translationTextComponent, ChatType.CHAT, UUID.randomUUID());
    }
}
