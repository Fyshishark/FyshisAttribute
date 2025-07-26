package com.fyshishark.fyshisattribute.mixin;

import com.fyshishark.fyshisattribute.util.AttributeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin (value = Entity.class)
public abstract class EntityMixin implements AttributeEntity {
    @Shadow public abstract boolean isInFluidType();
    @Shadow public abstract void resetFallDistance();
    @Shadow public abstract boolean isCrouching();
    @Shadow public abstract Vec3 getDeltaMovement();
    @Shadow public abstract void setDeltaMovement(double pX, double pY, double pZ);
    @Shadow private boolean onGround;
    @Shadow public abstract double getX();
    @Shadow public abstract double getY();
    @Shadow public abstract double getZ();
    @Shadow public abstract Level level();
    @Shadow public abstract Vec3 position();
    @Shadow public abstract BlockPos blockPosition();
    
    @Shadow @Final protected SynchedEntityData entityData;
    
    @Shadow public abstract void setDeltaMovement(Vec3 pDeltaMovement);
    
    @Unique
    protected boolean isFloating;
    @Inject (
            method = "onGround",
            at = @At("RETURN"),
            cancellable = true
    )
    private void modifyOnGround(CallbackInfoReturnable<Boolean> cir) {
        if(onGround) isFloating = false;
        
        cir.setReturnValue(onGround || isFloating);
    }
    
    @Override
    public boolean rawOnGround() {
        return onGround;
    }
}
