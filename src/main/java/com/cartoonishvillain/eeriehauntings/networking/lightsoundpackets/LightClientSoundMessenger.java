package com.cartoonishvillain.eeriehauntings.networking.lightsoundpackets;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public class LightClientSoundMessenger {
    private static String ProtocolVersion = "1";
    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation("eeriehauntings", "lightsoundtrigger"),
            () -> ProtocolVersion, ProtocolVersion::equals, ProtocolVersion::equals);
    private static int ID = 0;

    public static void register() {
        INSTANCE.registerMessage(0, LightClientSoundPacket.class, LightClientSoundPacket::encode, LightClientSoundPacket::decode, LightClientSoundPacket::handle);
    }

    public static void sendTo(Object message, Player player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), message);
    }

}
