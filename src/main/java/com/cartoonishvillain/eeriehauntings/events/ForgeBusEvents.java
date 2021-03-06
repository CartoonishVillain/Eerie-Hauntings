package com.cartoonishvillain.eeriehauntings.events;

import com.cartoonishvillain.eeriehauntings.EerieHauntings;
import com.cartoonishvillain.eeriehauntings.Register;
import com.cartoonishvillain.eeriehauntings.capabilities.playercapability.PlayerCapability;
import com.cartoonishvillain.eeriehauntings.capabilities.playercapability.PlayerCapabilityManager;
import com.cartoonishvillain.eeriehauntings.capabilities.worldcapability.WorldCapability;
import com.cartoonishvillain.eeriehauntings.capabilities.worldcapability.WorldCapabilityManager;
import com.cartoonishvillain.eeriehauntings.commands.*;
import com.cartoonishvillain.eeriehauntings.networking.lightsoundpackets.LightClientSoundMessenger;
import com.cartoonishvillain.eeriehauntings.networking.lightsoundpackets.LightClientSoundPacket;
import com.cartoonishvillain.eeriehauntings.networking.mediumsoundpackets.MediumClientSoundMessenger;
import com.cartoonishvillain.eeriehauntings.networking.mediumsoundpackets.MediumClientSoundPacket;
import com.cartoonishvillain.eeriehauntings.networking.shaderupdatepacket.ShaderUpdateMessenger;
import com.cartoonishvillain.eeriehauntings.networking.shaderupdatepacket.ShaderUpdatePacket;
import com.cartoonishvillain.eeriehauntings.networking.strongsoundpackets.StrongClientSoundMessenger;
import com.cartoonishvillain.eeriehauntings.networking.strongsoundpackets.StrongClientSoundPacket;
import io.github.noeppi_noeppi.mods.torment.cap.TormentData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
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
    public static void serverLoad(RegisterCommandsEvent event){
        HelpCommand.register(event.getDispatcher());
        ForceHauntCommand.register(event.getDispatcher());
        RemoveHauntCommand.register(event.getDispatcher());
        ToggleAngerCommand.register(event.getDispatcher());
        SetHauntChance.register(event.getDispatcher());
        SetProtectionDays.register(event.getDispatcher());
    }


    @SubscribeEvent
    public static void worldRegister(AttachCapabilitiesEvent<Level> event){
        if(event.getObject().dimension().toString().contains("overworld")){
        WorldCapabilityManager provider = new WorldCapabilityManager();
        event.addCapability(new ResourceLocation(EerieHauntings.MODID, "worldtimer"), provider);}
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
    public static void playerEffectTick(TickEvent.PlayerTickEvent event) {
        if(event.phase.equals(TickEvent.Phase.END)){
            event.player.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                if(h.getVisualEffectTime() > 0){
                    h.setVisualEffectTime(h.getVisualEffectTime() - 1);
                    if((h.getVisualEffectTime() == 0 || !ValidPlayer(event.player)) && event.player.level.isClientSide()) {
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

                if(ValidPlayer(event.player)) {
                    boolean spookyForest = false;
                    if(EerieHauntings.spookyBiomesInstalled && playerInSpookyForest(event.player)) {spookyForest = true;}
                    if (h.getIsHaunted() && h.checkHauntActionTicks()) {
                        if (event.player.level.isDay() && !spookyForest) {
                            lightEffect((ServerPlayer) event.player);
                        } else {
                            if (!h.getAnger()) {
                                int chance = event.player.getRandom().nextInt(10);
                                if (chance <= 5) {
                                    lightEffect((ServerPlayer) event.player);
                                } else if (chance <= 7) {
                                    moderateEffect((ServerPlayer) event.player, true);
                                } else if (chance <= 8) {
                                    lightEffect((ServerPlayer) event.player);
                                    moderateEffect((ServerPlayer) event.player, true);
                                } else {
                                    strongEffect((ServerPlayer) event.player, true);
                                }

                            }

                            //If angered, it'll lean towards stronger events.
                            else {
                                int chance = event.player.getRandom().nextInt(10);
                                if (chance <= 2) {
                                    lightEffect((ServerPlayer) event.player);
                                } else if (chance <= 5) {
                                    moderateEffect((ServerPlayer) event.player, true);
                                } else if (chance <= 7) {
                                    lightEffect((ServerPlayer) event.player);
                                    moderateEffect((ServerPlayer) event.player, true);
                                } else {
                                    strongEffect((ServerPlayer) event.player, true);
                                }
                            }
                        }
                        h.setHauntActionTicks(event.player.getRandom().nextInt(EerieHauntings.serverConfig.MAXIMUMEFFECTWAIT.get() - EerieHauntings.serverConfig.MINIMUMEFFECTWAIT.get()) + EerieHauntings.serverConfig.MINIMUMEFFECTWAIT.get());

                        if (event.player.getMainHandItem().getItem().equals(Register.OLDRADIO.get()) && h.getGhostType() != 1 && h.getGhostType() != 0) {
                            event.player.getCooldowns().addCooldown(Register.OLDRADIO.get(), 100);
                            event.player.level.playSound(null, event.player.getOnPos(), Register.RADIOSOUND.get(), SoundSource.MASTER, 1, 1);
                            event.player.displayClientMessage(new TranslatableComponent("item.eeriehauntings.radio").withStyle(ChatFormatting.GOLD), true);
                        } else if (event.player.getMainHandItem().getItem().equals(Register.EMFCOUNTER.get()) && h.getGhostType() != 2 && h.getGhostType() != 0) {
                            event.player.getCooldowns().addCooldown(Register.EMFCOUNTER.get(), 100);
                            event.player.level.playSound(null, event.player.getOnPos(), Register.EMFCOUNTERSOUNDS.get(), SoundSource.MASTER, 1, 1);
                            event.player.displayClientMessage(new TranslatableComponent("item.eeriehauntings.emf").withStyle(ChatFormatting.GOLD), true);
                        }

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
        if(!victim.level.isClientSide() && event.getSource().getDirectEntity() instanceof Player) {
            Player aggressor = (Player) event.getSource().getDirectEntity();
            float hauntIncrease;
            if(victim instanceof Villager){hauntIncrease = EerieHauntings.serverConfig.VILLAGERHAUNTCHANCE.get().floatValue();}
            else if(victim instanceof Player){hauntIncrease = EerieHauntings.serverConfig.PLAYERHAUNTCHANCE.get().floatValue();}
            else if(victim instanceof AbstractGolem){hauntIncrease = EerieHauntings.serverConfig.GOLEMHAUNTCHANCE.get().floatValue();}
            else if(victim instanceof TamableAnimal) {hauntIncrease = EerieHauntings.serverConfig.PETHAUNTCHANCE.get().floatValue();}
            else {hauntIncrease = EerieHauntings.serverConfig.MISCHAUNTCHANCE.get().floatValue();}

            aggressor.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                h.addHauntChance(hauntIncrease);
            });
        }
    }

    @SubscribeEvent
    public static void playerJoinEvent(EntityJoinWorldEvent event){
        if(!event.getWorld().isClientSide() && event.getEntity() instanceof Player && ValidPlayer((Player) event.getEntity())){
            event.getEntity().getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                if(h.getVisualEffectTime() > 0){
                    ShaderUpdateMessenger.sendTo(new ShaderUpdatePacket(event.getEntity().getId(), h.getVisualEffectTime(), h.getEffectID()), (Player) event.getEntity());
                }
            });
        }
    }

    @SubscribeEvent
    public static void ItemToolTips(ItemTooltipEvent event){
        if(event.getItemStack().getItem().equals(Register.UNEARTHLYSHARD.get())){
            event.getToolTip().add(new TranslatableComponent("info.eeriehauntings.shard").withStyle(ChatFormatting.GOLD));
            event.getToolTip().add(new TranslatableComponent("info.eeriehauntings.shardgain").withStyle(ChatFormatting.RED));
        }
    }


    @SubscribeEvent
    public static void playerCloneEvent(PlayerEvent.Clone event){
        if(!event.isWasDeath()){
            //runs whenever player gets out of end
            Player originalPlayer = event.getOriginal();
            Player newPlayer = event.getPlayer();

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
        List<ServerPlayer> players = server.getPlayerList().getPlayers();
        for(ServerPlayer player : players){
            player.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                //if the player isn't haunted and isn't protected from haunting, attempt to haunt
                if(!h.getIsHaunted() && h.getProtectedDays() == 0){

                    //if torment is installed and compat is on, run the torment method
                    if(EerieHauntings.tormentInstalled){tormentModifier(player);}

                    if(EerieHauntings.spookyBiomesInstalled && playerInSpookyForest(player)){ghostlyBiomeModifier(player);}

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

    private static void moderateEffect(ServerPlayer player, boolean notRecycled){
        if(EerieHauntings.serverConfig.MEDIUMEFFECT.get()) {
            int random;
            if(EerieHauntings.serverConfig.PHYSEFFECT.get() && notRecycled) random = player.getRandom().nextInt(5);
            else random = player.getRandom().nextInt(3);
            switch (random) {
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
                case 3 -> {
                    if (!leverWork(player)) {
                        moderateEffect(player, false);
                        return;
                    }
                    break;
                }
                case 4 -> {
                    if (!buttonWork(player)) {
                        moderateEffect(player, false);
                        return;
                    }
                    break;
                }
            }
            MediumClientSoundMessenger.sendTo(new MediumClientSoundPacket(player.getId()), player);
        }else {lightEffect(player);}
    }

    private static void strongEffect(ServerPlayer player, boolean notRecycled){
        if(EerieHauntings.serverConfig.STRONGEFFECT.get()) {
            int random;
            if (EerieHauntings.serverConfig.PHYSEFFECT.get() && notRecycled) random = player.getRandom().nextInt(4);
            else random = player.getRandom().nextInt(3);
            switch (random) {
                case 0 -> {
                    player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 200, 0));
                    player.getCapability(PlayerCapability.INSTANCE).ifPresent(h -> {
                        h.setEffectID(1);
                        h.setVisualEffectTime(200);
                        ShaderUpdateMessenger.sendTo(new ShaderUpdatePacket(player.getId(), 200, 1), player);
                    });
//                player.displayClientMessage(new TranslatableComponent("ghost.stronglevitate.alert"), false);
                }
                case 1 -> {
                    player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 350, 0));
                    player.getCapability(PlayerCapability.INSTANCE).ifPresent(h -> {
                        h.setEffectID(3);
                        h.setVisualEffectTime(350);
                        ShaderUpdateMessenger.sendTo(new ShaderUpdatePacket(player.getId(), 350, 3), player);
                    });
//                player.displayClientMessage(new TranslatableComponent("ghost.strongconfusion.alert"), false);
                }
                case 2 -> {
                    player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 200, 0));
                    player.getCapability(PlayerCapability.INSTANCE).ifPresent(h -> {
                        h.setEffectID(2);
                        h.setVisualEffectTime(200);
                        ShaderUpdateMessenger.sendTo(new ShaderUpdatePacket(player.getId(), 200, 2), player);
                    });
//                player.displayClientMessage(new TranslatableComponent("ghost.stronghunger.alert"), false);
                }
                case 3 -> {
                    if(!doorWork(player)){
                        strongEffect(player, false);
                        return;
                    }
                    break;
                }
            }
            StrongClientSoundMessenger.sendTo(new StrongClientSoundPacket(player.getId()), player);
        }else {lightEffect(player);}
    }

    //Ghost removal w/o making the ghost angry (or letting it calm down first)
    public static void exorciseGhost(ServerPlayer player){
        player.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
            if (!MinecraftForge.EVENT_BUS.post(new GhostExorcisedEvent(player, h.getGhostType(), false))) {
                h.setHaunted(false);
                h.setGhostType(0);
                ItemEntity item = new ItemEntity(EntityType.ITEM, player.level);
                item.setItem(new ItemStack(Register.UNEARTHLYSHARD.get()));
                item.setPos(player.getX(), player.getY(), player.getZ());
                player.level.addFreshEntity(item);
            }
        });
    }

    //Ghost removal while angry. Doesn't drop an unearthlyShard
    public static void expellGhost(ServerPlayer player){
        player.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
            if (!MinecraftForge.EVENT_BUS.post(new GhostExorcisedEvent(player, h.getGhostType(), true))) {
                h.setHaunted(false);
                h.setGhostType(0);
            }
        });
    }

    //Ghost removal with an offering. Doesn't drop an unearthlyShard. Buffs the player.
    public static void boonExpelGhost(ServerPlayer player){
        player.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
            h.setHaunted(false);
            h.setGhostType(0);
            if(EerieHauntings.serverConfig.BOON.get()) {
                int rand = player.getRandom().nextInt(6);
                switch (rand) {
                    case 0 -> {
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, Integer.MAX_VALUE,  EerieHauntings.serverConfig.BOONSTRENGTH.get(), false, false));
                        player.displayClientMessage(new TranslatableComponent("boon.eeriehauntings.speed").withStyle(ChatFormatting.AQUA), false);
                    }
                    case 1 -> {
                        player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, Integer.MAX_VALUE, EerieHauntings.serverConfig.BOONSTRENGTH.get(), false, false));
                        player.displayClientMessage(new TranslatableComponent("boon.eeriehauntings.haste").withStyle(ChatFormatting.YELLOW), false);
                    }
                    case 2 -> {
                        player.addEffect(new MobEffectInstance(MobEffects.JUMP, Integer.MAX_VALUE, 1 + EerieHauntings.serverConfig.BOONSTRENGTH.get(), false, false));
                        player.displayClientMessage(new TranslatableComponent("boon.eeriehauntings.jump").withStyle(ChatFormatting.GREEN), false);
                    }
                    case 3 -> {
                        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, Integer.MAX_VALUE, EerieHauntings.serverConfig.BOONSTRENGTH.get(), false, false));
                        player.displayClientMessage(new TranslatableComponent("boon.eeriehauntings.resistance").withStyle(ChatFormatting.RED), false);
                    }
                    case 4 -> {
                        player.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, Integer.MAX_VALUE, 1 + EerieHauntings.serverConfig.BOONSTRENGTH.get(), false, false));
                        player.displayClientMessage(new TranslatableComponent("boon.eeriehauntings.life").withStyle(ChatFormatting.LIGHT_PURPLE), false);
                    }
                    case 5 -> {
                        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, Integer.MAX_VALUE, EerieHauntings.serverConfig.BOONSTRENGTH.get(), false, false));
                        player.displayClientMessage(new TranslatableComponent("boon.eeriehauntings.strength").withStyle(ChatFormatting.DARK_RED), false);
                    }
                }
            } else player.displayClientMessage(new TranslatableComponent("boon.eeriehauntings.disabled").withStyle(ChatFormatting.DARK_RED), false);
        });
    }


    private static boolean doorWork(Player player){
        boolean open = player.getRandom().nextBoolean();
        boolean worked = false;
        int range = EerieHauntings.serverConfig.PHYSEFFECTRADIUS.get();
        for(int i = (int) (player.getX() - range); i < player.getX()+range; i++){
            for(int j = (int) (player.getY() - range); j < player.getY()+range; j++){
                for(int k = (int) player.getZ() - range; k < player.getZ()+range; k++){
                    BlockPos blockPos = new BlockPos(i, j, k);
                    BlockState blockState = player.level.getBlockState(blockPos);
                    if (blockState.getBlock() instanceof DoorBlock && blockState.is(BlockTags.WOODEN_DOORS)) {
                        ((DoorBlock) blockState.getBlock()).setOpen(null, player.level, blockState, blockPos, open); worked = true;}
                }
            }
        }

        return worked;
    }


    private static boolean leverWork(Player player){
        boolean worked = false;
        int range = EerieHauntings.serverConfig.PHYSEFFECTRADIUS.get();
        for(int i = (int) (player.getX() - range); i < player.getX()+range; i++){
            for(int j = (int) (player.getY() - range); j < player.getY()+range; j++){
                for(int k = (int) player.getZ() - range; k < player.getZ()+range; k++){
                    BlockPos blockPos = new BlockPos(i, j, k);
                    BlockState blockState = player.level.getBlockState(blockPos);
                    if (blockState.getBlock() instanceof LeverBlock) {
                        ((LeverBlock) blockState.getBlock()).pull(blockState, player.level, blockPos);
                        worked = true;
                    }
                }
            }
        }
        return worked;
    }

    private static boolean buttonWork(Player player){
        boolean worked = false;
        int range = EerieHauntings.serverConfig.PHYSEFFECTRADIUS.get();
        for(int i = (int) (player.getX() - range); i < player.getX()+range; i++){
            for(int j = (int) (player.getY() - range); j < player.getY()+range; j++){
                for(int k = (int) player.getZ() - range; k < player.getZ()+range; k++){
                    BlockPos blockPos = new BlockPos(i, j, k);
                    BlockState blockState = player.level.getBlockState(blockPos);
                    if (blockState.getBlock() instanceof ButtonBlock) {
                        ((ButtonBlock) blockState.getBlock()).press(blockState, player.level, blockPos);
                        worked = true;
                    }
                }
            }
        }
        return worked;
    }

    private static void tormentModifier(ServerPlayer player){
        //gets the torment data of the relevant player
        TormentData tormentData = TormentData.get(player);
        player.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
            //For every point of torment I want the multiplier to be increased by 0.05;
            float multiplier = EerieHauntings.commonConfig.TORMENTCOMPATVALUE.get().floatValue() * tormentData.getTormentLevel();
            //add one so the multiplier increases the haunt chance when applied.
            multiplier += 1;
            //multiply haunt chance with the torment multiplier and set the new haunt chance before the check.
            float multiplied = h.getHauntChance() * multiplier;
            h.setHauntChance(multiplied);
        });
    }

    private static void broadcast(MinecraftServer server, Component translationTextComponent){
        server.getPlayerList().broadcastMessage(translationTextComponent, ChatType.CHAT, UUID.randomUUID());
    }

    private static boolean ValidPlayer(Player player){
        return !player.isCreative() && !player.isSpectator();
    }

    private static boolean playerInSpookyForest(Player player){
        return player.level.getBiome(player.getOnPos()).value().getRegistryName().toString().equals("spookybiomes:ghostly_forest");
    }

    private static void ghostlyBiomeModifier(ServerPlayer player){
        player.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
            float multiplier = EerieHauntings.commonConfig.SPOOKYCOMPATVALUE.get().floatValue();
            float multiplied = h.getHauntChance() * multiplier;
            h.setHauntChance(multiplied);
        });
    }
}
