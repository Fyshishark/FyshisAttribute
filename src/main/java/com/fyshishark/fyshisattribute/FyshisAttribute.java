package com.fyshishark.fyshisattribute;

import com.fyshishark.fyshisattribute.compat.CompatibilityMod;
import com.fyshishark.fyshisattribute.config.FyshisCommonConfig;
import com.fyshishark.fyshisattribute.config.FyshisServerConfig;
import com.fyshishark.fyshisattribute.network.NetworkHandler;
import com.fyshishark.fyshisattribute.registry.AttributeRegistry;
import com.fyshishark.fyshisattribute.registry.EffectRegistry;
import com.fyshishark.fyshisattribute.registry.PotionRegistry;
import com.fyshishark.fyshisattribute.registry.SoundRegistry;
import com.fyshishark.fyshisattribute.util.FyshisBrewingRecipe;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.List;

@Mod(FyshisAttribute.MODID)
public class FyshisAttribute {
    public static final String MODID = "fyshisattribute";
    public static final List<Double> defaultValue = List.of();
    
    public FyshisAttribute(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, FyshisCommonConfig.SPEC, "fyshishark/fyshisattribute-common.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, FyshisServerConfig.SPEC, "fyshisattribute-server.toml");
        
        AttributeRegistry.ATTRIBUTE.register(modEventBus);
        EffectRegistry.EFFECTS.register(modEventBus);
        PotionRegistry.POTIONS.register(modEventBus);
        SoundRegistry.SOUND_EVENTS.register(modEventBus);
        
        CompatibilityMod.init(forgeBus);
        modEventBus.addListener(this::commonSetup);
    }
    
    @SubscribeEvent
    public void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            NetworkHandler.register();
            BrewingRecipeRegistry.addRecipe(new FyshisBrewingRecipe(Potions.AWKWARD, Items.REDSTONE_BLOCK, PotionRegistry.EXTEND_IN_POSITIVE_POTION.get()));
            BrewingRecipeRegistry.addRecipe(new FyshisBrewingRecipe(PotionRegistry.EXTEND_IN_POSITIVE_POTION.get(), Items.REDSTONE, PotionRegistry.LONG_EXTEND_IN_POSITIVE_POTION.get()));
            BrewingRecipeRegistry.addRecipe(new FyshisBrewingRecipe(PotionRegistry.EXTEND_IN_POSITIVE_POTION.get(), Items.GLOWSTONE_DUST, PotionRegistry.STRONG_EXTEND_IN_POSITIVE_POTION.get()));
            
            BrewingRecipeRegistry.addRecipe(new FyshisBrewingRecipe(PotionRegistry.EXTEND_IN_POSITIVE_POTION.get(), Items.FERMENTED_SPIDER_EYE, PotionRegistry.SHORTEN_IN_POSITIVE_POTION.get()));
            BrewingRecipeRegistry.addRecipe(new FyshisBrewingRecipe(PotionRegistry.SHORTEN_IN_POSITIVE_POTION.get(), Items.REDSTONE, PotionRegistry.LONG_SHORTEN_IN_POSITIVE_POTION.get()));
            BrewingRecipeRegistry.addRecipe(new FyshisBrewingRecipe(PotionRegistry.SHORTEN_IN_POSITIVE_POTION.get(), Items.GLOWSTONE_DUST, PotionRegistry.STRONG_SHORTEN_IN_POSITIVE_POTION.get()));
            
            BrewingRecipeRegistry.addRecipe(new FyshisBrewingRecipe(PotionRegistry.SHORTEN_IN_POSITIVE_POTION.get(), Items.FERMENTED_SPIDER_EYE, PotionRegistry.EXTEND_IN_NEGATIVE_POTION.get()));
            BrewingRecipeRegistry.addRecipe(new FyshisBrewingRecipe(PotionRegistry.EXTEND_IN_NEGATIVE_POTION.get(), Items.REDSTONE, PotionRegistry.LONG_EXTEND_IN_NEGATIVE_POTION.get()));
            BrewingRecipeRegistry.addRecipe(new FyshisBrewingRecipe(PotionRegistry.EXTEND_IN_NEGATIVE_POTION.get(), Items.GLOWSTONE_DUST, PotionRegistry.STRONG_EXTEND_IN_NEGATIVE_POTION.get()));
            
            BrewingRecipeRegistry.addRecipe(new FyshisBrewingRecipe(PotionRegistry.EXTEND_IN_NEGATIVE_POTION.get(), Items.FERMENTED_SPIDER_EYE, PotionRegistry.SHORTEN_IN_NEGATIVE_POTION.get()));
            BrewingRecipeRegistry.addRecipe(new FyshisBrewingRecipe(PotionRegistry.SHORTEN_IN_NEGATIVE_POTION.get(), Items.REDSTONE, PotionRegistry.LONG_SHORTEN_IN_NEGATIVE_POTION.get()));
            BrewingRecipeRegistry.addRecipe(new FyshisBrewingRecipe(PotionRegistry.SHORTEN_IN_NEGATIVE_POTION.get(), Items.GLOWSTONE_DUST, PotionRegistry.STRONG_SHORTEN_IN_NEGATIVE_POTION.get()));
            
            BrewingRecipeRegistry.addRecipe(new FyshisBrewingRecipe(PotionRegistry.LONG_EXTEND_IN_POSITIVE_POTION.get(), Items.FERMENTED_SPIDER_EYE, PotionRegistry.LONG_SHORTEN_IN_POSITIVE_POTION.get()));
            BrewingRecipeRegistry.addRecipe(new FyshisBrewingRecipe(PotionRegistry.LONG_SHORTEN_IN_POSITIVE_POTION.get(), Items.FERMENTED_SPIDER_EYE, PotionRegistry.LONG_EXTEND_IN_NEGATIVE_POTION.get()));
            BrewingRecipeRegistry.addRecipe(new FyshisBrewingRecipe(PotionRegistry.LONG_EXTEND_IN_NEGATIVE_POTION.get(), Items.FERMENTED_SPIDER_EYE, PotionRegistry.LONG_SHORTEN_IN_NEGATIVE_POTION.get()));
            BrewingRecipeRegistry.addRecipe(new FyshisBrewingRecipe(PotionRegistry.STRONG_EXTEND_IN_POSITIVE_POTION.get(), Items.FERMENTED_SPIDER_EYE, PotionRegistry.STRONG_SHORTEN_IN_POSITIVE_POTION.get()));
            BrewingRecipeRegistry.addRecipe(new FyshisBrewingRecipe(PotionRegistry.STRONG_SHORTEN_IN_POSITIVE_POTION.get(), Items.FERMENTED_SPIDER_EYE, PotionRegistry.STRONG_EXTEND_IN_NEGATIVE_POTION.get()));
            BrewingRecipeRegistry.addRecipe(new FyshisBrewingRecipe(PotionRegistry.STRONG_EXTEND_IN_NEGATIVE_POTION.get(), Items.FERMENTED_SPIDER_EYE, PotionRegistry.STRONG_SHORTEN_IN_NEGATIVE_POTION.get()));
        });
    }
}
