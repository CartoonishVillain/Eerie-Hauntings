package com.cartoonishvillain.eeriehauntings.commands;

import com.cartoonishvillain.eeriehauntings.capabilities.playercapability.PlayerCapability;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;


import java.util.Collection;

public class ForceHauntCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("forcehaunt")
                .requires(cs -> {return cs.hasPermission(2);})
                .then(Commands.argument("target", GameProfileArgument.gameProfile()).executes(context -> {
                    return hauntRandom(context.getSource(), GameProfileArgument.getGameProfiles(context, "target"));
                }))
        );
    }

    private static int hauntRandom(CommandSourceStack source, Collection<GameProfile> profiles){
        for(GameProfile gameProfile : profiles){
            ServerPlayer serverPlayerEntity = source.getServer().getPlayerList().getPlayer(gameProfile.getId());
            serverPlayerEntity.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                if (!h.getIsHaunted()) {
                    h.setHaunted(true);
                    h.setGhostType(source.getLevel().getRandom().nextInt(3)+1);
                }
            });
        }
        source.sendSuccess(new TranslatableComponent("haunted.eeriehauntings.success"), false);
        return 0;
    }
}
