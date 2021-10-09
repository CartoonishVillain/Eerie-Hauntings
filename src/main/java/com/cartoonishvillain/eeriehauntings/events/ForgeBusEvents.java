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
import com.cartoonishvillain.eeriehauntings.networking.shaderupdatepacket.ShaderUpdateMessenger;
import com.cartoonishvillain.eeriehauntings.networking.shaderupdatepacket.ShaderUpdatePacket;
import com.cartoonishvillain.eeriehauntings.networking.strongsoundpackets.StrongClientSoundMessenger;
import com.cartoonishvillain.eeriehauntings.networking.strongsoundpackets.StrongClientSoundPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Mod.EventBusSubscriber(modid = EerieHauntings.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)

public class ForgeBusEvents {


    @SubscribeEvent
    public static void worldRegister(AttachCapabilitiesEvent<World> event){
        if(event.getObject().dimension().toString().contains("overworld")){
        WorldCapabilityManager provider = new WorldCapabilityManager();
        event.addCapability(new ResourceLocation(EerieHauntings.MODID, "worldtimer"), provider);}
    }

    @SubscribeEvent
    public static void playerRegister(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject() instanceof PlayerEntity) {
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
    public static void playerEffectTick(TickEvent.PlayerTickEvent event) {
        if(event.phase.equals(TickEvent.Phase.END)){
            event.player.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                if(h.getVisualEffectTime() > 0){
                    h.setVisualEffectTime(h.getVisualEffectTime() - 1);
                    if(h.getVisualEffectTime() == 0 && event.player.level.isClientSide()) {
                        //if the ticker reaches 0, remove the effect.
                        Minecraft.getInstance().gameRenderer.shutdownEffect();
                        h.setEffectID(0);
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void playerGhostEvent(TickEvent.PlayerTickEvent event) {

        if (!event.player.level.isClientSide() && event.phase.equals(TickEvent.Phase.END)) {
            event.player.getCapability(PlayerCapability.INSTANCE).ifPresent(h -> {

                if (h.getIsHaunted() && h.checkHauntActionTicks()) {
                    if (event.player.level.isDay()) {
                        lightEffect((ServerPlayerEntity) event.player);

                    } else {
                        if (!h.getAnger()) {
                            int chance = event.player.getRandom().nextInt(10);
                            if (chance <= 5) {
                                lightEffect((ServerPlayerEntity) event.player);
                            } else if (chance <= 7) {
                                moderateEffect((ServerPlayerEntity) event.player);
                            } else if (chance <= 8) {
                                lightEffect((ServerPlayerEntity) event.player);
                                moderateEffect((ServerPlayerEntity) event.player);
                            } else {
                                strongEffect((ServerPlayerEntity) event.player);
                            }

                        }

                        //If angered, it'll lean towards stronger events.
                        else {
                            int chance = event.player.getRandom().nextInt(10);
                            if (chance <= 2) {
                                lightEffect((ServerPlayerEntity) event.player);
                            } else if (chance <= 5) {
                                moderateEffect((ServerPlayerEntity) event.player);
                            } else if (chance <= 7) {
                                lightEffect((ServerPlayerEntity) event.player);
                                moderateEffect((ServerPlayerEntity) event.player);
                            } else {
                                strongEffect((ServerPlayerEntity) event.player);
                            }
                        }
                    }
                    h.setHauntActionTicks(event.player.getRandom().nextInt(EerieHauntings.serverConfig.MAXIMUMEFFECTWAIT.get() - EerieHauntings.serverConfig.MINIMUMEFFECTWAIT.get()) + EerieHauntings.serverConfig.MINIMUMEFFECTWAIT.get());

                    if(event.player.getMainHandItem().getItem().equals(Register.OLDRADIO.get()) && h.getGhostType() != 1 && h.getGhostType() != 0){
                        event.player.getCooldowns().addCooldown(Register.OLDRADIO.get(), 100);
                        event.player.level.playSound(null, event.player.blockPosition(), Register.RADIOSOUND.get(), SoundCategory.MASTER, 1, 1);
                        event.player.displayClientMessage(new TranslationTextComponent("item.eeriehauntings.radio").withStyle(TextFormatting.GOLD), true);
                    }
                    else if(event.player.getMainHandItem().getItem().equals(Register.EMFCOUNTER.get()) && h.getGhostType() != 2 && h.getGhostType() != 0){
                        event.player.getCooldowns().addCooldown(Register.EMFCOUNTER.get(), 100);
                        event.player.level.playSound(null, event.player.blockPosition(), Register.EMFCOUNTERSOUNDS.get(), SoundCategory.MASTER, 1, 1);
                        event.player.displayClientMessage(new TranslationTextComponent("item.eeriehauntings.emf").withStyle(TextFormatting.GOLD), true);
                    }

                }
                if(!h.getIsHaunted()) h.setHauntActionTicks(0);
            });
        }
    }

    @SubscribeEvent
    public static void entityKilled(LivingDeathEvent event){
        //The death of entities may cause a user to be haunted... Some entities are stronger willed than others.
        LivingEntity victim = event.getEntityLiving();
        if(!victim.level.isClientSide() && event.getSource().getDirectEntity() instanceof PlayerEntity) {
            PlayerEntity aggressor = (PlayerEntity) event.getSource().getDirectEntity();
            float hauntIncrease;
            if(victim instanceof VillagerEntity){hauntIncrease = EerieHauntings.serverConfig.VILLAGERHAUNTCHANCE.get().floatValue();}
            else if(victim instanceof PlayerEntity){hauntIncrease = EerieHauntings.serverConfig.PLAYERHAUNTCHANCE.get().floatValue();}
            else if(victim instanceof GolemEntity){hauntIncrease = EerieHauntings.serverConfig.GOLEMHAUNTCHANCE.get().floatValue();}
            else if(victim instanceof TameableEntity) {hauntIncrease = EerieHauntings.serverConfig.PETHAUNTCHANCE.get().floatValue();}
            else {hauntIncrease = EerieHauntings.serverConfig.MISCHAUNTCHANCE.get().floatValue();}

            aggressor.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                h.addHauntChance(hauntIncrease);
            });
        }
    }

    @SubscribeEvent
    public static void playerJoinEvent(EntityJoinWorldEvent event){
        if(!event.getWorld().isClientSide() && event.getEntity() instanceof PlayerEntity){
            event.getEntity().getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                if(h.getVisualEffectTime() > 0){
                    ShaderUpdateMessenger.sendTo(new ShaderUpdatePacket(event.getEntity().getId(), h.getVisualEffectTime(), h.getEffectID()), (PlayerEntity) event.getEntity());
                }
            });
        }
    }

    @SubscribeEvent
    public static void ItemToolTips(ItemTooltipEvent event){
        if(event.getItemStack().getItem().equals(Register.UNEARTHLYSHARD.get())){
            event.getToolTip().add(new TranslationTextComponent("info.eeriehauntings.shard").withStyle(TextFormatting.GOLD));
            event.getToolTip().add(new TranslationTextComponent("info.eeriehauntings.shardgain").withStyle(TextFormatting.RED));
        }
    }


    @SubscribeEvent
    public static void playerCloneEvent(PlayerEvent.Clone event){
        if(!event.isWasDeath()){
            //runs whenever player gets out of end
            PlayerEntity originalPlayer = event.getOriginal();
            PlayerEntity newPlayer = event.getPlayer();

             AtomicBoolean haunted = new AtomicBoolean(false);
             AtomicBoolean anger = new AtomicBoolean(false);
             AtomicReference<Float> hauntChance = new AtomicReference<>((float) 1);
             AtomicInteger ghostType = new AtomicInteger();
             AtomicInteger protectedDays = new AtomicInteger();
             AtomicInteger hauntTicks = new AtomicInteger();
             AtomicInteger effectTicks = new AtomicInteger();
             AtomicInteger effectID = new AtomicInteger();

             originalPlayer.revive();

            //variables to store info

            //code refuses to enter this code block, despite the capability showing up in variable viewer, and being active prior to entering the end portal
            originalPlayer.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                //gather info
                haunted.set(h.getIsHaunted());
                anger.set(h.getAnger());
                hauntChance.set(h.getHauntChance());
                ghostType.set(h.getGhostType());
                protectedDays.set(h.getProtectedDays());
                hauntTicks.set(h.getHauntActionTicks());
                effectTicks.set(h.getVisualEffectTime());
                effectID.set(h.getEffectID());
            });
            originalPlayer.kill();
            //code enters just fine
            newPlayer.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                //set info
                h.setHaunted(haunted.get());
                h.setAnger(anger.get());
                h.setHauntChance(hauntChance.get());
                h.setGhostType(ghostType.get());
                h.setProtectedDays(protectedDays.get());
                h.setHauntActionTicks(hauntTicks.get());
                h.setVisualEffectTime(effectTicks.get());
                h.setEffectID(effectID.get());
            });
        }
    }



    private static void HauntCheck(MinecraftServer server){
        List<ServerPlayerEntity> players = server.getPlayerList().getPlayers();
        for(ServerPlayerEntity player : players){
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
                if(h.getProtectedDays() > 0){h.setProtectedDays(h.getProtectedDays() - 1);}
                //reset haunt chances for all.
                h.setHauntChance(EerieHauntings.serverConfig.NORMALHAUNTCHANCE.get().floatValue());
            });
        }
    }

    private static void ResetGhostAngers(MinecraftServer server){
        List<ServerPlayerEntity> players = server.getPlayerList().getPlayers();
        for(ServerPlayerEntity player : players){
            player.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                h.setAnger(false);
            });
    }
    }


    private static void lightEffect(ServerPlayerEntity player){
        int soundID = player.getRandom().nextInt(EerieHauntings.lowEndSounds.size());
        LightClientSoundMessenger.sendTo(new LightClientSoundPacket(player.getId(), soundID), player);
    }

    private static void moderateEffect(ServerPlayerEntity player){
        if(EerieHauntings.serverConfig.MEDIUMEFFECT.get()) {
            int random = player.getRandom().nextInt(3);
            switch (random) {
                case 0:
                    player.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 350, 0));
//                player.displayClientMessage(new TranslationTextComponent()("ghost.moderateslow.alert"), false);
                    break;
                case 1:
                    player.addEffect(new EffectInstance(Effects.BLINDNESS, 350, 0));
//                player.displayClientMessage(new TranslationTextComponent()("ghost.moderateblind.alert"), false);
                    break;
                case 2:
                    player.addEffect(new EffectInstance(Effects.WEAKNESS, 350, 0));
//                player.displayClientMessage(new TranslationTextComponent()("ghost.moderateweakness.alert"), false);
                    break;
            }
            MediumClientSoundMessenger.sendTo(new MediumClientSoundPacket(player.getId()), player);
        }else {lightEffect(player);}
    }

    private static void strongEffect(ServerPlayerEntity player){
        if(EerieHauntings.serverConfig.STRONGEFFECT.get()) {
            int random = player.getRandom().nextInt(3);
            switch (random) {
                case 0: {
                    player.addEffect(new EffectInstance(Effects.LEVITATION, 200, 0));
                    player.getCapability(PlayerCapability.INSTANCE).ifPresent(h -> {
                        h.setEffectID(1);
                        h.setVisualEffectTime(200);
                        ShaderUpdateMessenger.sendTo(new ShaderUpdatePacket(player.getId(), 200, 1), player);
                    });
                    break;
//                player.displayClientMessage(new TranslationTextComponent()("ghost.stronglevitate.alert"), false);
                }
                case 1: {
                    player.addEffect(new EffectInstance(Effects.CONFUSION, 200, 0));
//                player.displayClientMessage(new TranslationTextComponent()("ghost.strongconfusion.alert"), false);
                    break;
                }
                case 2: {
                    player.addEffect(new EffectInstance(Effects.HUNGER, 200, 0));
                    player.getCapability(PlayerCapability.INSTANCE).ifPresent(h -> {
                        h.setEffectID(2);
                        h.setVisualEffectTime(200);
                        ShaderUpdateMessenger.sendTo(new ShaderUpdatePacket(player.getId(), 200, 2), player);
                    });
//                player.displayClientMessage(new TranslationTextComponent()("ghost.stronghunger.alert"), false);
                    break;
                }
            }
            StrongClientSoundMessenger.sendTo(new StrongClientSoundPacket(player.getId()), player);
        }else {lightEffect(player);}
    }

    //Ghost removal w/o making the ghost angry (or letting it calm down first)
    public static void exorciseGhost(ServerPlayerEntity player){
        player.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
            h.setHaunted(false);
            h.setGhostType(0);
            ItemEntity item = new ItemEntity(EntityType.ITEM, player.level);
            item.setItem(new ItemStack(Register.UNEARTHLYSHARD.get()));
            item.setPos(player.getX(), player.getY(), player.getZ());
            player.level.addFreshEntity(item);
        });
    }

    //Ghost removal while angry. Doesn't drop an unearthlyShard
    public static void expellGhost(ServerPlayerEntity player){
        player.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
            h.setHaunted(false);
            h.setGhostType(0);
        });
    }

    //Ghost removal with an offering. Doesn't drop an unearthlyShard. Buffs the player.
    public static void boonExpelGhost(ServerPlayerEntity player){
        player.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
            h.setHaunted(false);
            h.setGhostType(0);
            if(EerieHauntings.serverConfig.BOON.get()) {
                int rand = player.getRandom().nextInt(6);
                switch (rand) {
                    case 0: {
                        player.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 72000, EerieHauntings.serverConfig.BOONSTRENGTH.get(), false, false));
                        player.displayClientMessage(new TranslationTextComponent("boon.eeriehauntings.speed").withStyle(TextFormatting.AQUA), false);
                        break;
                    }
                    case 1: {
                        player.addEffect(new EffectInstance(Effects.DIG_SPEED, 72000, EerieHauntings.serverConfig.BOONSTRENGTH.get(), false, false));
                        player.displayClientMessage(new TranslationTextComponent("boon.eeriehauntings.haste").withStyle(TextFormatting.YELLOW), false);
                        break;
                    }
                    case 2: {
                        player.addEffect(new EffectInstance(Effects.JUMP, 72000, EerieHauntings.serverConfig.BOONSTRENGTH.get(), false, false));
                        player.displayClientMessage(new TranslationTextComponent("boon.eeriehauntings.jump").withStyle(TextFormatting.GREEN), false);
                        break;
                    }
                    case 3: {
                        player.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 72000, EerieHauntings.serverConfig.BOONSTRENGTH.get(), false, false));
                        player.displayClientMessage(new TranslationTextComponent("boon.eeriehauntings.resistance").withStyle(TextFormatting.RED), false);
                        break;
                    }
                    case 4: {
                        player.addEffect(new EffectInstance(Effects.HEALTH_BOOST, 72000, 1 + EerieHauntings.serverConfig.BOONSTRENGTH.get(), false, false));
                        player.displayClientMessage(new TranslationTextComponent("boon.eeriehauntings.life").withStyle(TextFormatting.LIGHT_PURPLE), false);
                        break;
                    }
                    case 5: {
                        player.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 72000, EerieHauntings.serverConfig.BOONSTRENGTH.get(), false, false));
                        player.displayClientMessage(new TranslationTextComponent("boon.eeriehauntings.strength").withStyle(TextFormatting.DARK_RED), false);
                        break;
                    }
                }
            } else player.displayClientMessage(new TranslationTextComponent("boon.eeriehauntings.disabled").withStyle(TextFormatting.DARK_RED), false);
        });
    }

    private static void broadcast(MinecraftServer server, TextComponent translationTextComponent){
        server.getPlayerList().broadcastMessage(translationTextComponent, ChatType.CHAT, UUID.randomUUID());
    }
}
