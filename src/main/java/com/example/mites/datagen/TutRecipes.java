package com.example.mites.datagen;

import com.example.mites.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

public class TutRecipes extends RecipeProvider {
    public TutRecipes(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer){
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Registration.BURROW_ITEM),
                        Registration.MITE.get(), 2.0f, 50)
                .unlockedBy("has_ore", has(Registration.BURROW_ITEM))
                .save(consumer, "mite_smelt");

    }
}
