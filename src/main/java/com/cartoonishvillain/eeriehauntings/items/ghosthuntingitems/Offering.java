package com.cartoonishvillain.eeriehauntings.items.ghosthuntingitems;

import com.cartoonishvillain.eeriehauntings.capabilities.playercapability.PlayerCapability;
import com.cartoonishvillain.eeriehauntings.events.ForgeBusEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class Offering extends Item {

    public Offering(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        if(!p_41432_.isClientSide()){
            if(p_41433_.getMainHandItem().getItem() instanceof Offering && p_41433_.getOffhandItem().getItem().equals(Items.FLINT_AND_STEEL)){

                p_41433_.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                    if(h.getIsHaunted() && !h.getAnger()){
                        p_41433_.getMainHandItem().shrink(1);
                        ForgeBusEvents.boonExpelGhost((ServerPlayer) p_41433_);
                    } else if(h.getIsHaunted() && h.getAnger()) {
                        p_41433_.getMainHandItem().shrink(1);
                        p_41433_.displayClientMessage(new TranslatableComponent("boon.eeriehauntings.deny").withStyle(ChatFormatting.RED), false);
                    } else if(!h.getIsHaunted()){
                        p_41433_.displayClientMessage(new TranslatableComponent("boon.eeriehauntings.wasted").withStyle(ChatFormatting.RED), false);
                    }
                });
            }
        }
        return super.use(p_41432_, p_41433_, p_41434_);
    }
}
