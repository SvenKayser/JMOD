package com.jeffpeng.jmod.scripting.mods;

import java.util.List;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.actions.railcraft.AddRollingMachineRecipe;
import com.jeffpeng.jmod.actions.railcraft.RemoveRollingMachineRecipe;
import com.jeffpeng.jmod.primitives.OwnedObject;

public class RailCraft extends OwnedObject {

	public RailCraft(JMODRepresentation owner) {
		super(owner);
	}
	
	public void addRollingMachineRecipe(String outputItemStr, List<String[]> shape) {
		if(owner.testForMod("Railcraft")) new AddRollingMachineRecipe(owner, outputItemStr, shape);
	}
	
	public void removeRollingMachineRecipe(String outputItemStr) {
		if(owner.testForMod("Railcraft")) new RemoveRollingMachineRecipe(owner, outputItemStr);
	}

}
