package com.fyshishark.fyshisattribute.registry;

import com.fyshishark.fyshisattribute.FyshisAttribute;
import com.fyshishark.fyshisattribute.effect.DurationEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EffectRegistry {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.Keys.MOB_EFFECTS, FyshisAttribute.MODID);
    public static final RegistryObject<MobEffect>
            EXTEND_IN_POSITIVE = EFFECTS.register("extend_in_positive", () -> new DurationEffect(MobEffectCategory.BENEFICIAL, 0xb8e864)
                    .addAttributeModifier(AttributeRegistry.POSITIVE_EFFECT_IN_DURATION.get(), "d954a371-d23f-4a96-84aa-2f6e457c0805", 0.3, AttributeModifier.Operation.MULTIPLY_TOTAL)),
            SHORTEN_IN_POSITIVE = EFFECTS.register("shorten_in_positive", () -> new DurationEffect(MobEffectCategory.HARMFUL, 0x54613e)
                    .addAttributeModifier(AttributeRegistry.POSITIVE_EFFECT_IN_DURATION.get(), "874c4132-9b0d-4b56-b206-defa2db9b79c", -0.2, AttributeModifier.Operation.MULTIPLY_TOTAL)),
            EXTEND_IN_NEGATIVE = EFFECTS.register("extend_in_negative", () -> new DurationEffect(MobEffectCategory.HARMFUL, 0xd44681)
                    .addAttributeModifier(AttributeRegistry.NEGATIVE_EFFECT_IN_DURATION.get(), "d954a371-d23f-4a96-84aa-2f6e457c0805", 0.3, AttributeModifier.Operation.MULTIPLY_TOTAL)),
            SHORTEN_IN_NEGATIVE = EFFECTS.register("shorten_in_negative", () -> new DurationEffect(MobEffectCategory.BENEFICIAL, 0x5c3947)
                    .addAttributeModifier(AttributeRegistry.NEGATIVE_EFFECT_IN_DURATION.get(), "874c4132-9b0d-4b56-b206-defa2db9b79c", -0.2, AttributeModifier.Operation.MULTIPLY_TOTAL));
}
