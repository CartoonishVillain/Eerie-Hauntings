package com.cartoonishvillain.eeriehauntings.events;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Event;

public class GhostExorcisedEvent extends Event {
    private final ServerPlayerEntity player;
    private final int ghostType;
    private final boolean isExpell;

    /**
     * This event is fired when a player actively exorcises a ghost themselves.
     * Called in {@link ForgeBusEvents#exorciseGhost(ServerPlayerEntity)} and {@link ForgeBusEvents#expellGhost(ServerPlayerEntity)}.<br>
     * <br>
     * {@link #ghostType} refers to the type of ghost (and therefore the method needed) to exorcise the spirit.<br>
     * <br>
     * {@link #isExpell} refers to if the player exorcised the ghost while it was angered with another method. This isn't rewarded within the mod when true.
     **/

    public GhostExorcisedEvent(ServerPlayerEntity player, int ghostType, boolean isExpell) {
        this.player = player;
        this.ghostType = ghostType;
        this.isExpell = isExpell;
    }

    public ServerPlayerEntity getPlayer() {
        return player;
    }

    public int getGhostType() {
        return ghostType;
    }

    public boolean isExpell() {
        return isExpell;
    }
}
