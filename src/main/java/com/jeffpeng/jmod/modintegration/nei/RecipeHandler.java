package com.jeffpeng.jmod.modintegration.nei;

import net.minecraft.item.ItemStack;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.recipe.ShapedRecipeHandler;
import com.jeffpeng.jmod.crafting.StringListRecipe;

public class RecipeHandler extends ShapedRecipeHandler {
	
	@Override
	public void loadCraftingRecipes(ItemStack result) {
        for (StringListRecipe slrecipe : StringListRecipe.recipeList) {
            if (NEIServerUtils.areStacksSameTypeCrafting(slrecipe.getRecipeOutput(), result)) {
                CachedShapedRecipe recipe = new CachedShapedRecipe(slrecipe.getWidth(), slrecipe.getHeight(), slrecipe.getIngredientArray(), slrecipe.getRecipeOutput());
                recipe.computeVisuals();
                arecipes.add(recipe);
            }
        }
    }
	
	 @Override
	 public void loadUsageRecipes(ItemStack ingredient) {
		 for (StringListRecipe slrecipe : StringListRecipe.recipeList) {
			 if (slrecipe.containsIngredient(ingredient)) {
				 CachedShapedRecipe recipe = new CachedShapedRecipe(slrecipe.getWidth(), slrecipe.getHeight(), slrecipe.getIngredientArray(), slrecipe.getRecipeOutput());
	             recipe.computeVisuals();
	             arecipes.add(recipe);
	         }
		 }
	 }
}
