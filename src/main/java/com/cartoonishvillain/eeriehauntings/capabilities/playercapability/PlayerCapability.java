package com.cartoonishvillain.eeriehauntings.capabilities.playercapability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class PlayerCapability {
    @CapabilityInject(IPlayerCapability.class)
    public static Capability<IPlayerCapability> INSTANCE = null;
}
