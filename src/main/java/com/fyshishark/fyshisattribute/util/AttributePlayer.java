package com.fyshishark.fyshisattribute.util;

public interface AttributePlayer {
    void activateAircrouch();
    void consumeAircrouch();
    void resetAircrouch();
    int getAircrouch();
    boolean isFloating();
    void tickAircrouch();
    void interruptAircrouch();
    
    boolean firstJoined();
}
