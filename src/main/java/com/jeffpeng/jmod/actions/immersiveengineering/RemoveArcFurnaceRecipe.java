package com.jeffpeng.jmod.actions.immersiveengineering;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;

public class RemoveArcFurnaceRecipe extends BasicAction {
	private String outputStr;

	public RemoveArcFurnaceRecipe(JMODRepresentation owner, String outputStr) {
		super(owner);
		this.outputStr = outputStr;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event){
		valid = false;
		lib.stringToMaybeItemStack(outputStr)
		   .ifPresent(outputIS -> {
			valid = true;
			ArcFurnaceRecipe.removeRecipes(outputIS);
		});
		return valid;
	}
}
