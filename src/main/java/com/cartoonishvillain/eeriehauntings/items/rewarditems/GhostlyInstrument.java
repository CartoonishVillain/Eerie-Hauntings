package com.cartoonishvillain.eeriehauntings.items.rewarditems;

import com.cartoonishvillain.eeriehauntings.EerieHauntings;
import com.cartoonishvillain.eeriehauntings.Register;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;


public class GhostlyInstrument extends Item {
    public GhostlyInstrument(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        if(!p_41432_.isClientSide()) {
            String item = p_41433_.getItemInHand(p_41434_).getItem().getRegistryName().getPath();
            switch (item) {
                case "amplified_ghostly_instrument": {
                    p_41433_.getCooldowns().addCooldown(this, 300);
                    SoundEvent soundEvent = null;
                    short x = (short) p_41433_.getRandom().nextInt(2);
                    soundEvent = x == 0 ?  Register.MEDIUMSTRENGTHSOUNDS.get() :  Register.STRONGSTRENGTHSOUNDS.get();
                    float randomPitch = (p_41433_).getRandom().nextFloat();
                    int xModifier = (p_41433_).getRandom().nextInt(6 + 6) - 6;
                    int yModifier = (p_41433_).getRandom().nextInt(6 + 6) - 6;
                    int zModifier = (p_41433_).getRandom().nextInt(6 + 6) - 6;
                    p_41433_.level.playSound(null, p_41433_.getX() + xModifier, p_41433_.getY() + yModifier, p_41433_.getZ() + zModifier, soundEvent, SoundSource.MASTER, 1.25f, randomPitch * 1.2f);
                   if (p_41433_.isCrouching()) {p_41433_.getItemInHand(p_41434_).hurtAndBreak(blindNearbyPlayers(p_41433_, p_41432_)*2, p_41433_, (p_41300_) -> {
                       p_41300_.broadcastBreakEvent(p_41434_);
                   } );}
                    break;
                }
                default: {
                    p_41433_.getCooldowns().addCooldown(this, 200);
                    int soundID = p_41433_.getRandom().nextInt(EerieHauntings.lowEndSounds.size());
                    float randomPitch = (p_41433_).getRandom().nextFloat();
                    int xModifier = (p_41433_).getRandom().nextInt(6 + 6) - 6;
                    int yModifier = (p_41433_).getRandom().nextInt(6 + 6) - 6;
                    int zModifier = (p_41433_).getRandom().nextInt(6 + 6) - 6;
                    p_41433_.level.playSound(null, p_41433_.getX() + xModifier, p_41433_.getY() + yModifier, p_41433_.getZ() + zModifier, EerieHauntings.lowEndSounds.get(soundID), SoundSource.MASTER, 1.25f, randomPitch * 1.2f);
                }
            }
            p_41433_.getItemInHand(p_41434_).hurtAndBreak(1, p_41433_, (p_41300_) -> {
                p_41300_.broadcastBreakEvent(p_41434_);
            });
        }
        return super.use(p_41432_, p_41433_, p_41434_);
    }

    private int blindNearbyPlayers(Player player, Level level){
        ArrayList<Player> players = (ArrayList<Player>) level.getEntitiesOfClass(Player.class, player.getBoundingBox().inflate(7));
        int effected = 0;
        for(Player effectedPlayer : players){
            if(!effectedPlayer.equals(player)){
                effectedPlayer.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20*15, 0));
                effected++;
            }
        }
        return effected;
    }



}
