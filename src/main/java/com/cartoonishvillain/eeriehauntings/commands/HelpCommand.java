package com.cartoonishvillain.eeriehauntings.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class HelpCommand implements Command<CommandSource> {
    private static final HelpCommand CMD = new HelpCommand();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher){
        return Commands.literal("help")
                .requires(cs -> cs.hasPermission(0))
                .executes(CMD);


    }
    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        context.getSource().sendSuccess(new TranslationTextComponent("help.eeriehauntings.help"), false);
        context.getSource().sendSuccess(new TranslationTextComponent("help.eeriehauntings.forcehaunt"), false);
        context.getSource().sendSuccess(new TranslationTextComponent("help.eeriehauntings.removehaunt"), false);
        context.getSource().sendSuccess(new TranslationTextComponent("help.eeriehauntings.anger"), false);
        context.getSource().sendSuccess(new TranslationTextComponent("help.eeriehauntings.setchance"), false);
        context.getSource().sendSuccess(new TranslationTextComponent("help.eeriehauntings.protect"), false);
        return 0;
    }
}
