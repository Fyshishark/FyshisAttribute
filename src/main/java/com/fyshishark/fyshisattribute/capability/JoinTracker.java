package com.fyshishark.fyshisattribute.capability;

import net.minecraft.nbt.CompoundTag;

public class JoinTracker {
    private int joinTrack;
    private final String INT_KEY = "joinedTracker";
    private boolean hasJoined;
    private final String BOOLEAN_KEY = "hasJoined";
    
    public boolean hasJoinedBefore() {
        ++joinTrack;
        if(!hasJoined) {
            hasJoined = true;
            return false;
        } return true;
    }
    
    public void copyFrom(JoinTracker source) {
        this.joinTrack = source.joinTrack;
        this.hasJoined = source.hasJoined;
    }
    
    public void saveNBT(CompoundTag nbt) {
        nbt.putInt(INT_KEY, joinTrack);
        nbt.putBoolean(BOOLEAN_KEY, hasJoined);
    }
    
    public void loadNBT(CompoundTag nbt) {
        joinTrack = nbt.getInt(INT_KEY);
        hasJoined = nbt.getBoolean(BOOLEAN_KEY);
    }
}
