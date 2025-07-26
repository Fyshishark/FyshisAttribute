package com.fyshishark.fyshisattribute.config;

import com.fyshishark.fyshisattribute.registry.AttributeRegistry;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec;

public class FyshisCommonConfig {
    public static final Builder BUILDER = new Builder();
    public static final ForgeConfigSpec SPEC;
    
    public static final ConfigValue<Integer>
            AIRCROUCH_VALUE,
            AIRCROUCH_TICK_VALUE,
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
        BUILDER.push("Config for Fyshis Attribute");
        BUILDER.comment("If you're planning to change the attribute for existing world, delete/access the file in saves/<YOUR WORLD>/serverconfig/fyshisattribute-server.toml");
        BUILDER.comment("1. Leave the world first");
        BUILDER.comment("2. Access/Delete the fyshisattribute-server.toml");
        BUILDER.comment("3. Join and respawn to apply the new default value of attribute.");
        BUILDER.comment("That's how attribute works, it goes back to its default value after respawn.");
        BUILDER.comment("");
        
        AIRCROUCH_VALUE = BUILDER.comment("Default Value for \"fyshisattribute:aircrouch\" (Default: 0)")
                .defineInRange("Aircrouch Default", 0, 0, AttributeRegistry.MAX);
        BUILDER.comment("");
        
        AIRCROUCH_TICK_VALUE = BUILDER.comment("Default Value for \"fyshisattribute:aircrouch_tick\" (Default: 30)")
                .defineInRange("Aircrouch Tick Default", 30, 0, 1200);
        BUILDER.comment("");
        
        ENERGY_VALUE = BUILDER.comment("Default Value for \"fyshisattribute:energy\" (Default: 1.00)")
                .defineInRange("Energy Default", 1.00, 0.001, AttributeRegistry.MAX);
        BUILDER.comment("");
        
        HUNGERSTEAL_VALUE = BUILDER.comment("Default Value for \"fyshisattribute:hungersteal\" (Default: 0.00)")
                .defineInRange("Hungersteal Default", 0.00, -AttributeRegistry.MAX, AttributeRegistry.MAX);
        BUILDER.comment("");
        
        JUMP_VALUE = BUILDER.comment("Default Value for \"fyshisattribute:jump\" (Default: 1)")
                .defineInRange("Jump Default", 1, 0, AttributeRegistry.MAX);
        BUILDER.comment("");
        
        JUMP_HEIGHT_VALUE = BUILDER.comment("Default Value for \"fyshisattribute:jump_height\" (Default: 1.00)")
                .defineInRange("Jump Height Default", 1.00, 0, AttributeRegistry.MAX);
        BUILDER.comment("");
        
        LIFESTEAL_VALUE = BUILDER.comment("Default Value for \"fyshisattribute:lifesteal\" (Default: 0.00)")
                .defineInRange("Lifesteal Default", 0.00, -AttributeRegistry.MAX, AttributeRegistry.MAX);
        BUILDER.comment("");
        
        NEGATIVE_EFFECT_IN_DURATION_VALUE = BUILDER.comment("Default Value for \"fyshisattribute:negative_effect_in_duration\" (Default: 1.00)")
                .defineInRange("Incoming Negative Duration Default", 1.00, 0.00, AttributeRegistry.MAX);
        BUILDER.comment("");
        
        NEGATIVE_EFFECT_OUT_DURATION_VALUE = BUILDER.comment("Default Value for \"fyshisattribute:negative_effect_out_duration\" (Default: 1.00)")
                .defineInRange("Passing Negative Duration Default", 1.00, 0.00, AttributeRegistry.MAX);
        BUILDER.comment("");
        
        POSITIVE_EFFECT_IN_DURATION_VALUE = BUILDER.comment("Default Value for \"fyshisattribute:positive_effect_in_duration\" (Default: 1.00)")
                .defineInRange("Incoming Positive Duration Default", 1.00, 0.00, AttributeRegistry.MAX);
        BUILDER.comment("");
        
        POSITIVE_EFFECT_OUT_DURATION_VALUE = BUILDER.comment("Default Value for \"fyshisattribute:positive_effect_out_duration\" (Default: 1.00)")
                .defineInRange("Passing Positive Duration Default", 1.00, 0.00, AttributeRegistry.MAX);
        BUILDER.comment("");
        
        REGENERATION_SPEED_VALUE = BUILDER.comment("Default Value for \"fyshisattribute:regeneration_speed\" (Default: 1.00)")
                .defineInRange("Regeneration Speed Default", 1.00, 0.001, AttributeRegistry.MAX);
        BUILDER.comment("");
        
        RESPIRATORY_VALUE = BUILDER.comment("Default Value for \"fyshisattribute:respiratory\" (Default: 1.00)")
                .defineInRange("Respiratory Default", 1.00, 0.001, AttributeRegistry.MAX);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
