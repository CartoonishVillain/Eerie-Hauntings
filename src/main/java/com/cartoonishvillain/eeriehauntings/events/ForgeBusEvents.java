package com.cartoonishvillain.eeriehauntings.events;

import com.cartoonishvillain.eeriehauntings.EerieHauntings;
import com.cartoonishvillain.eeriehauntings.Register;
import com.cartoonishvillain.eeriehauntings.capabilities.playercapability.PlayerCapability;
import com.cartoonishvillain.eeriehauntings.capabilities.playercapability.PlayerCapabilityManager;
import com.cartoonishvillain.eeriehauntings.capabilities.worldcapability.WorldCapability;
import com.cartoonishvillain.eeriehauntings.capabilities.worldcapability.WorldCapabilityManager;
import com.cartoonishvillain.eeriehauntings.networking.lightsoundpackets.LightClientSoundMessenger;
import com.cartoonishvillain.eeriehauntings.networking.lightsoundpackets.LightClientSoundPacket;
import com.cartoonishvillain.eeriehauntings.networking.mediumsoundpackets.MediumClientSoundMessenger;
import com.cartoonishvillain.eeriehauntings.networking.mediumsoundpackets.MediumClientSoundPacket;
import com.cartoonishvillain.eeriehauntings.networking.strongsoundpackets.StrongClientSoundMessenger;
import com.cartoonishvillain.eeriehauntings.networking.strongsoundpackets.StrongClientSoundPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
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
    public static void playerGhostEvent(TickEvent.PlayerTickEvent event) {

        if (!event.player.level.isClientSide() && event.phase.equals(TickEvent.Phase.END)) {
            event.player.getCapability(PlayerCapability.INSTANCE).ifPresent(h -> {
                if (h.checkHauntActionTicks())
                if (h.getIsHaunted() && h.checkHauntActionTicks()) {
                    if (event.player.level.isDay()) {
                        lightEffect((ServerPlayer) event.player);

                    } else {
                        if (!h.getAnger()) {
                            int chance = event.player.getRandom().nextInt(10);
                            if (chance <= 5) {
                                lightEffect((ServerPlayer) event.player);
                            } else if (chance <= 7) {
                                moderateEffect((ServerPlayer) event.player);
                            } else if (chance <= 8) {
                                lightEffect((ServerPlayer) event.player);
                                moderateEffect((ServerPlayer) event.player);
                            } else {
                                strongEffect((ServerPlayer) event.player);
                            }

                        }

                        //If angered, it'll lean towards stronger events.
                        else {
                            int chance = event.player.getRandom().nextInt(10);
                            if (chance <= 2) {
                                lightEffect((ServerPlayer) event.player);
                            } else if (chance <= 5) {
                                moderateEffect((ServerPlayer) event.player);
                            } else if (chance <= 7) {
                                lightEffect((ServerPlayer) event.player);
                                moderateEffect((ServerPlayer) event.player);
                            } else {
                                strongEffect((ServerPlayer) event.player);
                            }
                        }
                    }
                    h.setHauntActionTicks(event.player.getRandom().nextInt(750 - 400) + 400);

                    if(event.player.getMainHandItem().getItem().equals(Register.OLDRADIO.get()) && h.getGhostType() != 1 && h.getGhostType() != 0){
                        event.player.getCooldowns().addCooldown(Register.OLDRADIO.get(), 100);
                        event.player.level.playSound(null, event.player.getOnPos(), Register.RADIOSOUND.get(), SoundSource.MASTER, 1, 1);
                        event.player.displayClientMessage(new TranslatableComponent("item.eeriehauntings.radio").withStyle(ChatFormatting.GOLD), true);
                    }
                    else if(event.player.getMainHandItem().getItem().equals(Register.EMFCOUNTER.get()) && h.getGhostType() != 2 && h.getGhostType() != 0){
                        event.player.getCooldowns().addCooldown(Register.EMFCOUNTER.get(), 100);
                        event.player.level.playSound(null, event.player.getOnPos(), Register.EMFCOUNTERSOUNDS.get(), SoundSource.MASTER, 1, 1);
                        event.player.displayClientMessage(new TranslatableComponent("item.eeriehauntings.emf").withStyle(ChatFormatting.GOLD), true);
                    }

                }
            });
        }
    }

    @SubscribeEvent
    public static void entityKilled(LivingDeathEvent event){
        //The death of entities may cause a user to be haunted... Some entities are stronger willed than others.
        LivingEntity victim = event.getEntityLiving();
        if(!victim.level.isClientSide() && event.getSource().getDirectEntity() instanceof Player) {
            Player aggressor = (Player) event.getSource().getDirectEntity();
            float hauntIncrease;
            if(victim instanceof Villager){hauntIncrease = 10f;}
            else if(victim instanceof Player){hauntIncrease = 25f;}
            else if(victim instanceof AbstractGolem){hauntIncrease = 7.5f;}
            else if(victim instanceof Wolf || victim instanceof Axolotl || victim instanceof Cat || victim instanceof Parrot) {hauntIncrease = 12.5f;}
            else {hauntIncrease = 1f;}

            aggressor.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                h.addHauntChance(hauntIncrease);
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
                h.setHauntChance(1);
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
//                player.displayClientMessage(new TranslatableComponent("ghost.moderateslow.alert"), false);
            }
            case 1 -> {
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 350, 0));
//                player.displayClientMessage(new TranslatableComponent("ghost.moderateblind.alert"), false);
            }
            case 2 -> {
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 350, 0));
//                player.displayClientMessage(new TranslatableComponent("ghost.moderateweakness.alert"), false);
            }
        }
        MediumClientSoundMessenger.sendTo(new MediumClientSoundPacket(player.getId()), player);
    }

    private static void strongEffect(ServerPlayer player){
        int random = player.getRandom().nextInt(3);
        switch (random){
            case 0 ->{
                player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 200, 0));
//                player.displayClientMessage(new TranslatableComponent("ghost.stronglevitate.alert"), false);
            }
            case 1 ->{
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
//                player.displayClientMessage(new TranslatableComponent("ghost.strongconfusion.alert"), false);
            }
            case 2 ->{
                player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 200, 0));
//                player.displayClientMessage(new TranslatableComponent("ghost.stronghunger.alert"), false);
            }
        }
        StrongClientSoundMessenger.sendTo(new StrongClientSoundPacket(player.getId()), player);
    }

    private static void broadcast(MinecraftServer server, Component translationTextComponent){
        server.getPlayerList().broadcastMessage(translationTextComponent, ChatType.CHAT, UUID.randomUUID());
    }
}
