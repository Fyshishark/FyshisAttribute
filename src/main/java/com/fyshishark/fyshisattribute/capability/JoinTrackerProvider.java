package com.fyshishark.fyshisattribute.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JoinTrackerProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    
    public static Capability<JoinTracker> PLAYER_JOINED = CapabilityManager.get(new CapabilityToken<JoinTracker>() { });
    private JoinTracker join = null;
    private final LazyOptional<JoinTracker> optional = LazyOptional.of(this::create);
    
    private JoinTracker create() {
        if(join == null) join = new JoinTracker();
        return join;
    }
    
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        if(capability == PLAYER_JOINED) return optional.cast();
        return LazyOptional.empty();
    }
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        create().saveNBT(nbt);
        return nbt;
    }
    
    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        create().loadNBT(compoundTag);
    }
}
