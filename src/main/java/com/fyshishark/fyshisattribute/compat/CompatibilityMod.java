package com.fyshishark.fyshisattribute.compat;

import com.fyshishark.fyshisattribute.compat.parcool.ParcoolCompat;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.List;

public class CompatibilityMod {
    private static final List<ICompatibilityMod> MODS = List.of(
            new ParcoolCompat()
    );
    
    public static void init(IEventBus forgeBus) {
        for(ICompatibilityMod mod : MODS) {
            mod.init(forgeBus);
        }
    }
    
    public static boolean isInstalled(String s) {
        for(ICompatibilityMod mod : MODS)
            if(mod.isInstalled()) return true;
        return false;
    }
}
