package com.cartoonishvillain.eeriehauntings.items;

import com.cartoonishvillain.eeriehauntings.EerieHauntings;
import com.cartoonishvillain.eeriehauntings.capabilities.playercapability.PlayerCapability;
import com.cartoonishvillain.eeriehauntings.events.ForgeBusEvents;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class Offering extends Item {

    public Offering(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public ActionResult<ItemStack> use(World p_41432_, PlayerEntity p_41433_, Hand p_41434_) {
        if(!p_41432_.isClientSide()){
            if(p_41433_.getMainHandItem().getItem() instanceof Offering && p_41433_.getOffhandItem().getItem().equals(Items.FLINT_AND_STEEL)){

                p_41433_.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                    if(h.getIsHaunted() && !h.getAnger()){
                        p_41433_.getMainHandItem().shrink(1);
                        ForgeBusEvents.boonExpelGhost((ServerPlayerEntity) p_41433_);
                    } else if(h.getIsHaunted() && h.getAnger()) {
                        p_41433_.getMainHandItem().shrink(1);
                        p_41433_.displayClientMessage(new TranslationTextComponent("boon.eeriehauntings.deny").withStyle(TextFormatting.RED), false);
                    } else if(!h.getIsHaunted()){
                        p_41433_.displayClientMessage(new TranslationTextComponent("boon.eeriehauntings.wasted").withStyle(TextFormatting.RED), false);
                    }
                });
            }
        }
        return super.use(p_41432_, p_41433_, p_41434_);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable World p_41422_, List<ITextComponent> p_41423_, ITooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        if (EerieHauntings.serverConfig.BOON.get())
            p_41423_.add(new TranslationTextComponent("info.eeriehauntings.offering").withStyle(TextFormatting.GOLD));
        else
            p_41423_.add(new TranslationTextComponent("info.eeriehauntings.offeringdisabled").withStyle(TextFormatting.GRAY));
    }


}
