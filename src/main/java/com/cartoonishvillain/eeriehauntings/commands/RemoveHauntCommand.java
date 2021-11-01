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

public class RemoveHauntCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("eeriehauntings").then(Commands.literal("removehaunt")
                .requires(cs -> {return cs.hasPermission(2);})
                .then(Commands.argument("target", GameProfileArgument.gameProfile()).executes(context -> {
                    return removeHaunt(context.getSource(), GameProfileArgument.getGameProfiles(context, "target"));
                })))
        );
    }


    private static int removeHaunt(CommandSourceStack source, Collection<GameProfile> profiles){
        for(GameProfile gameProfile : profiles){
            ServerPlayer serverPlayerEntity = source.getServer().getPlayerList().getPlayer(gameProfile.getId());
            serverPlayerEntity.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                if(h.getIsHaunted()){
                    h.setHaunted(false);
                    h.setGhostType(0);
                    h.setAnger(false);
                }
            });
        }
        source.sendSuccess(new TranslatableComponent("remove.eeriehauntings.success"), false);
        return 0;
    }
}
