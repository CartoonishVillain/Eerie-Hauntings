package com.cartoonishvillain.eeriehauntings.capabilities.playercapability;

import com.cartoonishvillain.eeriehauntings.capabilities.worldcapability.IWorldCapability;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerCapabilityManager implements IPlayerCapability, ICapabilityProvider, INBTSerializable<CompoundTag> {
    public final LazyOptional<IPlayerCapability> holder = LazyOptional.of(()->this);
    protected boolean haunted = false;
    protected boolean anger = false;
    protected int hauntChance = 0;
    protected int ghostType = 0;
    protected int protectedDays = 0;
    @Override
    public int getHauntChance() {
        return hauntChance;
    }

    @Override
    public void setHauntChance(int hauntChance) {
        this.hauntChance = hauntChance;
    }

    @Override
    public void addHauntChance(int addedChance) {
        this.hauntChance += addedChance;
    }

    @Override
    public boolean getIsHaunted() {
        return haunted;
    }

    @Override
    public void setHaunted(boolean isHaunted) {
        this.haunted = isHaunted;
    }

    @Override
    public int getGhostType() {
        return ghostType;
    }

    @Override
    public void setGhostType(int Type) {
        ghostType = Type;
    }

    @Override
    public int getProtectedDays() {
        return protectedDays;
    }

    @Override
    public void setProtectedDays(int days) {
        protectedDays = days;
        if(protectedDays < 0) protectedDays = 0;
    }

    @Override
    public void setAnger(boolean isAngry) {
        anger = isAngry;
    }

    @Override
    public boolean getAnger() {
        return anger;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == PlayerCapability.INSTANCE){return PlayerCapability.INSTANCE.orEmpty(cap, this.holder);}
        else return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("haunted", haunted);
        tag.putBoolean("anger", anger);
        tag.putInt("hauntchance", hauntChance);
        tag.putInt("ghosttype", ghostType);
        tag.putInt("protecteddays", protectedDays);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        haunted = nbt.getBoolean("haunted");
        anger = nbt.getBoolean("anger");
        hauntChance = nbt.getInt("hauntchance");
        ghostType = nbt.getInt("ghosttype");
        protectedDays = nbt.getInt("protecteddays");
    }
}
