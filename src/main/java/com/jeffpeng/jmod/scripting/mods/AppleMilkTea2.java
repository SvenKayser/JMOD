package com.jeffpeng.jmod.scripting.mods;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.actions.dcamt2.AddClayPanRecipe;
import com.jeffpeng.jmod.actions.dcamt2.AddIronPlateRecipe;
import com.jeffpeng.jmod.actions.dcamt2.AddProcessorRecipe;
import com.jeffpeng.jmod.actions.dcamt2.RemoveDryingRecipe;
import com.jeffpeng.jmod.actions.dcamt2.RemoveIronPlateRecipe;
import com.jeffpeng.jmod.actions.dcamt2.RemoveProcessorRecipe;
import com.jeffpeng.jmod.primitives.OwnedObject;

public class AppleMilkTea2 extends OwnedObject {

	public AppleMilkTea2(JMODRepresentation owner) {
		super(owner);
	}

	public void addClayPanRecipe(String out, String input, String texture, String display) {
		if(owner.testForMod("DCsAppleMilk")) new AddClayPanRecipe(owner, out, input, texture, display);
	}
	
	public void addClayPanRecipe(String out, String outJpBowl, String input, String texture, String display) {
		if(owner.testForMod("DCsAppleMilk")) new AddClayPanRecipe(owner, outJpBowl, input, texture, display);
	}
	
	public void addIronPlateRecipe(String output, String input, int cookingTime, boolean isOvenRecipe) {
		if(owner.testForMod("DCsAppleMilk")) new AddIronPlateRecipe(owner, output, input, cookingTime, isOvenRecipe);
	}
	
	public void addProcessorRecipe(String output, String secondaryOutput, String inputs,
			boolean isFoodRecipe, float secondaryChance, boolean forceReturnContainer, int tier) {
		if(owner.testForMod("DCsAppleMilk")) new AddProcessorRecipe(owner, output, secondaryOutput, inputs, isFoodRecipe, secondaryChance, forceReturnContainer, tier);
	}
	
	public void addProcessorRecipe(String output, String secondaryOutput, String inputs, float secondaryChance, int tier) {
		boolean isFoodRecipe = false;
		boolean forceReturnContainer = false;
		this.addProcessorRecipe(output, secondaryOutput, inputs, isFoodRecipe, secondaryChance, forceReturnContainer, tier);
	}
	
	public void addFoodProcessorRecipe(String output, String inputs, boolean forceReturnContainer) {
		String secondaryOutput = null;
		boolean isFoodRecipe = true;
		float secondaryChance = 1;
		int tier = 1;
		
		this.addProcessorRecipe(output, secondaryOutput, inputs, isFoodRecipe, secondaryChance, forceReturnContainer, tier);
	}
	
	public void removeIronPlateRecipe(String output) {
		if(owner.testForMod("DCsAppleMilk")) new RemoveIronPlateRecipe(owner, output);
	}
	
	public void removeProcessorRecipe(String output, boolean isFoodRecipe) {
		if(owner.testForMod("DCsAppleMilk")) new RemoveProcessorRecipe(owner, output, isFoodRecipe);
	}
	
	public void removeDryingRecipe(String output) {
		if(owner.testForMod("AMTAddonJP")) new RemoveDryingRecipe(owner, output);
	}
}
