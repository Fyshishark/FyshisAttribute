package com.fyshishark.fyshisattribute.event;

import com.fyshishark.fyshisattribute.FyshisAttribute;
import com.fyshishark.fyshisattribute.capability.JoinTracker;
import com.fyshishark.fyshisattribute.capability.JoinTrackerProvider;
import com.fyshishark.fyshisattribute.config.FyshisServerConfig;
import com.fyshishark.fyshisattribute.registry.AttributeRegistry;
import com.fyshishark.fyshisattribute.util.AttributeEntity;
import com.fyshishark.fyshisattribute.util.AttributePlayer;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FyshisAttribute.MODID)
public class FyshiCommonEvent {
    @SubscribeEvent
    public static void onLivingHurt(final LivingHurtEvent event) {
        if(event.getEntity() instanceof AttributePlayer player) {
            player.interruptAircrouch();
        }
        
        if(event.getSource() == null) return;
        DamageSource source = event.getSource();
        
        if(source.getEntity() instanceof Player attacker) {
            float damage = event.getAmount();
            float lifesteal = (float)attacker.getAttributeValue(AttributeRegistry.LIFESTEAL.get());
            
            if (lifesteal != 0) {
                float result = damage * lifesteal;
                attacker.heal(result);
            }
            
            float hungersteal = (float)attacker.getAttributeValue(AttributeRegistry.HUNGERSTEAL.get());
            if (hungersteal != 0) {
                int result = (int)Math.floor(damage * hungersteal);
                FoodData attackersFood = attacker.getFoodData();
                attackersFood.setFoodLevel(attackersFood.getFoodLevel() + result);
            }
        }
    }

    @SubscribeEvent
    public static void onLand(final LivingFallEvent event) {
        Entity entity = event.getEntity();
        if(entity instanceof AttributePlayer ap) {
            AttributeEntity ae = (AttributeEntity) entity;
            ae.onLand();
            if (entity.isCrouching() && ap.isFloating()) {
                event.setCanceled(true);
            }
        }
    }
    
    @SubscribeEvent
    public static void onSpawn(final PlayerEvent.PlayerRespawnEvent event) {
        setDefaultAttribute(event.getEntity());
    }
    
    @SubscribeEvent
    public static void onJoin(final PlayerEvent.PlayerLoggedInEvent event) {
        Player p = event.getEntity();
        p.getCapability(JoinTrackerProvider.PLAYER_JOINED).ifPresent(data -> {
            if(!data.hasJoinedBefore()) setDefaultAttribute(p);
        });
    }
    
    @SubscribeEvent
    public static void onAttachCapabilities(final AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player p
                && !p.getCapability(JoinTrackerProvider.PLAYER_JOINED).isPresent()) {
            event.addCapability(
                    new ResourceLocation("fyshislib", "properties"),
                    new JoinTrackerProvider()
            );
        }
    }
    @SubscribeEvent
    public static void onCloned(final PlayerEvent.Clone event) {
        event.getOriginal().getCapability(JoinTrackerProvider.PLAYER_JOINED).ifPresent(oldData -> {
            event.getEntity().getCapability(JoinTrackerProvider.PLAYER_JOINED).ifPresent(newData -> {
                newData.copyFrom(oldData);
            });
        });
    }
    @SubscribeEvent
    public static void onRegisterCapabilities(final RegisterCapabilitiesEvent event) {
        event.register(JoinTracker.class);
    }
    
    private static void setDefaultAttribute(LivingEntity entity) {
        entity.getAttribute(AttributeRegistry.AIRCROUCH.get()).setBaseValue(FyshisServerConfig.AIRCROUCH_VALUE.get());
        entity.getAttribute(AttributeRegistry.AIRCROUCH_TICK.get()).setBaseValue(FyshisServerConfig.AIRCROUCH_TICK_VALUE.get());
        entity.getAttribute(AttributeRegistry.ENERGY.get()).setBaseValue(FyshisServerConfig.ENERGY_VALUE.get());
        entity.getAttribute(AttributeRegistry.HUNGERSTEAL.get()).setBaseValue(FyshisServerConfig.HUNGERSTEAL_VALUE.get());
        entity.getAttribute(AttributeRegistry.JUMP.get()).setBaseValue(FyshisServerConfig.JUMP_VALUE.get());
        entity.getAttribute(AttributeRegistry.JUMP_HEIGHT.get()).setBaseValue(FyshisServerConfig.JUMP_HEIGHT_VALUE.get());
        entity.getAttribute(AttributeRegistry.NEGATIVE_EFFECT_IN_DURATION.get()).setBaseValue(FyshisServerConfig.NEGATIVE_EFFECT_IN_DURATION_VALUE.get());
        entity.getAttribute(AttributeRegistry.NEGATIVE_EFFECT_OUT_DURATION.get()).setBaseValue(FyshisServerConfig.NEGATIVE_EFFECT_OUT_DURATION_VALUE.get());
        entity.getAttribute(AttributeRegistry.POSITIVE_EFFECT_IN_DURATION.get()).setBaseValue(FyshisServerConfig.POSITIVE_EFFECT_IN_DURATION_VALUE.get());
        entity.getAttribute(AttributeRegistry.POSITIVE_EFFECT_OUT_DURATION.get()).setBaseValue(FyshisServerConfig.POSITIVE_EFFECT_OUT_DURATION_VALUE.get());
        entity.getAttribute(AttributeRegistry.REGENERATION_SPEED.get()).setBaseValue(FyshisServerConfig.REGENERATION_SPEED_VALUE.get());
        entity.getAttribute(AttributeRegistry.RESPIRATORY.get()).setBaseValue(FyshisServerConfig.RESPIRATORY_VALUE.get());
    }
}
