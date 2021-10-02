package com.cartoonishvillain.eeriehauntings.networking.strongsoundpackets;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public class StrongClientSoundMessenger {
    private static String ProtocolVersion = "1";
    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation("eeriehauntings", "strongsoundtrigger"),
            () -> ProtocolVersion, ProtocolVersion::equals, ProtocolVersion::equals);
    private static int ID = 0;

    public static void register() {
        INSTANCE.registerMessage(0, StrongClientSoundPacket.class, StrongClientSoundPacket::encode, StrongClientSoundPacket::decode, StrongClientSoundPacket::handle);
    }

    public static void sendTo(Object message, Player player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), message);
    }

}
