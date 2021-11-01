package com.cartoonishvillain.eeriehauntings.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;

public class HelpCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("eeriehauntings").then(Commands.literal("help")
                .requires(cs -> cs.hasPermission(0))
                .executes(context -> {
                    return run(context.getSource());
                })));


    }

    public static int run(CommandSourceStack source) throws CommandSyntaxException {
        source.sendSuccess(new TranslatableComponent("help.eeriehauntings.help"), false);
        source.sendSuccess(new TranslatableComponent("help.eeriehauntings.forcehaunt"), false);
        source.sendSuccess(new TranslatableComponent("help.eeriehauntings.removehaunt"), false);
        source.sendSuccess(new TranslatableComponent("help.eeriehauntings.anger"), false);
        source.sendSuccess(new TranslatableComponent("help.eeriehauntings.setchance"), false);
        source.sendSuccess(new TranslatableComponent("help.eeriehauntings.protect"), false);
        return 0;
    }
}
