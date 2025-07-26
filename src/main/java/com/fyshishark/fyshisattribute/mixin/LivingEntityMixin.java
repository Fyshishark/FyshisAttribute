package com.fyshishark.fyshisattribute.mixin;

import com.fyshishark.fyshisattribute.event.EntityAirJumpEvent;
import com.fyshishark.fyshisattribute.registry.AttributeRegistry;
import com.fyshishark.fyshisattribute.registry.EffectRegistry;
import com.fyshishark.fyshisattribute.registry.SoundRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeLivingEntity;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin (value = LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin implements IForgeLivingEntity {
    @Shadow public abstract double getAttributeValue(Attribute pAttribute);
    @Shadow @Nullable public abstract AttributeInstance getAttribute(Attribute pAttribute);
    @Shadow protected abstract void jumpFromGround();
    
    @Unique private int airSupplyTick;
    @Unique private int airJumpDelay = 12;
    @Unique private int amountOfJumps;
    @Shadow private int noJumpDelay;
    
    @Unique private Entity source;
    @Unique private boolean toggleAirJump = true;
    @Unique private boolean jumpedFromGround;
    @Unique protected boolean wasInFluid;
    
    @Inject(
            method = "createLivingAttributes",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void extendCreateAttribute(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.setReturnValue(cir.getReturnValue()
                .add(AttributeRegistry.JUMP_HEIGHT.get())
                .add(AttributeRegistry.LIFESTEAL.get())
                .add(AttributeRegistry.NEGATIVE_EFFECT_IN_DURATION.get())
                .add(AttributeRegistry.POSITIVE_EFFECT_IN_DURATION.get())
                .add(AttributeRegistry.RESPIRATORY.get())
        );
    }
    
    @Inject(
            method = "baseTick",
            at = @At("HEAD")
    )
    private void baseTick(CallbackInfo ci) {
        ++airSupplyTick;
        if(isFloating) noJumpDelay = 100;
        playerBaseTick();
    }
    @Unique protected void playerBaseTick() {}
    
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
        if(value.getEffect() == EffectRegistry.EXTEND_IN_POSITIVE.get()
        || value.getEffect() == EffectRegistry.SHORTEN_IN_POSITIVE.get()
        || value.getEffect() == EffectRegistry.EXTEND_IN_NEGATIVE.get()
        || value.getEffect() == EffectRegistry.SHORTEN_IN_NEGATIVE.get())
            return value;
        
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
            if(source != null && source instanceof LivingEntity self) {
                if(self.getAttribute(AttributeRegistry.NEGATIVE_EFFECT_OUT_DURATION.get()) != null && self != getSelf()) {
                    newValue = new MobEffectInstance(
                            value.getEffect(),
                            (int)Math.floor(value.getDuration() * self.getAttributeValue(AttributeRegistry.NEGATIVE_EFFECT_OUT_DURATION.get())),
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
            if(source != null && source instanceof LivingEntity self) {
                if(self.getAttribute(AttributeRegistry.POSITIVE_EFFECT_OUT_DURATION.get()) != null && self != getSelf()) {
                    newValue = new MobEffectInstance(
                            value.getEffect(),
                            (int)Math.floor(value.getDuration() * self.getAttributeValue(AttributeRegistry.POSITIVE_EFFECT_OUT_DURATION.get())),
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
        if(getAttribute(AttributeRegistry.JUMP.get()) == null) {
            return flag;
        }
        
        return flag && amountOfJumps <= 0 && !isFloating;
    }
    
    @Inject (
            method = "aiStep",
            at = @At("TAIL")
    )
    private void setAttribute(CallbackInfo ci) {
        if(getSelf() instanceof Player && (rawOnGround() || isInFluidType())) {
            setJump(getMaxJumps());
        }
    }
    
    @Inject (
            method = "jumpFromGround",
            at = @At("HEAD"),
            cancellable = true
    )
    private void modifiedGroundJump(CallbackInfo ci) {
        if(getAttribute(AttributeRegistry.JUMP.get()) != null) {
            if(wasInFluid && !rawOnGround()) {
                --amountOfJumps;
                wasInFluid = false;
            }
            
            if(amountOfJumps <= 0 && !isFloating) {
                ci.cancel();
                return;
            }
            
            --amountOfJumps;
            if(rawOnGround()) jumpedFromGround = true;
            else {
                int maxParticle = 16;
                double speed = 0.05,
                        offset = 0.1,
                        x, z, result;
                for(int i = 0; i < maxParticle; i++) {
                    result = (double)i / maxParticle;
                    x = Math.sin(result * Math.PI * 2);
                    z = Math.cos(result * Math.PI * 2);
                    
                    level().addParticle(
                            ParticleTypes.END_ROD,
                            getX() + (x * offset),
                            getY() - (x * getDeltaMovement().x()) - (z * getDeltaMovement().z()),
                            getZ() + (z * offset),
                            x * speed,
                            ((x * getDeltaMovement().x()) - (z * getDeltaMovement().z())) * speed,
                            z * speed
                    );
                }
            }
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
        if (!rawOnGround() && amountOfJumps > 0 && noJumpDelay == 0
                && !isInFluidType() && getToggleAirJump() && !isFloating) {
            if (!jumpedFromGround()) {
                --amountOfJumps;
                if (amountOfJumps <= 0) return;
            }
            jumpFromGround();
            resetFallDistance();
            noJumpDelay = airJumpDelay;
            
            int maxAirJumps = getMaxJumps() - 1;
            float pitch = (((float)(maxAirJumps - amountOfJumps) / maxAirJumps) * 1.5f) + 0.5f;
            
            level().playSound(getSelf(), blockPosition(), SoundRegistry.MULTIJUMP.get(),
                    SoundSource.PLAYERS, 1, pitch);
            
            MinecraftForge.EVENT_BUS.post(new EntityAirJumpEvent(getSelf(), position()));
        }
    }
    
    @Override
    public void jumpInFluid(FluidType type) {
        setDeltaMovement(
                getDeltaMovement().add(
                        0.0,
                        0.03999999910593033 * this.self().getAttributeValue((Attribute) ForgeMod.SWIM_SPEED.get()),
                        0.0
                )
        );
        wasInFluid = true;
    }
    
    @ModifyConstant (
            method = "aiStep",
            constant = @Constant(intValue = 10)
    )
    private int modifyJumpDelay(int constant) {
        return !rawOnGround() && amountOfJumps > 1 ? airJumpDelay : constant;
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
    public void onLand() {
        if(rawOnGround()) {
            wasInFluid = false;
            jumpedFromGround = false;
            isFloating = false;
        }
    }
    
    @Unique public LivingEntity getSelf() { return (LivingEntity) (Object) this; }
    @Unique public int getRespiratoryRoundedAttribute() { return (int)Math.round(getAttributeValue(AttributeRegistry.RESPIRATORY.get())); }
    @Unique public double getRespiratoryAttribute() { return getAttributeValue(AttributeRegistry.RESPIRATORY.get()); }
    @Override public void toggleAirJump(boolean toggle) { toggleAirJump = toggle; }
    @Override public boolean getToggleAirJump() { return toggleAirJump; }
    @Override public void setJump(int i) { amountOfJumps = i; }
    @Override public int getJump() { return amountOfJumps; }
    @Override public int getMaxJumps() { return (int)Math.ceil(getAttributeValue(AttributeRegistry.JUMP.get())); }
    @Override public void setJumpDelay(int i) { noJumpDelay = i; }
    @Unique public int getJumpDelay() { return noJumpDelay; }
    @Override public boolean jumpedFromGround() { return jumpedFromGround; }
}
