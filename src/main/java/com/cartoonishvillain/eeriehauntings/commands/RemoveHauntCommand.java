package com.cartoonishvillain.eeriehauntings.commands;

import com.cartoonishvillain.eeriehauntings.capabilities.playercapability.PlayerCapability;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.GameProfileArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;

public class RemoveHauntCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(Commands.literal("eeriehauntings").then(Commands.literal("removehaunt")
                .requires(cs -> {return cs.hasPermission(2);})
                .then(Commands.argument("target", GameProfileArgument.gameProfile()).executes(context -> {
                    return removeHaunt(context.getSource(), GameProfileArgument.getGameProfiles(context, "target"));
                })))
        );
    }


    private static int removeHaunt(CommandSource source, Collection<GameProfile> profiles){
        for(GameProfile gameProfile : profiles){
            ServerPlayerEntity serverPlayerEntity = source.getServer().getPlayerList().getPlayer(gameProfile.getId());
            serverPlayerEntity.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                if(h.getIsHaunted()){
                    h.setHaunted(false);
                    h.setGhostType(0);
                    h.setAnger(false);
                }
            });
        }
        source.sendSuccess(new TranslationTextComponent("remove.eeriehauntings.success"), false);
        return 0;
    }
}
