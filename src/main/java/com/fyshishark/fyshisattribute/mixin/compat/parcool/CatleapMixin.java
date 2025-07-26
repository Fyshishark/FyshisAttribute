package com.fyshishark.fyshisattribute.mixin.compat.parcool;

import com.alrex.parcool.common.action.impl.CatLeap;
import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.capability.Parkourability;
import com.fyshishark.fyshisattribute.util.AttributePlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin (value = CatLeap.class)
public class CatleapMixin {
    @Shadow private int coolTimeTick;
    
    @Inject (
            method = "onTick",
            at = @At("TAIL"),
            remap = false
    )
    private void injected(Player player, Parkourability parkourability, IStamina stamina, CallbackInfo ci) {
        if(player instanceof AttributePlayer p) {
            coolTimeTick = p.isFloating() ? 0 : coolTimeTick;
        }
    }
}
