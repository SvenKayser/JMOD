package com.jeffpeng.jmod.actions.dcamt2;

import java.util.stream.Stream;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import mods.defeatedcrow.api.recipe.RecipeRegisterManager;
import net.minecraft.item.ItemStack;

public class AddIronPlateRecipe extends BasicAction {

	String inputStr;
	String outputStr;
	int cookingTime;
	boolean isOvenRecipe;
	
	public AddIronPlateRecipe(JMODRepresentation owner, String outputStr, String inputStr, int cookingTime, boolean isOvenRecipe) {
		super(owner);
		this.inputStr = inputStr;
		this.outputStr = outputStr;
		this.cookingTime = cookingTime;
		this.isOvenRecipe = isOvenRecipe;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event) {
		this.valid = false;
		
		Stream.of(lib.stringToMaybeItemStackNoOreDic(inputStr), 
				  lib.stringToMaybeItemStackNoOreDic(outputStr));
		
		lib.stringToMaybeItemStackNoOreDic(inputStr).ifPresent(input -> {
			lib.stringToMaybeItemStackNoOreDic(outputStr).ifPresent(output -> {
				if(this.recipeTests()) {
					addRecipe(input, output);
				}
			});
		});
		
		return this.valid;
	}

	private void addRecipe(ItemStack input, ItemStack output){
		RecipeRegisterManager.plateRecipe.register(input, output, this.cookingTime, this.isOvenRecipe);
		this.valid = true;
	}
	
	private boolean recipeTests() {
		return this.cookingTime > 0;
	}
}
