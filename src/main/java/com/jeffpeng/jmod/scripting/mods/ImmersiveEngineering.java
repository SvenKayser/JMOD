package com.jeffpeng.jmod.scripting.mods;

import java.util.Optional;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.actions.immersiveengineering.*;
import com.jeffpeng.jmod.primitives.OwnedObject;

public class ImmersiveEngineering extends OwnedObject {

	public ImmersiveEngineering(JMODRepresentation owner) {
		super(owner);
	}
	
	public void addCrusherRecipe(String outputStack, String inputStack, int energy,
			String secondaryOutput, float secondaryOutputChance){
		if(owner.testForMod("ImmersiveEngineering")) new AddCrusherRecipe(owner, outputStack, inputStack, energy, secondaryOutput, secondaryOutputChance);
	}

	public void addCrusherRecipe(String outputStack, String inputStack, int energy){
		if(owner.testForMod("ImmersiveEngineering")) new AddCrusherRecipe(owner, outputStack, inputStack, energy);
	}
	
	public void removeCrusherRecipe(String outputItemName){
		if(owner.testForMod("ImmersiveEngineering")) new RemoveCrusherRecipe(owner, outputItemName);
	}
	
	public void addMetalPressRecipe(String output, String input, int energy, String mold) {
		if(owner.testForMod("ImmersiveEngineering")) new AddMetalPressRecipe(owner, output, input, energy, mold);
	}
	
	public void removeMetalPressRecipe(String outputItemName){
		if(owner.testForMod("ImmersiveEngineering")) new RemoveMetalPressRecipe(owner, outputItemName);
	}
	
	public void addArcFurnaceRecipe(String output, String input, int time, int energyPerTick) {
		if(owner.testForMod("ImmersiveEngineering")) 
			new AddArcFurnaceRecipe(owner, output, input, time, energyPerTick, Optional.empty(), null);
	}
	
	public void addArcFurnaceRecipe(String output, String input, int time, int energyPerTick, String [] additives) {
		if(owner.testForMod("ImmersiveEngineering")) 
			new AddArcFurnaceRecipe(owner, output, input, time, energyPerTick, Optional.empty(), additives);
	}
	
	public void addArcFurnaceRecipe(String output, String input, int time, int energyPerTick, String slag, String [] additives) {
		if(owner.testForMod("ImmersiveEngineering")) 
			new AddArcFurnaceRecipe(owner, output, input, time, energyPerTick, Optional.of(slag), additives);
	}
	
	public void removeArcFurnaceRecipe(String outputItemName){
		if(owner.testForMod("ImmersiveEngineering")) new RemoveArcFurnaceRecipe(owner, outputItemName);
	}
}
