package com.jeffpeng.jmod.actions.dcamt2;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import mods.defeatedcrow.api.recipe.RecipeRegisterManager;
import net.minecraft.item.ItemStack;

public class RemoveProcessorRecipe extends BasicAction {

	private String output;
	private boolean isFoodRecipe = false;
	
	public RemoveProcessorRecipe(JMODRepresentation owner, String output, boolean isFoodRecipe) {
		super(owner);
		this.output = output;
		this.isFoodRecipe = isFoodRecipe;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event) {
		valid= false;
	
		lib.stringToMaybeItemStackNoOreDic(output).ifPresent(this::removeRecipe);
			
		return valid;
	}
	
	private void removeRecipe(ItemStack itemStack) {
		RecipeRegisterManager.processorRecipe.getRecipes().removeIf(recipe -> {
			return (recipe.isFoodRecipe() == this.isFoodRecipe) && 
					recipe.getOutput().isItemEqual(itemStack);
		});
	}
}
