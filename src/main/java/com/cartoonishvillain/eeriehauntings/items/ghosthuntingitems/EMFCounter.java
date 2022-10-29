package com.cartoonishvillain.eeriehauntings.items.ghosthuntingitems;

import com.cartoonishvillain.eeriehauntings.Register;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class EMFCounter extends Item {
    public EMFCounter(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        p_41433_.getCooldowns().addCooldown(this, 100);
        p_41432_.playSound(null, p_41433_.getOnPos(), Register.EMFCOUNTERSOUNDS.get(), SoundSource.PLAYERS, 1, 1);
        return super.use(p_41432_, p_41433_, p_41434_);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        p_41423_.add(Component.translatable("info.eeriehauntings.emfdesc").withStyle(ChatFormatting.GOLD));
        p_41423_.add(Component.translatable("info.eeriehauntings.click").withStyle(ChatFormatting.BLUE));
    }
}
