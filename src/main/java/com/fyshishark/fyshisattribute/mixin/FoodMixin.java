package com.fyshishark.fyshisattribute.mixin;

import com.fyshishark.fyshisattribute.registry.AttributeRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin (value = FoodData.class)
public abstract class FoodMixin {
    @Unique private LivingEntity livingEntity;
    
    @Inject (
            method = "tick",
            at = @At("HEAD")
    )
    private void inject(Player pPlayer, CallbackInfo ci) {
        livingEntity = pPlayer;
    }
    
    @ModifyConstant (
            method = "tick",
            constant = @Constant(intValue = 10)
    )
    private int modifyFullHunger(int x) {
        return (int)Math.round(x / livingEntity.getAttributeValue(AttributeRegistry.REGENERATION_SPEED.get()));
    }
    
    @ModifyConstant (
            method = "tick",
            constant =
            @Constant(
                    intValue = 80,
                    ordinal = 0
            )
    )
    private int modifyLessHunger(int x) {
        return (int)Math.round(x / livingEntity.getAttributeValue(AttributeRegistry.REGENERATION_SPEED.get()));
    }
    
    @ModifyArg (
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/food/FoodData;addExhaustion(F)V"
            )
    )
    private float modifyFullArg(float f) {
        return f / (float) livingEntity.getAttributeValue(AttributeRegistry.REGENERATION_SPEED.get());
    }
    
    @ModifyConstant (
            method = "tick",
            constant = @Constant(
                    floatValue = 6.0f,
                    ordinal = 2)
    )
    private float modifyLessArg(float f) {
        return f / (float) livingEntity.getAttributeValue(AttributeRegistry.REGENERATION_SPEED.get());
    }
    
    @ModifyVariable (
            method = "addExhaustion",
            at = @At("HEAD"),
            argsOnly = true
    )
    private float modifyAddExhaustion(float f) {
        if(livingEntity == null) return f;
        return f / (float) livingEntity.getAttributeValue(AttributeRegistry.ENERGY.get());
    }
}
