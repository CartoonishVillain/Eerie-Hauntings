package com.cartoonishvillain.eeriehauntings.networking.lightsoundpackets;

import com.cartoonishvillain.eeriehauntings.EerieHauntings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


public class LightClientSoundPacket {

    private int ID;
    private int soundID;

    public LightClientSoundPacket(int id, int soundID){
        this.ID = id;
        this.soundID = soundID;
    }

    public LightClientSoundPacket(PacketBuffer packetBuffer) {
        ID = packetBuffer.readInt();
        soundID = packetBuffer.readInt();
    }

    public void encode(PacketBuffer buffer){
        buffer.writeInt(ID);
        buffer.writeInt(soundID);
    }

    public static LightClientSoundPacket decode(PacketBuffer buf) {
        return new LightClientSoundPacket(buf);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(ID);
            if(entity instanceof PlayerEntity) {
                float randomPitch =  ((PlayerEntity) entity).getRandom().nextFloat();
                int xModifier = ((PlayerEntity) entity).getRandom().nextInt(6 + 6) - 6;
                int yModifier = ((PlayerEntity) entity).getRandom().nextInt(6 + 6) - 6;
                int zModifier = ((PlayerEntity) entity).getRandom().nextInt(6 + 6) - 6;
               entity.level.playSound((PlayerEntity) entity, entity.getX()+xModifier, entity.getY()+yModifier, entity.getZ()+zModifier, EerieHauntings.lowEndSounds.get(soundID), SoundCategory.MASTER, 1.25f, randomPitch*1.2f);
            }
        });
        context.setPacketHandled(true);
    }

}
