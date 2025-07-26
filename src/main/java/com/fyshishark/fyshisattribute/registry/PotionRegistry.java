package com.fyshishark.fyshisattribute.registry;

import com.fyshishark.fyshisattribute.FyshisAttribute;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PotionRegistry {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.Keys.POTIONS, FyshisAttribute.MODID);
    
    public static final RegistryObject<Potion>
            EXTEND_IN_POSITIVE_POTION = POTIONS.register("extend_in_positive_potion",
                () -> new Potion(new MobEffectInstance(EffectRegistry.EXTEND_IN_POSITIVE.get(), 1800, 0))),
            LONG_EXTEND_IN_POSITIVE_POTION = POTIONS.register("long_extend_in_positive_potion",
                    () -> new Potion(new MobEffectInstance(EffectRegistry.EXTEND_IN_POSITIVE.get(), 3600, 0))),
            STRONG_EXTEND_IN_POSITIVE_POTION = POTIONS.register("strong_extend_in_positive_potion",
                    () -> new Potion(new MobEffectInstance(EffectRegistry.EXTEND_IN_POSITIVE.get(), 900, 1))),
    
            SHORTEN_IN_POSITIVE_POTION = POTIONS.register("shorten_in_positive_potion",
                    () -> new Potion(new MobEffectInstance(EffectRegistry.SHORTEN_IN_POSITIVE.get(), 1800, 0))),
            LONG_SHORTEN_IN_POSITIVE_POTION = POTIONS.register("long_shorten_in_positive_potion",
                    () -> new Potion(new MobEffectInstance(EffectRegistry.SHORTEN_IN_POSITIVE.get(), 3600, 0))),
            STRONG_SHORTEN_IN_POSITIVE_POTION = POTIONS.register("strong_shorten_in_positive_potion",
                    () -> new Potion(new MobEffectInstance(EffectRegistry.SHORTEN_IN_POSITIVE.get(), 900, 1))),
    
            EXTEND_IN_NEGATIVE_POTION = POTIONS.register("extend_in_negative_potion",
                    () -> new Potion(new MobEffectInstance(EffectRegistry.EXTEND_IN_NEGATIVE.get(), 1800, 0))),
            LONG_EXTEND_IN_NEGATIVE_POTION = POTIONS.register("long_extend_in_negative_potion",
                    () -> new Potion(new MobEffectInstance(EffectRegistry.EXTEND_IN_NEGATIVE.get(), 3600, 0))),
            STRONG_EXTEND_IN_NEGATIVE_POTION = POTIONS.register("strong_extend_in_negative_potion",
                    () -> new Potion(new MobEffectInstance(EffectRegistry.EXTEND_IN_NEGATIVE.get(), 900, 1))),
    
            SHORTEN_IN_NEGATIVE_POTION = POTIONS.register("shorten_in_negative_potion",
                    () -> new Potion(new MobEffectInstance(EffectRegistry.SHORTEN_IN_NEGATIVE.get(), 1800, 0))),
            LONG_SHORTEN_IN_NEGATIVE_POTION = POTIONS.register("long_shorten_in_negative_potion",
                    () -> new Potion(new MobEffectInstance(EffectRegistry.SHORTEN_IN_NEGATIVE.get(), 3600, 0))),
            STRONG_SHORTEN_IN_NEGATIVE_POTION = POTIONS.register("strong_shorten_in_negative_potion",
                    () -> new Potion(new MobEffectInstance(EffectRegistry.SHORTEN_IN_NEGATIVE.get(), 900, 1)));
}
