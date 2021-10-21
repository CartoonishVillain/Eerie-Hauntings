package com.cartoonishvillain.eeriehauntings.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class GhostExorcisedEvent extends Event {
    
    private final ServerPlayer player;
    private final int ghostType;
    private final boolean isExpell;

    /**
     * This event is fired when a player actively exorcises a ghost themselves.
     * Called in {@link ForgeBusEvents#exorciseGhost(ServerPlayer)} and {@link ForgeBusEvents#expellGhost(ServerPlayer)}.<br>
     * <br>
     * {@link #ghostType} refers to the type of ghost (and therefore the method needed) to exorcise the spirit.<br>
     * <br>
     * {@link #isExpell} refers to if the player exorcised the ghost while it was angered with another method. This isn't rewarded within the mod when true.
     **/

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
