package com.cartoonishvillain.eeriehauntings.networking.shaderupdatepacket;

import com.cartoonishvillain.eeriehauntings.EerieHauntings;
import com.cartoonishvillain.eeriehauntings.capabilities.playercapability.PlayerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class ShaderUpdatePacket {

    private int ID;
    private int shaderID;
    private int ticks;

    public ShaderUpdatePacket(int id, int ticks, int shaderID){
        this.ID = id;
        this.ticks = ticks;
        this.shaderID = shaderID;
    }

    public ShaderUpdatePacket(FriendlyByteBuf packetBuffer) {
        ID = packetBuffer.readInt();
        ticks = packetBuffer.readInt();
        shaderID = packetBuffer.readInt();
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeInt(ID);
        buffer.writeInt(ticks);
        buffer.writeInt(shaderID);
    }

    public static ShaderUpdatePacket decode(FriendlyByteBuf buf) {
        return new ShaderUpdatePacket(buf);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(ID);
            if(entity instanceof Player){
                entity.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                    h.setVisualEffectTime(ticks);
                    h.setEffectID(shaderID);
                });
                ResourceLocation resourceLocation = idTranslator(shaderID);
                if(resourceLocation != null)
                Minecraft.getInstance().gameRenderer.loadEffect(resourceLocation);
                else{
                    Minecraft.getInstance().gameRenderer.shutdownEffect();
                }
            }
        });
        context.setPacketHandled(true);
    }

    public ResourceLocation idTranslator(int shaderID){
        switch (shaderID){
            default -> {return null;}
            case 1 -> {return new ResourceLocation("shaders/post/flip.json");}
            case 2 -> {return new ResourceLocation("shaders/post/blobs.json");}
        }
    }

}
