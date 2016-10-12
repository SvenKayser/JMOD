package com.jeffpeng.jmod.scripting.mods;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.actions.rotarycraft.AddBlastFurnaceAlloying;
import com.jeffpeng.jmod.actions.rotarycraft.AddBlastFurnaceRecipe;
import com.jeffpeng.jmod.actions.rotarycraft.AddCentrifugeRecipe;
import com.jeffpeng.jmod.actions.rotarycraft.AddGrinderRecipe;
import com.jeffpeng.jmod.primitives.OwnedObject;

public class RotaryCraft extends OwnedObject {
	
	public RotaryCraft(JMODRepresentation owner){
		super(owner);
	}
	
	public void addGrinderRecipe(String out, String in){
		//SI.CONFIG.rotaryCraft.grinderRecipes.add(new GrinderRecipeDescriptor(in,out));
		if(owner.testForMod("RotaryCraft")) new AddGrinderRecipe(owner,out,in);
	}
	
	public void addBlastFurnaceRecipe(String out, int temperature, int speed, float xp, Object recipe){
		if(owner.testForMod("RotaryCraft")) new AddBlastFurnaceRecipe(owner,out,temperature,speed,xp,recipe);
	}
	
	public AddBlastFurnaceAlloying addBlastFurnaceAlloying(String out, String main, int temp){
		return new AddBlastFurnaceAlloying(owner,out,main,temp);
	}
	
	public AddCentrifugeRecipe addCentrifugeRecipe(String inputitem){
		return new AddCentrifugeRecipe(owner, inputitem);
	}
	
	public void patchRotarycraftSteelTools(boolean value){
		config.patchRotarycraftSteelTools = value;
	}
	
}
