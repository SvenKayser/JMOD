package com.jeffpeng.jmod.actions.immersiveengineering;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import blusunrize.immersiveengineering.api.crafting.MetalPressRecipe;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;

public class RemoveMetalPressRecipe extends BasicAction {
	private String outputItemName;

	public RemoveMetalPressRecipe(JMODRepresentation owner, String outputItemName) {
		super(owner);
		this.outputItemName = outputItemName;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event){
		valid = false;
		lib.stringToMaybeItemStackNoOreDic(outputItemName)
		   .ifPresent(outputIS -> {
			valid = true;
			MetalPressRecipe.removeRecipes(outputIS);
		});
		return valid;
	}
}
