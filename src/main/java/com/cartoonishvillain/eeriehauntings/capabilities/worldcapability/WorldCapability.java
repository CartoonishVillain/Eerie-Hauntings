package com.cartoonishvillain.eeriehauntings.capabilities.worldcapability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class WorldCapability {
    @CapabilityInject(IWorldCapability.class)
    public static Capability<IWorldCapability> INSTANCE = null;

}
