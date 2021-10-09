package com.cartoonishvillain.eeriehauntings.items.ghosthuntingitems;

import com.cartoonishvillain.eeriehauntings.EerieHauntings;
import com.cartoonishvillain.eeriehauntings.capabilities.playercapability.PlayerCapability;
import com.cartoonishvillain.eeriehauntings.events.ForgeBusEvents;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class PurifiedWater extends Item {

    public PurifiedWater(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public ActionResult<ItemStack> use(World p_41432_, PlayerEntity p_41433_, Hand p_41434_) {
        if(p_41433_.getMainHandItem().getItem() instanceof PurifiedWater && !p_41432_.isClientSide()){
            p_41433_.getCooldowns().addCooldown(this, 100);
            p_41433_.getMainHandItem().shrink(1);
            p_41432_.playSound(null, p_41433_.blockPosition(), SoundEvents.SPLASH_POTION_BREAK, SoundCategory.PLAYERS, 1, 1);

            p_41433_.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                int type = h.getGhostType();
                if(h.getIsHaunted()){
                    if(!EerieHauntings.serverConfig.EASYMODE.get()) {
                        if (type == 2) {
                            if (h.getAnger()) ForgeBusEvents.expellGhost((ServerPlayerEntity) p_41433_);
                            else ForgeBusEvents.exorciseGhost((ServerPlayerEntity) p_41433_);
                        } else {
                            if(EerieHauntings.serverConfig.ANGERGHOST.get())
                                h.setAnger(true);
                            h.setHauntActionTicks(1);
                        }
                    } else ForgeBusEvents.exorciseGhost((ServerPlayerEntity) p_41433_);
                }
            });
        }
        return super.use(p_41432_, p_41433_, p_41434_);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable World p_41422_, List<ITextComponent> p_41423_, ITooltipFlag p_41424_) {
        if(!EerieHauntings.serverConfig.EASYMODE.get())
            p_41423_.add(new TranslationTextComponent("tools.eeriehauntings.water").withStyle(TextFormatting.GOLD));
        else
            p_41423_.add(new TranslationTextComponent("tools.eeriehauntings.easywater").withStyle(TextFormatting.GOLD));
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
    }

}
