package com.fyshishark.fyshisattribute.mixin;

import com.fyshishark.fyshisattribute.registry.AttributeRegistry;
import com.fyshishark.fyshisattribute.util.LivingEntityFyshiAttribute;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin (value = LivingEntity.class, priority = 900)
public abstract class LivingEntityMixin extends Entity implements LivingEntityFyshiAttribute {
    @Shadow public abstract double getAttributeValue(Attribute pAttribute);
    @Shadow @Nullable public abstract AttributeInstance getAttribute(Attribute pAttribute);
    @Shadow protected abstract void jumpFromGround();
    
    @Unique private int airSupplyTick;
    @Unique private int airJumpDelay = 12;
    @Unique private int amountOfJumps;
    @Shadow private int noJumpDelay;
    @Unique private Entity source;
    @Unique private boolean toggleAirJump = true;
    
    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    @Inject(
            method = "baseTick",
            at = @At("HEAD")
    )
    private void baseTick(CallbackInfo ci) {
        ++airSupplyTick;
    }
    
    @Inject(
            method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z",
            at = @At("HEAD")
    )
    private void getSource(MobEffectInstance pEffectInstance, Entity pEntity, CallbackInfoReturnable<Boolean> cir) {
        source = pEntity;
    }
    
    @ModifyVariable (
            method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z",
            at = @At(
                    value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;getEffect()Lnet/minecraft/world/effect/MobEffect;",
                    ordinal = 0
            ),
            argsOnly = true
    )
    private MobEffectInstance modifyEffect(MobEffectInstance value) {
        MobEffectInstance newValue = value;
        if (value.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
            if (getAttribute(AttributeRegistry.NEGATIVE_EFFECT_IN_DURATION.get()) != null) {
                newValue = new MobEffectInstance(
                        value.getEffect(),
                        (int)Math.floor(value.getDuration() * getAttributeValue(AttributeRegistry.NEGATIVE_EFFECT_IN_DURATION.get())),
                        value.getAmplifier(),
                        value.isAmbient(),
                        value.isVisible(),
                        value.showIcon(),
                        null,
                        value.getFactorData()
                );
            }
            
            value = newValue;
            if(source != null && source instanceof Player player) {
                if(player.getAttribute(AttributeRegistry.NEGATIVE_EFFECT_OUT_DURATION.get()) != null && player != getSelf()) {
                    newValue = new MobEffectInstance(
                            value.getEffect(),
                            (int)Math.floor(value.getDuration() * player.getAttributeValue(AttributeRegistry.NEGATIVE_EFFECT_OUT_DURATION.get())),
                            value.getAmplifier(),
                            value.isAmbient(),
                            value.isVisible(),
                            value.showIcon(),
                            null,
                            value.getFactorData()
                    );
                }
            }
        } else {
            if (getAttribute(AttributeRegistry.POSITIVE_EFFECT_IN_DURATION.get()) != null) {
                newValue = new MobEffectInstance(
                        value.getEffect(),
                        (int)Math.floor(value.getDuration() * getAttributeValue(AttributeRegistry.POSITIVE_EFFECT_IN_DURATION.get())),
                        value.getAmplifier(),
                        value.isAmbient(),
                        value.isVisible(),
                        value.showIcon(),
                        null,
                        value.getFactorData()
                );
            }
            
            value = newValue;
            if(source != null && source instanceof Player player) {
                if(player.getAttribute(AttributeRegistry.POSITIVE_EFFECT_OUT_DURATION.get()) != null && player != getSelf()) {
                    newValue = new MobEffectInstance(
                            value.getEffect(),
                            (int)Math.floor(value.getDuration() * player.getAttributeValue(AttributeRegistry.POSITIVE_EFFECT_OUT_DURATION.get())),
                            value.getAmplifier(),
                            value.isAmbient(),
                            value.isVisible(),
                            value.showIcon(),
                            null,
                            value.getFactorData()
                    );
                }
            }
        }
        
        return newValue;
    }
    
    @Inject (
            method = "increaseAirSupply",
            at = @At("RETURN"),
            cancellable = true
    )
    private void modifiedAirSupplyIncrease(int pCurrentAir, CallbackInfoReturnable<Integer> cir) {
        if(getAttribute(AttributeRegistry.RESPIRATORY.get()) == null) {
            cir.cancel();
            return;
        }
        
        if(airSupplyTick % (Math.max(getRespiratoryRoundedAttribute(), 1)) == 0) {
            int air = cir.getReturnValueI();
            if(getRespiratoryAttribute() < 1)
                air += (int)Math.round(4 / getRespiratoryAttribute());
            cir.setReturnValue(air);
        } else cir.setReturnValue(pCurrentAir);
    }
    
    @Inject (
            method = "decreaseAirSupply",
            at = @At("RETURN"),
            cancellable = true
    )
    private void modifiedAirSupplyDecrease(int pCurrentAir, CallbackInfoReturnable<Integer> cir) {
        if(getAttribute(AttributeRegistry.RESPIRATORY.get()) == null) {
            cir.cancel();
            return;
        }
        
        if(airSupplyTick % (Math.max(getRespiratoryRoundedAttribute(), 1)) == 0) {
            int air = cir.getReturnValueI();
            if(getRespiratoryAttribute() < 1)
                air -= (int)Math.round(1 / getRespiratoryAttribute());
            cir.setReturnValue(air);
        } else cir.setReturnValue(pCurrentAir);
    }
    
    @ModifyVariable (
            method = "aiStep",
            at = @At("STORE")
    )
    private boolean modifyFlag(boolean flag) {
        if(getSelf().getAttribute(AttributeRegistry.JUMP.get()) == null) {
            return flag;
        }
        return flag && amountOfJumps <= 0;
    }
    
    @Inject (
            method = "aiStep",
            at = @At("TAIL")
    )
    private void setAttribute(CallbackInfo ci) {
        if(getSelf() instanceof Player && (onGround() || isInFluidType())) {
            setJump(getMaxJumps());
        }
    }
    
    @Inject (
            method = "jumpFromGround",
            at = @At("HEAD"),
            cancellable = true
    )
    private void modifiedGroundJump(CallbackInfo ci) {
        if(getSelf().getAttribute(AttributeRegistry.JUMP.get()) != null) {
            if(amountOfJumps <= 0) {
                ci.cancel();
                return;
            }
            
            --amountOfJumps;
        }
    }
    
    @Inject (
            method = "aiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;jumpInFluid(Lnet/minecraftforge/fluids/FluidType;)V",
                    shift = At.Shift.BY,
                    by = 2
            )
    )
    public void jumpFromAir(CallbackInfo ci) {
        if (!onGround() && amountOfJumps > 0 && noJumpDelay == 0 && !isInFluidType() && getToggleAirJump()) {
            jumpFromGround();
            resetFallDistance();
            noJumpDelay = airJumpDelay;
        }
    }
    
    @ModifyConstant (
            method = "aiStep",
            constant = @Constant(intValue = 10)
    )
    private int modifyJumpDelay(int constant) {
        return !onGround() && amountOfJumps > 1 ? airJumpDelay : constant;
    }
    
    @Inject (
            method = "getJumpPower",
            at = @At("RETURN"),
            cancellable = true
    )
    private void modifiedJumpPower(CallbackInfoReturnable<Float> cir) {
        if(getAttribute(AttributeRegistry.JUMP_HEIGHT.get()) == null) {
            cir.cancel();
            return;
        }
        
        cir.setReturnValue(cir.getReturnValueF() * (float)getAttributeValue(AttributeRegistry.JUMP_HEIGHT.get()));
    }
    
    @ModifyConstant (
            method = "calculateFallDamage",
            constant = @Constant(floatValue = 3.0f)
    )
    private float modifyFallDamageCalculation(float value) {
        if(getAttribute(AttributeRegistry.JUMP_HEIGHT.get()) == null) return value;
        return value * (float)getAttributeValue(AttributeRegistry.JUMP_HEIGHT.get());
    }
    
    @Override
    public void toggleAirJump(boolean toggle) {
        toggleAirJump = toggle;
    }
    
    @Override
    public boolean getToggleAirJump() {
        return toggleAirJump;
    }
    
    @Override
    public int getMaxJumps() {
        return (int)Math.ceil(getSelf().getAttributeValue(AttributeRegistry.JUMP.get()));
    }
    
    @Override
    public void setJump(int i) {
        amountOfJumps = i;
    }
    
    @Override
    public void setJumpDelay(int i) {
        noJumpDelay = i;
    }
    
    @Override
    public int getJump() {
        return amountOfJumps;
    }
    
    
    @Unique public LivingEntity getSelf() { return (LivingEntity) (Object) this; }
    @Unique public int getRespiratoryRoundedAttribute() { return (int)Math.round(getAttributeValue(AttributeRegistry.RESPIRATORY.get())); }
    @Unique public double getRespiratoryAttribute() { return getAttributeValue(AttributeRegistry.RESPIRATORY.get()); }
}
