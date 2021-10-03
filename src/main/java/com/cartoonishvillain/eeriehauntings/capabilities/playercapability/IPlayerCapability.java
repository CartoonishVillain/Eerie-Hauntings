package com.cartoonishvillain.eeriehauntings.capabilities.playercapability;

public interface IPlayerCapability {
    float getHauntChance();
    void setHauntChance(float hauntChance);
    void addHauntChance(float addedChance);
    boolean getIsHaunted();
    void setHaunted(boolean isHaunted);
    int getGhostType();
    void setGhostType(int Type);
    int getProtectedDays();
    void setProtectedDays(int days);
    void setAnger(boolean isAngry);
    boolean getAnger();
    int getHauntActionTicks();
    void setHauntActionTicks(int ticks);
    boolean checkHauntActionTicks();
    int getVisualEffectTime();
    void setVisualEffectTime(int ticks);
    int getEffectID();
    void setEffectID(int ID);
}
