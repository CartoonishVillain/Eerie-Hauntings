package com.cartoonishvillain.eeriehauntings.networking.mediumsoundpackets;

import com.cartoonishvillain.eeriehauntings.Register;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MediumClientSoundPacket {

    private int ID;

    public MediumClientSoundPacket(int id){
        this.ID = id;
    }

    public MediumClientSoundPacket(PacketBuffer packetBuffer) {
        ID = packetBuffer.readInt();
    }

    public void encode(PacketBuffer buffer){
        buffer.writeInt(ID);
    }

    public static MediumClientSoundPacket decode(PacketBuffer buf) {
        return new MediumClientSoundPacket(buf);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(ID);
            if(entity instanceof PlayerEntity) {
                float randomPitch =  ((PlayerEntity) entity).getRandom().nextFloat();
                int xModifier = ((PlayerEntity) entity).getRandom().nextInt(3 + 3) - 3;
                int yModifier = ((PlayerEntity) entity).getRandom().nextInt(3 + 3) - 3;
                int zModifier = ((PlayerEntity) entity).getRandom().nextInt(3 + 3) - 3;
               entity.level.playSound((PlayerEntity) entity, entity.getX()+xModifier, entity.getY()+yModifier, entity.getZ()+zModifier, Register.MEDIUMSTRENGTHSOUNDS.get(), SoundCategory.MASTER, 1.25f, randomPitch*1.15f);
            }
        });
        context.setPacketHandled(true);
    }

}
