package com.fyshishark.fyshisattribute.event;

import com.fyshishark.fyshisattribute.FyshisAttribute;
import com.fyshishark.fyshisattribute.gui.VignetteOverlay;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = FyshisAttribute.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FyshiClientEvent {
    @SubscribeEvent
    public static void registerGuiOverlays(final RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("floatvignette", VignetteOverlay.VIGNETTE_OVERLAY);
    }
}
