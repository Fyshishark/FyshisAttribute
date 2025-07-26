package com.fyshishark.fyshisattribute.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public class FyshisBrewingRecipe implements IBrewingRecipe {
    private final Potion input;
    private final Item ingredient;
    private final Potion output;
    
    public FyshisBrewingRecipe(Potion pInput, Item pIngredient, Potion pOutput) {
        input = pInput;
        ingredient = pIngredient;
        output = pOutput;
    }
    
    @Override
    public ItemStack getOutput(ItemStack pInput, ItemStack pIngredient) {
        if(!isInput(pInput) || !isIngredient(pIngredient)) return ItemStack.EMPTY;
        
        ItemStack item = new ItemStack(pInput.getItem());
        item.setTag(new CompoundTag());
        PotionUtils.setPotion(item, output);
        return item;
    }
    
    @Override public boolean isInput(ItemStack pInput) { return PotionUtils.getPotion(pInput) == input; }
    @Override public boolean isIngredient(ItemStack pIngredient) { return pIngredient.getItem() == ingredient; }
}
