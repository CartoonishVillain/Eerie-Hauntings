package com.cartoonishvillain.eeriehauntings.networking.strongsoundpackets;

import com.cartoonishvillain.eeriehauntings.Register;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class StrongClientSoundPacket {

    private int ID;

    public StrongClientSoundPacket(int id){
        this.ID = id;
    }

    public StrongClientSoundPacket(FriendlyByteBuf packetBuffer) {
        ID = packetBuffer.readInt();
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeInt(ID);
    }

    public static StrongClientSoundPacket decode(FriendlyByteBuf buf) {
        return new StrongClientSoundPacket(buf);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(ID);
            if(entity instanceof Player){
                float randomPitch =  ((Player) entity).getRandom().nextFloat();
               entity.level.playSound((Player) entity, entity.getX(), entity.getY(), entity.getZ(), Register.STRONGSTRENGTHSOUNDS.get(), SoundSource.MASTER, 1.5f, randomPitch);
            }
        });
        context.setPacketHandled(true);
    }

}
