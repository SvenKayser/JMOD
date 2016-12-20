package com.jeffpeng.jmod.actions.dcamt2;

import java.util.Optional;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import mods.defeatedcrow.api.recipe.RecipeRegisterManager;
import net.minecraft.item.ItemStack;

public class RemoveIronPlateRecipe extends BasicAction {

	String inputStr;

	public RemoveIronPlateRecipe(JMODRepresentation owner, String inputStr) {
		super(owner);
		this.inputStr = inputStr;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event) {
		Optional<ItemStack> inputItem = Optional.ofNullable(lib.stringToItemStackNoOreDict(inputStr));
		boolean isValid = false;

		if ( inputItem.isPresent() ) {
			isValid = true;
			
			inputItem.flatMap(item -> Optional.ofNullable(RecipeRegisterManager.plateRecipe.getRecipe(item)))
					 .ifPresent(recipe -> {
						 RecipeRegisterManager.plateRecipe.getRecipeList().remove(recipe);
					 });
		}
		
		return isValid;
	}
}
