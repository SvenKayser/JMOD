package com.jeffpeng.jmod.scripting.mods;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.actions.rotarycraft.AddBlastFurnaceAlloying;
import com.jeffpeng.jmod.actions.rotarycraft.AddBlastFurnaceRecipe;
import com.jeffpeng.jmod.actions.rotarycraft.AddCentrifugeRecipe;
import com.jeffpeng.jmod.actions.rotarycraft.AddCompactorRecipe;
import com.jeffpeng.jmod.actions.rotarycraft.AddDryingBedRecipe;
import com.jeffpeng.jmod.actions.rotarycraft.AddGrinderRecipe;
import com.jeffpeng.jmod.actions.rotarycraft.AddPulseJetRecipe;
import com.jeffpeng.jmod.actions.rotarycraft.AddRockMelterRecipe;
import com.jeffpeng.jmod.actions.rotarycraft.AddWetterRecipe;
import com.jeffpeng.jmod.primitives.OwnedObject;

public class RotaryCraft extends OwnedObject {
	
	public RotaryCraft(JMODRepresentation owner){
		super(owner);
	}
	
	public void addGrinderRecipe(String out, String in){

		if(owner.testForMod("RotaryCraft")) new AddGrinderRecipe(owner,out,in);
	}
	
	public void addBlastFurnaceRecipe(String out, int temperature, int speed, float xp, Object recipe){
		if(owner.testForMod("RotaryCraft")) new AddBlastFurnaceRecipe(owner,out,temperature,speed,xp,recipe);
	}
	
	public AddBlastFurnaceAlloying addBlastFurnaceAlloying(String out, String main, int temp){
		if(owner.testForMod("RotaryCraft")) return new AddBlastFurnaceAlloying(owner,out,main,temp); 
		else return null;
	}
	
	public AddCentrifugeRecipe addCentrifugeRecipe(String inputitem){
		if(owner.testForMod("RotaryCraft"))	return new AddCentrifugeRecipe(owner, inputitem); 
		else return null;
	}
	
	public void addPulseJetRecipe(String out, String in){
		if(owner.testForMod("RotaryCraft")) new AddPulseJetRecipe(owner,out,in);
	}
	
	public void addCompactorRecipe(String out, String in, int temperature, int pressure){
		if(owner.testForMod("RotaryCraft")) new AddCompactorRecipe(owner,out,in,temperature, pressure);
	}
	
	public void addDryingBedRecipe(String out, String in){
		if(owner.testForMod("RotaryCraft")) new AddDryingBedRecipe(owner,out,in);
	}
	
	public void addRockMelterRecipe(String out, String in, int temperature, long power){
		if(owner.testForMod("RotaryCraft")) new AddRockMelterRecipe(owner,out,in,temperature,power);
	}
	
	public void addLiquefactionRecipe(String out, String in, String fluidin, int time){
		if(owner.testForMod("RotaryCraft")) new AddWetterRecipe(owner, out, in, fluidin, time);
	}
	
	public void patchRotarycraftSteelTools(boolean value){
		config.patchRotarycraftSteelTools = value;
	}
	
	
}
