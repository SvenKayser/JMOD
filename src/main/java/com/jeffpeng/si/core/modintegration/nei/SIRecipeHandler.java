package com.jeffpeng.si.core.modintegration.nei;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.recipe.ShapedRecipeHandler;
import codechicken.nei.recipe.ShapedRecipeHandler.CachedShapedRecipe;

import com.jeffpeng.si.core.crafting.StringListRecipe;

public class SIRecipeHandler extends ShapedRecipeHandler {
	
	public void loadCraftingRecipes(ItemStack result) {
        for (StringListRecipe slrecipe : StringListRecipe.recipeList) {
            if (NEIServerUtils.areStacksSameTypeCrafting(slrecipe.getRecipeOutput(), result)) {
                CachedShapedRecipe recipe = new CachedShapedRecipe(slrecipe.getWidth(), slrecipe.getHeight(), slrecipe.getIngredientArray(), slrecipe.getRecipeOutput());
                recipe.computeVisuals();
                arecipes.add(recipe);
            }
        }
    }
}
