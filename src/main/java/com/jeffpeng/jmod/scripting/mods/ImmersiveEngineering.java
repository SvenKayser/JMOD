package com.jeffpeng.jmod.scripting.mods;

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
}
