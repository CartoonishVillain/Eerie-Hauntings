package com.cartoonishvillain.eeriehauntings.networking;

import com.cartoonishvillain.eeriehauntings.EerieHauntings;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientSoundPacket {

    private int ID;
    private int soundID;

    public ClientSoundPacket(int id, int soundID){
        this.ID = id;
        this.soundID = soundID;
    }

    public ClientSoundPacket(FriendlyByteBuf packetBuffer) {
        ID = packetBuffer.readInt();
        soundID = packetBuffer.readInt();
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeInt(ID);
        buffer.writeInt(soundID);
    }

    public static ClientSoundPacket decode(FriendlyByteBuf buf) {
        return new ClientSoundPacket(buf);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(ID);
            if(entity instanceof Player){
                float randomPitch =  ((Player) entity).getRandom().nextFloat();
                int xModifier = ((Player) entity).getRandom().nextInt(6 + 6) - 6;
                int yModifier = ((Player) entity).getRandom().nextInt(6 + 6) - 6;
                int zModifier = ((Player) entity).getRandom().nextInt(6 + 6) - 6;
               entity.level.playSound((Player) entity, entity.getX()+xModifier, entity.getY()+yModifier, entity.getZ()+zModifier, EerieHauntings.lowEndSounds.get(soundID), SoundSource.MASTER, 1.25f, randomPitch*1.2f);
            }
        });
        context.setPacketHandled(true);
    }

}
