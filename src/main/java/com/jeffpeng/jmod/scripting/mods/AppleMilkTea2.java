package com.jeffpeng.jmod.scripting.mods;

import java.util.Optional;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;
import com.jeffpeng.jmod.actions.dcamt2.AddClayPanRecipe;
import com.jeffpeng.jmod.actions.dcamt2.AddHeatSource;
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

	public void addHeatSource(String block) {
		if(owner.testForMod("DCsAppleMilk"))
			new AddHeatSource(owner, block, Optional.empty());
	}
	
	public void addHeatSource(String block, int meta) {
		if(owner.testForMod("DCsAppleMilk"))
			new AddHeatSource(owner, block, Optional.of(meta));
	}
	
	public void addClayPanRecipe(String out, String input, String texture, String display) {
		if(owner.testForMod("DCsAppleMilk")) 
			new AddClayPanRecipe(owner, out, input, texture, display);
	}
	
	public void addIronPlateOvenRecipe(String output, String input, int cookingTime) {
		boolean isOvenRecipe = true;
		
		if(owner.testForMod("DCsAppleMilk")) 
			new AddIronPlateRecipe(owner, output, input, cookingTime, isOvenRecipe);
	}
	
	public void addIronPlateRecipe(String output, String input, int cookingTime) {
		boolean isOvenRecipe = false;
		
		if(owner.testForMod("DCsAppleMilk")) 
			new AddIronPlateRecipe(owner, output, input, cookingTime, isOvenRecipe);
	}
	
	public void addJawCrusherRecipe(String output, Object inputArray, int tier) {
		boolean isFoodRecipe = false;
		boolean forceReturnContainer = false;
		String secondaryOutput = null;
		float secondaryChance = 0F;
		
		if(owner.testForMod("DCsAppleMilk")) 
			new AddProcessorRecipe(owner, output, secondaryOutput, Lib.convertArray(inputArray, String[].class), isFoodRecipe, secondaryChance, forceReturnContainer, tier);
	}
	
	public void addJawCrusherRecipe(String output, String secondaryOutput, Object inputArray, float secondaryChance, int tier) {
		boolean isFoodRecipe = false;
		boolean forceReturnContainer = false;
		
		if(owner.testForMod("DCsAppleMilk")) 
			new AddProcessorRecipe(owner, output, secondaryOutput, Lib.convertArray(inputArray, String[].class), isFoodRecipe, secondaryChance, forceReturnContainer, tier);
	}
	
	public void addFoodProcessorRecipe(String output, Object inputArray, boolean forceReturnContainer) {
		String secondaryOutput = null;
		boolean isFoodRecipe = true;
		float secondaryChance = 1;
		int tier = 1;
		
		if(owner.testForMod("DCsAppleMilk")) 
			new AddProcessorRecipe(owner, output, secondaryOutput, Lib.convertArray(inputArray, String[].class), isFoodRecipe, secondaryChance, forceReturnContainer, tier);
	}
	
	public void removeIronPlateRecipe(String output) {
		if(owner.testForMod("DCsAppleMilk")) new RemoveIronPlateRecipe(owner, output);
	}
	
	public void removeJawCrusherRecipe(String output) {
		boolean isFoodRecipe = false;
		if(owner.testForMod("DCsAppleMilk")) 
			new RemoveProcessorRecipe(owner, output, isFoodRecipe);
	}
	
	public void removeFoodProcessorRecipe(String output) {
		boolean isFoodRecipe = true;
		if(owner.testForMod("DCsAppleMilk")) 
			new RemoveProcessorRecipe(owner, output, isFoodRecipe);
	}
	
	public void removeDryingRecipe(String output) {
		if(owner.testForMod("AMTAddonJP")) new RemoveDryingRecipe(owner, output);
	}
}
