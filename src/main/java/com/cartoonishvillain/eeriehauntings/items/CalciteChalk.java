package com.cartoonishvillain.eeriehauntings.items;

import com.cartoonishvillain.eeriehauntings.capabilities.playercapability.PlayerCapability;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CalciteChalk extends Item {
    public CalciteChalk(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        if(!p_41432_.isClientSide()) {
            p_41433_.getCapability(PlayerCapability.INSTANCE).ifPresent(h -> {
                if (!h.getIsHaunted()) {
                    h.setProtectedDays(10);
                    p_41433_.displayClientMessage(new TranslatableComponent("info.eeriehauntings.activatechalk"), false);
                    p_41433_.getCooldowns().addCooldown(this, 100);
                    p_41433_.getItemInHand(p_41434_).shrink(1);
                } else {
                    p_41433_.displayClientMessage(new TranslatableComponent("info.eeriehauntings.failactivatechalk"), false);
                    p_41433_.getCooldowns().addCooldown(this, 100);
                }
            });
        }
        return super.use(p_41432_, p_41433_, p_41434_);
    }
}
