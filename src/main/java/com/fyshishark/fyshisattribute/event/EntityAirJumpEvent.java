package com.fyshishark.fyshisattribute.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.Event;

public class EntityAirJumpEvent extends Event {
    private final LivingEntity entity;
    public LivingEntity getEntity() {
        return entity;
    }
    
    private final Vec3 position;
    public Vec3 getPosition() {
        return position;
    }
    
    public EntityAirJumpEvent(LivingEntity entity, Vec3 position) {
        this.entity = entity;
        this.position = position;
    }
}
