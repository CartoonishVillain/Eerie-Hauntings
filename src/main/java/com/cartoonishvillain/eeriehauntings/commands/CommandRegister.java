package com.cartoonishvillain.eeriehauntings.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;

public class CommandRegister {

    public static void register(CommandDispatcher<CommandSource> dispatcher){
        LiteralCommandNode<CommandSource> commands = dispatcher.register(Commands.literal("eeriehauntings").then(HelpCommand.register(dispatcher)));

    }
}
