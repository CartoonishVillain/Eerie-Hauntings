package com.cartoonishvillain.eeriehauntings.capabilities.worldcapability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class WorldCapabilityManager implements IWorldCapability, ICapabilityProvider, INBTSerializable<CompoundNBT> {
    public final LazyOptional<IWorldCapability> holder = LazyOptional.of(()->this);
    protected boolean night = false;

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nonnull Direction side) {
        if(cap == WorldCapability.INSTANCE){ return WorldCapability.INSTANCE.orEmpty(cap, this.holder); }
        else return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("night", night);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        night = nbt.getBoolean("night");
    }

    @Override
    public boolean isNight() {
        return night;
    }

    @Override
    public void setisNight(boolean status) {
        night = status;
    }

}
