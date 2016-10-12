package com.jeffpeng.jmod.actions;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;
import com.jeffpeng.jmod.crafting.BlacklistCraftingResults;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class AddShapelessRecipe extends BasicAction {

	public String resultString;
	public String[] ingredientsStrings;
	
	public AddShapelessRecipe(JMODRepresentation owner, String resultString, String[] ingredientsStrings){
		super(owner);
		this.resultString = resultString;
		this.ingredientsStrings = ingredientsStrings;
		this.valid = true;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event){
		if(BlacklistCraftingResults.match(resultString,log))	return this.valid = false;
		
		return valid;

	}
	
	@Override
	public void execute(){
		
		
		
		List<Object> ingredients = new ArrayList<Object>();

		for (String ingrediententry : ingredientsStrings) {
			int amount = Lib.itemStackSizeFromString(ingrediententry);
			
			Object ingredient = lib.stringToItemStack(ingrediententry);
			
			if(ingredient instanceof ItemStack){
				ingredients.add(ingredient);
			} else {
				for (int c = 0; c < amount; c++) {
					ingredients.add(ingredient);
				}
			}
			
			
		}

		Object result = lib.stringToItemStack(resultString);
		if (!(result instanceof ItemStack)){
			log.warn("Cannot resolve "  + resultString + " to an item. Hint: OreDict entries are not valid as craftig results.");
			return;
		}
		ItemStack resultstack = ((ItemStack) result);
		GameRegistry.addRecipe(new ShapelessOreRecipe(resultstack, ingredients.toArray()));
	}
	
	@Override
	public int priority(){
		return 10;
	}
}
