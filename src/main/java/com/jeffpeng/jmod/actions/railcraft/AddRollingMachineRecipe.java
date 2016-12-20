package com.jeffpeng.jmod.actions.railcraft;

import java.util.List;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.crafting.StringListRecipe;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import mods.railcraft.api.crafting.RailcraftCraftingManager;

public class AddRollingMachineRecipe extends BasicAction {

	private String outputItemStr;
	private List<String[]> shape;
	
	public AddRollingMachineRecipe(JMODRepresentation owner, String outputItemStr, List<String[]> shape) {
		super(owner);
		this.outputItemStr = outputItemStr;
		this.shape= shape;
	}

	@Override
	public boolean on(FMLPostInitializationEvent event){
		StringListRecipe recipe = new StringListRecipe(owner, outputItemStr,shape);
		valid = recipe.isValid();
		
		if(valid) {
			RailcraftCraftingManager.rollingMachine
									.addRecipe(recipe.getRecipeOutput(), 
											   recipe.getIngredientArray());
		}
		
		return valid;
	}
}
