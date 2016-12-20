package com.jeffpeng.jmod.actions.dcamt2;

import java.util.Optional;

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
		Optional<ItemStack> outItem = Optional.ofNullable(lib.stringToItemStackNoOreDict(outputStr));
		Optional<ItemStack> inputItem = Optional.ofNullable(lib.stringToItemStackNoOreDict(inputStr));
		boolean isValid = false;
		
		if ( outItem.isPresent() && inputItem.isPresent() && this.cookingTime > 0) {
			isValid = true;
			
			outItem.ifPresent(output -> inputItem.ifPresent(input -> {
				RecipeRegisterManager.plateRecipe.register(input, output, this.cookingTime, this.isOvenRecipe);
			}));
		}
		
		return isValid;
	}
	
}
