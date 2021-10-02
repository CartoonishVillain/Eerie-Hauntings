package com.cartoonishvillain.eeriehauntings.capabilities.playercapability;

public interface IPlayerCapability {
    int getHauntChance();
    void setHauntChance(int hauntChance);
    void addHauntChance(int addedChance);
    boolean getIsHaunted();
    void setHaunted(boolean isHaunted);
    int getGhostType();
    void setGhostType(int Type);
    int getProtectedDays();
    void setProtectedDays(int days);
    void setAnger(boolean isAngry);
    boolean getAnger();
}
