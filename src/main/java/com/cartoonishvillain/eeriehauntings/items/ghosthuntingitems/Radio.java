package com.cartoonishvillain.eeriehauntings.items.ghosthuntingitems;

import com.cartoonishvillain.eeriehauntings.Register;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class Radio extends Item {
    public Radio(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public ActionResult<ItemStack> use(World p_41432_, PlayerEntity p_41433_, Hand p_41434_) {
        p_41433_.getCooldowns().addCooldown(this, 100);
        p_41432_.playSound(null, p_41433_.blockPosition(), Register.RADIOSOUND.get(), SoundCategory.PLAYERS, 1, 1);
        return super.use(p_41432_, p_41433_, p_41434_);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable World p_41422_, List<ITextComponent> p_41423_, ITooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        p_41423_.add(new TranslationTextComponent("info.eeriehauntings.radiodesc").withStyle(TextFormatting.GOLD));
        p_41423_.add(new TranslationTextComponent("info.eeriehauntings.click").withStyle(TextFormatting.BLUE));
    }

}
