package com.fyshishark.fyshisattribute.compat;

import net.minecraftforge.eventbus.api.IEventBus;

public interface ICompatibilityMod {
    void init(IEventBus forgeBus);
    boolean isInstalled();
    String getModID();
}
