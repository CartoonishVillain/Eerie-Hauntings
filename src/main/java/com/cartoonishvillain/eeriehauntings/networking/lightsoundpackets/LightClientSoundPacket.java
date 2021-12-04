package com.cartoonishvillain.eeriehauntings.networking.lightsoundpackets;

import com.cartoonishvillain.eeriehauntings.EerieHauntings;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LightClientSoundPacket {

    private int ID;
    private int soundID;

    public LightClientSoundPacket(int id, int soundID){
        this.ID = id;
        this.soundID = soundID;
    }

    public LightClientSoundPacket(FriendlyByteBuf packetBuffer) {
        ID = packetBuffer.readInt();
        soundID = packetBuffer.readInt();
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeInt(ID);
        buffer.writeInt(soundID);
    }

    public static LightClientSoundPacket decode(FriendlyByteBuf buf) {
        return new LightClientSoundPacket(buf);
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
