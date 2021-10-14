package com.cartoonishvillain.eeriehauntings.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class CommandRegister {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        LiteralCommandNode<CommandSourceStack> commands = dispatcher.register(Commands.literal("eeriehauntings").then(HelpCommand.register(dispatcher)));

    }
}
