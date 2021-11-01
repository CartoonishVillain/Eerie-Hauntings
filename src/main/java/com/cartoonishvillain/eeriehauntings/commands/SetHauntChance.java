package com.cartoonishvillain.eeriehauntings.commands;

import com.cartoonishvillain.eeriehauntings.capabilities.playercapability.PlayerCapability;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.GameProfileArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;

public class SetHauntChance {

    public static void register(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(Commands.literal("eeriehauntings").then(Commands.literal("sethauntchance")
                .requires(cs -> {return cs.hasPermission(2);})
                .then(Commands.argument("target", GameProfileArgument.gameProfile()).then(Commands.argument("chance", FloatArgumentType.floatArg(0, 100)).executes(context -> {
                    return setHauntChance(context.getSource(), GameProfileArgument.getGameProfiles(context, "target"), FloatArgumentType.getFloat(context, "chance"));
                }))))

        );
    }


    private static int setHauntChance(CommandSource source, Collection<GameProfile> profiles, float chance){
        for(GameProfile gameProfile : profiles){
            ServerPlayerEntity serverPlayerEntity = source.getServer().getPlayerList().getPlayer(gameProfile.getId());
            serverPlayerEntity.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                h.setHauntChance(chance);
            });
        }
        source.sendSuccess(new TranslationTextComponent("hauntchance.eeriehauntings.success", chance), false);
        return 0;
    }
}
