package com.fyshishark.fyshisattribute.compat.parcool;

import com.fyshishark.fyshisattribute.compat.ICompatibilityMod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;

public class ParcoolCompat implements ICompatibilityMod {
    private static boolean installed;
    
    @Override
    public void init(IEventBus forgeBus) {
        var modFile = ModList.get().getModFileById(getModID());
        installed = modFile != null;
        if(installed) {
            forgeBus.register(ParcoolEvent.class);
        }
    }
    
    @Override
    public boolean isInstalled() {
        return installed;
    }
    
    @Override
    public String getModID() {
        return "parcool";
    }
}
