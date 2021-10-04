package com.cartoonishvillain.eeriehauntings.items.ghosthuntingitems;

import com.cartoonishvillain.eeriehauntings.EerieHauntings;
import com.cartoonishvillain.eeriehauntings.capabilities.playercapability.PlayerCapability;
import com.cartoonishvillain.eeriehauntings.events.ForgeBusEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
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

public class RedstoneDreamCatcher extends Item {

    public RedstoneDreamCatcher(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        if(p_41433_.getMainHandItem().getItem() instanceof RedstoneDreamCatcher && !p_41432_.isClientSide()){
            p_41433_.getCooldowns().addCooldown(this, 100);
            p_41433_.getMainHandItem().shrink(1);
            p_41432_.playSound(null, p_41433_.getOnPos(), SoundEvents.PISTON_CONTRACT, SoundSource.PLAYERS, 0.5f, 1);
            p_41432_.playSound(null, p_41433_.getOnPos(), SoundEvents.PISTON_EXTEND, SoundSource.PLAYERS, 0.5f, 1);

            p_41433_.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                int type = h.getGhostType();
                if(h.getIsHaunted()){
                    if(!EerieHauntings.serverConfig.EASYMODE.get()) {
                        if (type == 1) {
                            if (h.getAnger()) ForgeBusEvents.expellGhost((ServerPlayer) p_41433_);
                            else ForgeBusEvents.exorciseGhost((ServerPlayer) p_41433_);
                        } else {
                            if(EerieHauntings.serverConfig.ANGERGHOST.get())
                                h.setAnger(true);
                            h.setHauntActionTicks(1);
                        }
                    } else{
                        ForgeBusEvents.exorciseGhost((ServerPlayer) p_41433_);
                    }
                }
            });
        }
        return super.use(p_41432_, p_41433_, p_41434_);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        if(!EerieHauntings.serverConfig.EASYMODE.get())
        p_41423_.add(new TranslatableComponent("tools.eeriehauntings.redstone").withStyle(ChatFormatting.GOLD));
        else
            p_41423_.add(new TranslatableComponent("tools.eeriehauntings.easyredstone").withStyle(ChatFormatting.GOLD));
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
    }
}
