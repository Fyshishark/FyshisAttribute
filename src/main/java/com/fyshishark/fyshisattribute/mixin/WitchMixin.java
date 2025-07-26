package com.fyshishark.fyshisattribute.mixin;

import com.fyshishark.fyshisattribute.registry.AttributeRegistry;
import com.fyshishark.fyshisattribute.registry.EffectRegistry;
import com.fyshishark.fyshisattribute.registry.PotionRegistry;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.alchemy.Potion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Witch.class)
public class WitchMixin {
    @Inject(
            method = "createAttributes",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void extendCreateAttribute(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.setReturnValue(cir.getReturnValue()
                .add(AttributeRegistry.POSITIVE_EFFECT_OUT_DURATION.get())
                .add(AttributeRegistry.NEGATIVE_EFFECT_OUT_DURATION.get())
        );
    }
    
    @Inject(
            method = "performRangedAttack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/projectile/ThrownPotion;<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)V"
            )
    )
    private void performCustomRangedAttack(LivingEntity pTarget, float pDistanceFactor, CallbackInfo ci, @Local LocalRef<Potion> potion) {
        if(pTarget instanceof Raider) {
            if(!pTarget.hasEffect(EffectRegistry.EXTEND_IN_POSITIVE.get())) {
                potion.set(PotionRegistry.EXTEND_IN_POSITIVE_POTION.get());
            }
        }else if(!pTarget.hasEffect(EffectRegistry.EXTEND_IN_NEGATIVE.get())) {
            potion.set(PotionRegistry.EXTEND_IN_NEGATIVE_POTION.get());
        }
    }
}
