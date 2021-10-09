package com.cartoonishvillain.eeriehauntings.capabilities.playercapability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;
import java.util.concurrent.Callable;

public class PlayerCapability {
    @CapabilityInject(IPlayerCapability.class)
    public static Capability<IPlayerCapability> INSTANCE = null;

    public static void register(){
        CapabilityManager.INSTANCE.register(IPlayerCapability.class, new Capability.IStorage<IPlayerCapability>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<IPlayerCapability> capability, IPlayerCapability instance, Direction side) {
                CompoundNBT tag = new CompoundNBT();
                tag.putBoolean("haunted", instance.getIsHaunted());
                tag.putBoolean("anger", instance.getAnger());
                tag.putFloat("hauntchance", instance.getHauntChance());
                tag.putInt("ghosttype", instance.getGhostType());
                tag.putInt("protecteddays", instance.getProtectedDays());
                tag.putInt("hauntactivitytime", instance.getHauntActionTicks());
                tag.putInt("effectticks", instance.getVisualEffectTime());
                tag.putInt("effectid", instance.getEffectID());
                return tag;
            }

            @Override
            public void readNBT(Capability<IPlayerCapability> capability, IPlayerCapability instance, Direction side, INBT nbt) {
                CompoundNBT tag = (CompoundNBT) nbt;
                instance.setHaunted(tag.getBoolean("haunted"));
                instance.setAnger(tag.getBoolean("anger"));
                instance.setHauntChance(tag.getFloat("hauntchance"));
                instance.setGhostType(tag.getInt("ghosttype"));
                instance.setProtectedDays(tag.getInt("protecteddays"));
                instance.setHauntActionTicks(tag.getInt("hauntactivitytime"));
                instance.setVisualEffectTime(tag.getInt("effectticks"));
                instance.setEffectID(tag.getInt("effectid"));
            }
        }, new Callable<PlayerCapabilityManager>(){
            @Override
            public PlayerCapabilityManager call() throws Exception {
                return new PlayerCapabilityManager();
            }

        });
    }
}
