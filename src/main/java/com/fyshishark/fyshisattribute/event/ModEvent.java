package com.fyshishark.fyshisattribute.event;

import com.fyshishark.fyshisattribute.FyshisAttribute;
import com.fyshishark.fyshisattribute.registry.AttributeRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FyshisAttribute.MODID)
public class ModEvent {
    @SubscribeEvent
    public static void OnLivingHurt(final LivingHurtEvent event) {
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
}
