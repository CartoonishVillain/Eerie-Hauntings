package com.cartoonishvillain.eeriehauntings.networking.strongsoundpackets;

import com.cartoonishvillain.eeriehauntings.Register;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class StrongClientSoundPacket {

    private int ID;

    public StrongClientSoundPacket(int id){
        this.ID = id;
    }

    public StrongClientSoundPacket(PacketBuffer packetBuffer) {
        ID = packetBuffer.readInt();
    }

    public void encode(PacketBuffer buffer){
        buffer.writeInt(ID);
    }

    public static StrongClientSoundPacket decode(PacketBuffer buf) {
        return new StrongClientSoundPacket(buf);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(ID);
            if(entity instanceof PlayerEntity){
                float randomPitch =  ((PlayerEntity) entity).getRandom().nextFloat();
               entity.level.playSound((PlayerEntity) entity, entity.getX(), entity.getY(), entity.getZ(), Register.STRONGSTRENGTHSOUNDS.get(), SoundCategory.MASTER, 1.5f, randomPitch);
            }
        });
        context.setPacketHandled(true);
    }

}
