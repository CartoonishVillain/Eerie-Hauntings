package com.cartoonishvillain.eeriehauntings.networking.mediumsoundpackets;

import com.cartoonishvillain.eeriehauntings.Register;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MediumClientSoundPacket {

    private int ID;

    public MediumClientSoundPacket(int id){
        this.ID = id;
    }

    public MediumClientSoundPacket(FriendlyByteBuf packetBuffer) {
        ID = packetBuffer.readInt();
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeInt(ID);
    }

    public static MediumClientSoundPacket decode(FriendlyByteBuf buf) {
        return new MediumClientSoundPacket(buf);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(ID);
            if(entity instanceof Player){
                float randomPitch =  ((Player) entity).getRandom().nextFloat();
                int xModifier = ((Player) entity).getRandom().nextInt(3 + 3) - 3;
                int yModifier = ((Player) entity).getRandom().nextInt(3 + 3) - 3;
                int zModifier = ((Player) entity).getRandom().nextInt(3 + 3) - 3;
               entity.level.playSound((Player) entity, entity.getX()+xModifier, entity.getY()+yModifier, entity.getZ()+zModifier, Register.MEDIUMSTRENGTHSOUNDS.get(), SoundSource.MASTER, 1.25f, randomPitch*1.15f);
            }
        });
        context.setPacketHandled(true);
    }

}
