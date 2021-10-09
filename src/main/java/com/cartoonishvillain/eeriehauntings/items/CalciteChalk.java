package com.cartoonishvillain.eeriehauntings.items;

import com.cartoonishvillain.eeriehauntings.EerieHauntings;
import com.cartoonishvillain.eeriehauntings.capabilities.playercapability.PlayerCapability;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class CalciteChalk extends Item {
    public CalciteChalk(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public ActionResult<ItemStack> use(World p_77659_1_, PlayerEntity p_77659_2_, Hand p_77659_3_) {
        if(!p_77659_1_.isClientSide()) {
            p_77659_2_.getCapability(PlayerCapability.INSTANCE).ifPresent(h -> {
                if (!h.getIsHaunted()) {
                    h.setProtectedDays(EerieHauntings.serverConfig.CHALKDURATION.get());
                    p_77659_2_.displayClientMessage(new TranslationTextComponent("info.eeriehauntings.activatechalk", EerieHauntings.serverConfig.CHALKDURATION.get()), false);
                    p_77659_2_.getCooldowns().addCooldown(this, 100);
                    p_77659_2_.getItemInHand(p_77659_3_).shrink(1);
                } else {
                    p_77659_2_.displayClientMessage(new TranslationTextComponent("info.eeriehauntings.failactivatechalk"), false);
                    p_77659_2_.getCooldowns().addCooldown(this, 100);
                }
            });
        }
        return super.use(p_77659_1_, p_77659_2_, p_77659_3_);
    }

    @Override
    public void appendHoverText(ItemStack p_77624_1_, @Nullable World p_77624_2_, List<ITextComponent> p_77624_3_, ITooltipFlag p_77624_4_) {
        super.appendHoverText(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
        p_77624_3_.add(new TranslationTextComponent("info.eeriehauntings.calciteexplain", EerieHauntings.serverConfig.CHALKDURATION.get()).withStyle(TextFormatting.GOLD));
    }

}
