package com.jeffpeng.jmod.actions.dcamt2;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import mods.defeatedcrow.api.recipe.RecipeRegisterManager;
import net.minecraft.item.ItemStack;

public class RemoveIronPlateRecipe extends BasicAction {

	String output;

	public RemoveIronPlateRecipe(JMODRepresentation owner, String output) {
		super(owner);
		this.output = output;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event) {
		valid= false;
	
		lib.stringToMaybeItemStackNoOreDic(output).ifPresent(this::removeRecipe);
			
		return valid;
	}
	
	private void removeRecipe(ItemStack itemStack) {
		this.valid = RecipeRegisterManager.plateRecipe
										  .getRecipeList()
										  .removeIf( recipe -> 
										  	recipe.getOutput().isItemEqual(itemStack)
										  );
	}
}
