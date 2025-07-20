package com.fyshishark.fyshisattribute.util;

public interface LivingEntityFyshiAttribute {
    int getMaxJumps();
    int getJump();
    void setJump(int i);
    void setJumpDelay(int i);
    
    void toggleAirJump(boolean toggle);
    boolean getToggleAirJump();
}
