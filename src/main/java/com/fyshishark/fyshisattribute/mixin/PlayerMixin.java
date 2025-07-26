package com.fyshishark.fyshisattribute.mixin;

import com.fyshishark.fyshisattribute.event.PlayerAircrouchEvent;
import com.fyshishark.fyshisattribute.network.NetworkHandler;
import com.fyshishark.fyshisattribute.network.packet.C2SActivateAircrouch;
import com.fyshishark.fyshisattribute.network.packet.C2SAircrouchTick;
import com.fyshishark.fyshisattribute.network.packet.C2SConsumeAircrouch;
import com.fyshishark.fyshisattribute.registry.AttributeRegistry;
import com.fyshishark.fyshisattribute.registry.SoundRegistry;
import com.fyshishark.fyshisattribute.util.AttributePlayer;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin (value = Player.class)
public abstract class PlayerMixin extends LivingEntityMixin implements AttributePlayer {
    @Unique private int aircrouch;
    @Unique private int aircrouchTick;
    @Unique private boolean flagForNextAircrouch;
    @Unique private double aircrouchFall;
    @Unique private int lastJumpTick;
    @Unique private boolean stopSoundplayed;
    @Unique private int aircrouchInterrupted;
    @Unique private final static EntityDataAccessor<Integer> DATA_AIRCROUCHINTERRUPTED = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);
    @Unique private final static EntityDataAccessor<Boolean> FIRSTJOINED = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);
    
    @Inject(
            method = "defineSynchedData",
            at = @At("TAIL")
    )
    private void defineMyData(CallbackInfo ci) {
        entityData.define(DATA_AIRCROUCHINTERRUPTED, 0);
        entityData.define(FIRSTJOINED, false);
    }
    
    @Override
    protected void playerBaseTick() {
        if(rawOnGround()) {
            resetAircrouch();
        }
    }
    
    @ModifyConstant (
            method = "aiStep",
            constant = @Constant(intValue = 20)
    )
    private int inject(int x) {
        return (int)Math.round(x / getAttributeValue(AttributeRegistry.REGENERATION_SPEED.get()));
    }
    
    @Inject(
            method = "aiStep",
            at = @At("TAIL")
    )
    private void airstayAttribute(CallbackInfo ci) {
        if(getAttribute(AttributeRegistry.AIRCROUCH.get()) == null ||
                getAttribute(AttributeRegistry.AIRCROUCH_TICK.get()) == null) return;
        int syncedAircrouchInterrupt = entityData.get(DATA_AIRCROUCHINTERRUPTED);
        
        if(aircrouch > 0 && !isInFluidType()) {
            if (isCrouching() && !rawOnGround() &&
                    !isFloating && !flagForNextAircrouch && syncedAircrouchInterrupt <= 0) {
                activateAircrouch();
                lastJumpTick = getJumpDelay();
                aircrouchTick = (int) Math.floor(getAttributeValue(AttributeRegistry.AIRCROUCH_TICK.get()));
                aircrouchFall = -0.01;
                level().playLocalSound(blockPosition(), SoundRegistry.AIRCROUCH_START.get(), SoundSource.PLAYERS,
                        1, 1, true);
                
                NetworkHandler.sendToServer(new C2SActivateAircrouch());
                stopSoundplayed = false;
            }
            
            if (isFloating) {
                if (!isCrouching() || aircrouchTick < 0 || syncedAircrouchInterrupt > 0) {
                    stopAircrouch();
                } else {
                    tickAircrouch();
                    NetworkHandler.sendToServer(new C2SAircrouchTick());
                    MinecraftForge.EVENT_BUS.post(new PlayerAircrouchEvent.OnStartEvent(getSelf(), aircrouch));
                    
                    if (aircrouchTick <= 8 && !stopSoundplayed) {
                        level().playLocalSound(blockPosition(), SoundRegistry.AIRCROUCH_STOP.get(),
                                SoundSource.PLAYERS, 1, 1, true);
                        stopSoundplayed = true;
                    }
                    
                    if (aircrouchTick % 10 == 0) {
                        level().playLocalSound(blockPosition(), SoundRegistry.AIRCROUCH_LOOP.get(), SoundSource.PLAYERS,
                                (float)Math.min(1, 0.05 * aircrouchTick) * (((float)(aircrouchTick % 20) / 20) + 0.25f),
                                1, true);
                    }
                    
                }
            }
        }
        
        if(isInFluidType() && isFloating) {
            stopAircrouch();
        }
        
        if(syncedAircrouchInterrupt > 0) {
            entityData.set(DATA_AIRCROUCHINTERRUPTED, --syncedAircrouchInterrupt);
            aircrouchInterrupted--;
        }
        flagForNextAircrouch = isCrouching();
    }
    
    @Unique
    private void stopAircrouch() {
        setJumpDelay(lastJumpTick);
        consumeAircrouch();
        NetworkHandler.sendToServer(new C2SConsumeAircrouch());
        MinecraftForge.EVENT_BUS.post(new PlayerAircrouchEvent.OnEndEvent(getSelf(), aircrouch));
        if (aircrouchInterrupted > 0) {
            level().playLocalSound(blockPosition(), SoundRegistry.AIRCROUCH_INTERRUPTED.get(), SoundSource.PLAYERS,
                    1, 1, true);
        } else if (aircrouchTick > 0) {
            level().playLocalSound(blockPosition(), SoundRegistry.AIRCROUCH_CANCEL.get(), SoundSource.PLAYERS,
                    1, 1, true);
        }
    }
    
    @Override
    public void activateAircrouch() {
        isFloating = true;
    }
    
    @Override
    public void tickAircrouch() {
        Vec3 vec = getDeltaMovement();
        aircrouchFall = Math.max(-0.3, aircrouchFall + (aircrouchFall / 8));
        setDeltaMovement(vec.x(), aircrouchFall, vec.z());
        resetFallDistance();
        --aircrouchTick;
    }
    
    @Override
    public void consumeAircrouch() {
        isFloating = false;
        --aircrouch;
    }
    
    @Override
    public void resetAircrouch() {
        aircrouch = getAttribute(AttributeRegistry.AIRCROUCH.get()) != null ?
                (int)Math.floor(getAttributeValue(AttributeRegistry.AIRCROUCH.get())) : 0;
    }
    
    @Override
    public void interruptAircrouch() {
        entityData.set(DATA_AIRCROUCHINTERRUPTED, 20);
    }
    
    @Override public int getAircrouch() { return aircrouch; }
    @Override public boolean isFloating() { return isFloating; }
    @Override public boolean firstJoined() {
        if(!entityData.get(FIRSTJOINED)) {
            entityData.set(FIRSTJOINED, true);
            return true;
        }
        
        return false;
    }
}
