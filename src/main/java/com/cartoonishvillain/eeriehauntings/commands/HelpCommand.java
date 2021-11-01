package com.cartoonishvillain.eeriehauntings.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class HelpCommand {


    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("eeriehauntings").then(Commands.literal("help")
                .requires(cs -> cs.hasPermission(0))
                .executes(context -> {
                    return run(context.getSource());
                })));
    }

    public static int run(CommandSource source) throws CommandSyntaxException {
        source.sendSuccess(new TranslationTextComponent("help.eeriehauntings.help"), false);
        source.sendSuccess(new TranslationTextComponent("help.eeriehauntings.forcehaunt"), false);
        source.sendSuccess(new TranslationTextComponent("help.eeriehauntings.removehaunt"), false);
        source.sendSuccess(new TranslationTextComponent("help.eeriehauntings.anger"), false);
        source.sendSuccess(new TranslationTextComponent("help.eeriehauntings.setchance"), false);
        source.sendSuccess(new TranslationTextComponent("help.eeriehauntings.protect"), false);
        return 0;
    }
}
