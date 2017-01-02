package com.jeffpeng.jmod.actions.dcamt2;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import defeatedcrow.addonforamt.jpaddon.api.RecipeManagerJP;
import net.minecraft.item.ItemStack;

public class RemoveDryingRecipe extends BasicAction {
	private String output;

	public RemoveDryingRecipe(JMODRepresentation owner, String output) {
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
		RecipeManagerJP.dryerRecipe.getRecipes().removeIf(recipe -> 
			recipe.getOutput().isItemEqual(itemStack)
		);
	}
}
