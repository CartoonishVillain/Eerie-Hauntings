package com.cartoonishvillain.eeriehauntings.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class GhostExorcisedEvent extends Event {
    
    private final ServerPlayer player;
    private final int ghostType;
    private final boolean isExpell;

    public GhostExorcisedEvent(ServerPlayer player, int ghostType, boolean isExpell) {
        this.player = player;
        this.ghostType = ghostType;
        this.isExpell = isExpell;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public int getGhostType() {
        return ghostType;
    }

    public boolean isExpell() {
        return isExpell;
    }
}
