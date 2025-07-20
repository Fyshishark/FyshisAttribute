package com.fyshishark.fyshisattribute.mixin;

import com.fyshishark.fyshisattribute.registry.AttributeRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin (value = Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    @ModifyConstant (
            method = "aiStep",
            constant = @Constant(intValue = 20)
    )
    private int inject(int x) {
        return (int)Math.round(x / getAttributeValue(AttributeRegistry.REGENERATION_SPEED.get()));
    }
}
