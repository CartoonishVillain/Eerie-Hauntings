package com.cartoonishvillain.eeriehauntings.items.rewarditems;

import com.cartoonishvillain.eeriehauntings.EerieHauntings;
import com.cartoonishvillain.eeriehauntings.Register;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;


public class GhostlyInstrument extends Item {
    public enum TYPE {
        STANDARD,
        AMPLIFIED
    }
    private TYPE type;
    public GhostlyInstrument(Properties p_41383_, TYPE type) {
        super(p_41383_);
        this.type = type;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        if(!p_41432_.isClientSide()) {
            switch (type) {
                case AMPLIFIED: {
                    SoundEvent soundEvent = null;
                    short x = (short) p_41433_.getRandom().nextInt(2);
                    soundEvent = x == 0 ?  Register.MEDIUMSTRENGTHSOUNDS.get() :  Register.STRONGSTRENGTHSOUNDS.get();
                    float randomPitch = (p_41433_).getRandom().nextFloat();
                    int xModifier = (p_41433_).getRandom().nextInt(3 + 3) - 3;
                    int yModifier = (p_41433_).getRandom().nextInt(3 + 3) - 3;
                    int zModifier = (p_41433_).getRandom().nextInt(3 + 3) - 3;
                    p_41433_.level.playSound(null, p_41433_.getX() + xModifier, p_41433_.getY() + yModifier, p_41433_.getZ() + zModifier, soundEvent, SoundSource.PLAYERS, 1.25f, randomPitch * 1.2f);
                   if (p_41433_.isCrouching()) {p_41433_.getItemInHand(p_41434_).hurtAndBreak(blindNearbyPlayers(p_41433_, p_41432_)*2, p_41433_, (p_41300_) -> {
                       p_41300_.broadcastBreakEvent(p_41434_);
                   } ); p_41433_.getCooldowns().addCooldown(this, 300);}
                   else p_41433_.getCooldowns().addCooldown(this, 200);
                   break;
                }
                default: {
                    p_41433_.getCooldowns().addCooldown(this, 200);
                    int soundID = p_41433_.getRandom().nextInt(EerieHauntings.lowEndSounds.size());
                    float randomPitch = (p_41433_).getRandom().nextFloat();
                    int xModifier = (p_41433_).getRandom().nextInt(3 + 3) - 3;
                    int yModifier = (p_41433_).getRandom().nextInt(3 + 3) - 3;
                    int zModifier = (p_41433_).getRandom().nextInt(3 + 3) - 3;
                    p_41433_.level.playSound(null, p_41433_.getX() + xModifier, p_41433_.getY() + yModifier, p_41433_.getZ() + zModifier, EerieHauntings.lowEndSounds.get(soundID), SoundSource.PLAYERS, 1.25f, randomPitch * 1.2f);
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

    @Override
    public boolean isFoil(ItemStack p_41453_) {
        return type == TYPE.AMPLIFIED;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        switch (type){
            case AMPLIFIED:{
                p_41423_.add(Component.translatable("amplifiedinstrument.eeriehauntings.infosound").withStyle(ChatFormatting.BLUE));
                p_41423_.add(Component.translatable("amplifiedinstrument.eeriehauntings.infocrouch").withStyle(ChatFormatting.RED));
                break;
            }
            default:{
                p_41423_.add(Component.translatable("instrument.eeriehauntings.infosound").withStyle(ChatFormatting.BLUE));
                break;
            }
        }

    }
}
