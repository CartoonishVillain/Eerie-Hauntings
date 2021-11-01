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

public class ToggleAngerCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("eeriehauntings").then(Commands.literal("toggleanger")
                .requires(cs -> {return cs.hasPermission(2);})
                .then(Commands.argument("target", GameProfileArgument.gameProfile()).executes(context -> {
                            return toggleAnger(context.getSource(), GameProfileArgument.getGameProfiles(context, "target"));
                        })
                )));
    }


    private static int toggleAnger(CommandSourceStack source, Collection<GameProfile> profiles){
        for(GameProfile gameProfile : profiles){
            ServerPlayer serverPlayerEntity = source.getServer().getPlayerList().getPlayer(gameProfile.getId());
            serverPlayerEntity.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                h.setAnger(!h.getAnger());
            });
        }
        source.sendSuccess(new TranslatableComponent("anger.eeriehauntings.toggled"), false);
        return 0;
    }
}
