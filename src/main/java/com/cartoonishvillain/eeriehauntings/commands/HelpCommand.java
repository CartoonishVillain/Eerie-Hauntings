package com.cartoonishvillain.eeriehauntings.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class HelpCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("eeriehauntings").then(Commands.literal("help")
                .requires(cs -> cs.hasPermission(0))
                .executes(context -> {
                    return run(context.getSource());
                })));


    }

    public static int run(CommandSourceStack source) throws CommandSyntaxException {
        source.sendSuccess(Component.translatable("help.eeriehauntings.help"), false);
        source.sendSuccess(Component.translatable("help.eeriehauntings.forcehaunt"), false);
        source.sendSuccess(Component.translatable("help.eeriehauntings.removehaunt"), false);
        source.sendSuccess(Component.translatable("help.eeriehauntings.anger"), false);
        source.sendSuccess(Component.translatable("help.eeriehauntings.setchance"), false);
        source.sendSuccess(Component.translatable("help.eeriehauntings.protect"), false);
        return 0;
    }
}
