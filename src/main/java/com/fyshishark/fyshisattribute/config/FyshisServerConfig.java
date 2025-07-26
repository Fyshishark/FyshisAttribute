package com.fyshishark.fyshisattribute.config;

import com.fyshishark.fyshisattribute.registry.AttributeRegistry;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class FyshisServerConfig {
    public static final Builder BUILDER = new Builder();
    public static final ForgeConfigSpec SPEC;
    
    public static final ConfigValue<Integer>
            AIRCROUCH_VALUE, AIRCROUCH_TICK_VALUE,
            JUMP_VALUE;
    public static final ConfigValue<Double>
            ENERGY_VALUE,
            HUNGERSTEAL_VALUE,
            JUMP_HEIGHT_VALUE,
            LIFESTEAL_VALUE,
            NEGATIVE_EFFECT_IN_DURATION_VALUE,
            NEGATIVE_EFFECT_OUT_DURATION_VALUE,
            POSITIVE_EFFECT_IN_DURATION_VALUE,
            POSITIVE_EFFECT_OUT_DURATION_VALUE,
            REGENERATION_SPEED_VALUE,
            RESPIRATORY_VALUE;
    
    static {
        BUILDER.comment("I recommend changing the values at \"config/fyshishark/fyshisattribute-common.toml\"");
        AIRCROUCH_VALUE = BUILDER.defineInRange("aircrouch", FyshisCommonConfig.AIRCROUCH_VALUE, 0, AttributeRegistry.MAX);
        AIRCROUCH_TICK_VALUE = BUILDER.defineInRange("aircrouch-tick", FyshisCommonConfig.AIRCROUCH_TICK_VALUE, 0, 1200);
        ENERGY_VALUE = BUILDER.defineInRange("energy", FyshisCommonConfig.ENERGY_VALUE, 0.001, AttributeRegistry.MAX);
        HUNGERSTEAL_VALUE = BUILDER.defineInRange("hungersteal", FyshisCommonConfig.HUNGERSTEAL_VALUE, -AttributeRegistry.MAX, AttributeRegistry.MAX);
        JUMP_VALUE = BUILDER.defineInRange("jump", FyshisCommonConfig.JUMP_VALUE, 0, AttributeRegistry.MAX);
        JUMP_HEIGHT_VALUE = BUILDER.defineInRange("jump-height", FyshisCommonConfig.JUMP_HEIGHT_VALUE, 0.00, AttributeRegistry.MAX);
        LIFESTEAL_VALUE = BUILDER.defineInRange("lifesteal", FyshisCommonConfig.LIFESTEAL_VALUE, -AttributeRegistry.MAX, AttributeRegistry.MAX);
        NEGATIVE_EFFECT_IN_DURATION_VALUE = BUILDER.defineInRange("incoming-negative-duration", FyshisCommonConfig.NEGATIVE_EFFECT_IN_DURATION_VALUE, 0.00, AttributeRegistry.MAX);
        NEGATIVE_EFFECT_OUT_DURATION_VALUE = BUILDER.defineInRange("passing-negative-duration", FyshisCommonConfig.NEGATIVE_EFFECT_OUT_DURATION_VALUE, 0.00, AttributeRegistry.MAX);
        POSITIVE_EFFECT_IN_DURATION_VALUE = BUILDER.defineInRange("incoming-positive-duration", FyshisCommonConfig.POSITIVE_EFFECT_IN_DURATION_VALUE, 0.00, AttributeRegistry.MAX);
        POSITIVE_EFFECT_OUT_DURATION_VALUE = BUILDER.defineInRange("passing-positive-duration", FyshisCommonConfig.POSITIVE_EFFECT_OUT_DURATION_VALUE, 0.00, AttributeRegistry.MAX);
        REGENERATION_SPEED_VALUE = BUILDER.defineInRange("regeneration-speed", FyshisCommonConfig.REGENERATION_SPEED_VALUE, 0.001, AttributeRegistry.MAX);
        RESPIRATORY_VALUE = BUILDER.defineInRange("respiratory", FyshisCommonConfig.RESPIRATORY_VALUE, 0.001, AttributeRegistry.MAX);
        SPEC = BUILDER.build();
    }
}
