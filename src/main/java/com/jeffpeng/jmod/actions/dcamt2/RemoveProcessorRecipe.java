package com.jeffpeng.jmod.actions.dcamt2;

import java.util.Optional;
import java.util.function.Predicate;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import mods.defeatedcrow.api.recipe.IProcessorRecipe;
import mods.defeatedcrow.api.recipe.RecipeRegisterManager;
import net.minecraft.item.ItemStack;

public class RemoveProcessorRecipe extends BasicAction {

	private String output;
	private boolean isFoodRecipe = false;
	private Optional<ItemStack> outputItem = Optional.empty();
	
	public RemoveProcessorRecipe(JMODRepresentation owner, String output, boolean isFoodRecipe) {
		super(owner);
		this.output = output;
		this.isFoodRecipe = isFoodRecipe;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event) {
		outputItem = Optional.ofNullable(lib.stringToItemStackNoOreDict(output));
		boolean isValid = false;
		
		if ( outputItem.isPresent() ) {
			isValid = outputItem.filter(output -> 
				RecipeRegisterManager.processorRecipe.getRecipes().removeIf(isMatch)
			).isPresent();
		}
		return isValid;
	}
	
	private Predicate<IProcessorRecipe> isMatch = recipe -> {
		return outputItem.filter(lookupItem -> recipe.getOutput().isItemEqual(lookupItem))
				  		 .filter(item -> recipe.isFoodRecipe() == this.isFoodRecipe)
				  		 .isPresent();
	};
}
