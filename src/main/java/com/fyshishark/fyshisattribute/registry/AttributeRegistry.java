package com.fyshishark.fyshisattribute.registry;

import com.fyshishark.fyshisattribute.FyshisAttribute;
import com.fyshishark.fyshisattribute.capability.JoinTracker;
import com.fyshishark.fyshisattribute.capability.JoinTrackerProvider;
import com.fyshishark.fyshisattribute.config.FyshisServerConfig;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AttributeRegistry {
    private static final String PREFIX = String.format("attribute.%s.", FyshisAttribute.MODID);
    public static final int MAX = 255;
    
    public static final DeferredRegister<Attribute> ATTRIBUTE = DeferredRegister.create(ForgeRegistries.Keys.ATTRIBUTES, FyshisAttribute.MODID);
    public static final RegistryObject<Attribute>
            AIRCROUCH                       = add("aircrouch", 0, 0),
            AIRCROUCH_TICK                  = add("aircrouch_tick", 30, 0, 1200),
            ENERGY                          = add("energy", 1, 0.001),
            HUNGERSTEAL                     = add("hungersteal", 0),
            JUMP                            = add("jump", 1, 0),
            JUMP_HEIGHT                     = add("jump_height", 1, 0),
            LIFESTEAL                       = add("lifesteal", 0),
            NEGATIVE_EFFECT_IN_DURATION     = add("negative_effect_in_duration", 1, 0),
            NEGATIVE_EFFECT_OUT_DURATION    = add("negative_effect_out_duration", 1, 0),
            POSITIVE_EFFECT_IN_DURATION     = add("positive_effect_in_duration", 1, 0),
            POSITIVE_EFFECT_OUT_DURATION    = add("positive_effect_out_duration", 1, 0),
            REGENERATION_SPEED              = add("regeneration_speed", 1, 0.001),
            RESPIRATORY                     = add("respiratory", 1, 0.001);
    
    public static RegistryObject<Attribute> add(String id, double value) { return add(id, value, -MAX, MAX); }
    public static RegistryObject<Attribute> add(String id, double value, double min) { return add(id, value, min, MAX); }
    public static RegistryObject<Attribute> add(String id, double value, double min, double max) {
        return ATTRIBUTE.register(id, () -> new RangedAttribute(PREFIX + id, value, min, max).setSyncable(true));
    }
    
    @SubscribeEvent
    public static void setAttributes(final EntityAttributeModificationEvent event) {
        ATTRIBUTE.getEntries().forEach(attribute -> event.add(EntityType.PLAYER, attribute.get()));
    }
}
