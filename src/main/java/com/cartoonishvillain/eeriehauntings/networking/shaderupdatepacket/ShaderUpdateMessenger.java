package com.cartoonishvillain.eeriehauntings.networking.shaderupdatepacket;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ShaderUpdateMessenger {
    private static String ProtocolVersion = "1";
    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation("eeriehauntings", "shaderupdate"),
            () -> ProtocolVersion, ProtocolVersion::equals, ProtocolVersion::equals);
    private static int ID = 0;

    public static void register() {
        INSTANCE.registerMessage(0, ShaderUpdatePacket.class, ShaderUpdatePacket::encode, ShaderUpdatePacket::decode, ShaderUpdatePacket::handle);
    }

    public static void sendTo(Object message, Player player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), message);
    }

}
