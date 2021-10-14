package com.cartoonishvillain.eeriehauntings.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;

public class HelpCommand implements Command<CommandSourceStack> {
    private static final HelpCommand CMD = new HelpCommand();

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher){
        return Commands.literal("help")
                .requires(cs -> cs.hasPermission(0))
                .executes(CMD);


    }
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        context.getSource().sendSuccess(new TranslatableComponent("help.eeriehauntings.help"), false);
        context.getSource().sendSuccess(new TranslatableComponent("help.eeriehauntings.forcehaunt"), false);
        context.getSource().sendSuccess(new TranslatableComponent("help.eeriehauntings.removehaunt"), false);
        context.getSource().sendSuccess(new TranslatableComponent("help.eeriehauntings.anger"), false);
        context.getSource().sendSuccess(new TranslatableComponent("help.eeriehauntings.setchance"), false);
        context.getSource().sendSuccess(new TranslatableComponent("help.eeriehauntings.protect"), false);
        return 0;
    }
}
