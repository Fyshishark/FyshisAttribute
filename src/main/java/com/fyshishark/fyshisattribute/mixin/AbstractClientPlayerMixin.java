package com.fyshishark.fyshisattribute.mixin;

import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin (value = AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin extends PlayerMixin {
    @Unique private float currentFOV = 1.00F;
    @ModifyVariable(
            method = "getFieldOfViewModifier",
            at = @At("STORE"),
            ordinal = 0
    )
    private float injected(float f) {
        if(isFloating) {
            if (currentFOV > 0.6f) currentFOV /= 1.005f;
            else currentFOV = 0.6f;
        }
        else currentFOV = 1;
        
        return isFloating ? currentFOV : f;
    }
}
