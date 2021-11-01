package com.cartoonishvillain.eeriehauntings.commands;

import com.cartoonishvillain.eeriehauntings.capabilities.playercapability.PlayerCapability;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.GameProfileArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;

public class ToggleAngerCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(Commands.literal("eeriehauntings").then(Commands.literal("toggleanger")
                .requires(cs -> {return cs.hasPermission(2);})
                .then(Commands.argument("target", GameProfileArgument.gameProfile()).executes(context -> {
                    return toggleAnger(context.getSource(), GameProfileArgument.getGameProfiles(context, "target"));
                }))
        ));
    }


    private static int toggleAnger(CommandSource source, Collection<GameProfile> profiles){
        for(GameProfile gameProfile : profiles){
            ServerPlayerEntity serverPlayerEntity = source.getServer().getPlayerList().getPlayer(gameProfile.getId());
            serverPlayerEntity.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                h.setAnger(!h.getAnger());
            });
        }
        source.sendSuccess(new TranslationTextComponent("anger.eeriehauntings.toggled"), false);
        return 0;
    }
}
