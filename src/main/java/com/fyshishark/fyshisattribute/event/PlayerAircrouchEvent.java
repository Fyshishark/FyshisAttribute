package com.fyshishark.fyshisattribute.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public class PlayerAircrouchEvent extends Event {
    private final LivingEntity entity;
    public LivingEntity getEntity() {
        return entity;
    }
    
    private final int amount;
    public int getAmount() { return amount; }
    
    public PlayerAircrouchEvent(LivingEntity entity, int amount) {
        this.entity = entity;
        this.amount = amount;
    }
    
    public static class OnStartEvent extends PlayerAircrouchEvent {
        public OnStartEvent(LivingEntity entity, int amount) { super(entity, amount); }
    }
    
    public static class OnEndEvent extends PlayerAircrouchEvent {
        public OnEndEvent(LivingEntity entity, int amount) {
            super(entity, amount);
        }
    }
}
