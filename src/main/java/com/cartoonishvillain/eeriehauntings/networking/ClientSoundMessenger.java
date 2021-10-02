package com.cartoonishvillain.eeriehauntings.networking;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public class ClientSoundMessenger {
    private static String ProtocolVersion = "1";
    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation("eeriehaunting", "soundtrigger"),
            () -> ProtocolVersion, ProtocolVersion::equals, ProtocolVersion::equals);
    private static int ID = 0;

    public static void register() {
        INSTANCE.registerMessage(0, ClientSoundPacket.class, ClientSoundPacket::encode, ClientSoundPacket::decode, ClientSoundPacket::handle);
    }

    public static void sendTo(Object message, Player player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), message);
    }

}
