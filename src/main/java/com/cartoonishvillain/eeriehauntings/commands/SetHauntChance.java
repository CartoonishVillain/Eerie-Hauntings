package com.cartoonishvillain.eeriehauntings.commands;

import com.cartoonishvillain.eeriehauntings.capabilities.playercapability.PlayerCapability;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class SetHauntChance {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("eeriehauntings").then(Commands.literal("sethauntchance")
                .requires(cs -> {return cs.hasPermission(2);})
                .then(Commands.argument("target", GameProfileArgument.gameProfile()).then(Commands.argument("chance", FloatArgumentType.floatArg(0, 100)).executes(context -> {
                    return setHauntChance(context.getSource(), GameProfileArgument.getGameProfiles(context, "target"), FloatArgumentType.getFloat(context, "chance"));
                }))))

        );
    }


    private static int setHauntChance(CommandSourceStack source, Collection<GameProfile> profiles, float chance){
        for(GameProfile gameProfile : profiles){
            ServerPlayer serverPlayerEntity = source.getServer().getPlayerList().getPlayer(gameProfile.getId());
            serverPlayerEntity.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                h.setHauntChance(chance);
            });
        }
        source.sendSuccess(Component.translatable("hauntchance.eeriehauntings.success", chance), false);
        return 0;
    }
}
