package com.cartoonishvillain.eeriehauntings.items.rewarditems;

import com.cartoonishvillain.eeriehauntings.EerieHauntings;
import com.cartoonishvillain.eeriehauntings.Register;
import net.minecraft.client.audio.SoundSource;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GhostlyInstrument extends Item {

    public GhostlyInstrument(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public ActionResult<ItemStack> use(World p_77659_1_, PlayerEntity p_77659_2_, Hand p_77659_3_) {
        if(!p_77659_1_.isClientSide()) {
            String item = p_77659_2_.getItemInHand(p_77659_3_).getItem().getRegistryName().getPath();
            switch (item) {
                case "amplified_ghostly_instrument": {

                    SoundEvent soundEvent = null;
                    short x = (short) p_77659_2_.getRandom().nextInt(2);
                    soundEvent = x == 0 ?  Register.MEDIUMSTRENGTHSOUNDS.get() :  Register.STRONGSTRENGTHSOUNDS.get();
                    float randomPitch = (p_77659_2_).getRandom().nextFloat();
                    int xModifier = (p_77659_2_).getRandom().nextInt(3 + 3) - 3;
                    int yModifier = (p_77659_2_).getRandom().nextInt(3 + 3) - 3;
                    int zModifier = (p_77659_2_).getRandom().nextInt(3 + 3) - 3;
                    p_77659_2_.level.playSound(null, p_77659_2_.getX() + xModifier, p_77659_2_.getY() + yModifier, p_77659_2_.getZ() + zModifier, soundEvent, SoundCategory.PLAYERS, 1.25f, randomPitch * 1.2f);
                    if (p_77659_2_.isCrouching()) {p_77659_2_.getItemInHand(p_77659_3_).hurtAndBreak(blindNearbyPlayers(p_77659_2_, p_77659_1_)*2, p_77659_2_, (p_41300_) -> {
                        p_41300_.broadcastBreakEvent(p_77659_3_);
                    } ); p_77659_2_.getCooldowns().addCooldown(this, 300);}
                    else p_77659_2_.getCooldowns().addCooldown(this, 200);
                    break;
                }
                default: {
                    p_77659_2_.getCooldowns().addCooldown(this, 200);
                    int soundID = p_77659_2_.getRandom().nextInt(EerieHauntings.lowEndSounds.size());
                    float randomPitch = (p_77659_2_).getRandom().nextFloat();
                    int xModifier = (p_77659_2_).getRandom().nextInt(3 + 3) - 3;
                    int yModifier = (p_77659_2_).getRandom().nextInt(3 + 3) - 3;
                    int zModifier = (p_77659_2_).getRandom().nextInt(3 + 3) - 3;
                    p_77659_2_.level.playSound(null, p_77659_2_.getX() + xModifier, p_77659_2_.getY() + yModifier, p_77659_2_.getZ() + zModifier, EerieHauntings.lowEndSounds.get(soundID), SoundCategory.PLAYERS, 1.25f, randomPitch * 1.2f);
                }
            }
            p_77659_2_.getItemInHand(p_77659_3_).hurtAndBreak(1, p_77659_2_, (p_41300_) -> {
                p_41300_.broadcastBreakEvent(p_77659_3_);
            });
        }
        return super.use(p_77659_1_, p_77659_2_, p_77659_3_);
    }


    private int blindNearbyPlayers(PlayerEntity player, World level){
        ArrayList<PlayerEntity> players = (ArrayList<PlayerEntity>) level.getEntitiesOfClass(PlayerEntity.class, player.getBoundingBox().inflate(7));
        int effected = 0;
        for(PlayerEntity effectedPlayer : players){
            if(!effectedPlayer.equals(player)){
                effectedPlayer.addEffect(new EffectInstance(Effects.BLINDNESS, 20*15, 0));
                effected++;
            }
        }
        return effected;
    }

    @Override
    public boolean isFoil(ItemStack p_77636_1_) {
        return p_77636_1_.getItem().getRegistryName().getPath().equals("amplified_ghostly_instrument");
    }

    @Override
    public void appendHoverText(ItemStack p_77624_1_, @Nullable World p_77624_2_, List<ITextComponent> p_77624_3_, ITooltipFlag p_77624_4_) {
        super.appendHoverText(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
        String item = p_77624_1_.getItem().getRegistryName().getPath();
        switch (item){
            case "amplified_ghostly_instrument":{
                p_77624_3_.add(new TranslationTextComponent("amplifiedinstrument.eeriehauntings.infosound").withStyle(TextFormatting.BLUE));
                p_77624_3_.add(new TranslationTextComponent("amplifiedinstrument.eeriehauntings.infocrouch").withStyle(TextFormatting.RED));
                break;
            }
            default:{
                p_77624_3_.add(new TranslationTextComponent("instrument.eeriehauntings.infosound").withStyle(TextFormatting.BLUE));
                break;
            }
        }
    }
}
