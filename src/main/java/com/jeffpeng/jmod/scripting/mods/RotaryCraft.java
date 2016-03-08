package com.jeffpeng.jmod.scripting.mods;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.OwnedObject;
import com.jeffpeng.jmod.util.actions.rotarycraft.AddBlastFurnaceAlloyingDescriptor;
import com.jeffpeng.jmod.util.actions.rotarycraft.AddBlastFurnaceRecipeDescriptor;
import com.jeffpeng.jmod.util.actions.rotarycraft.AddGrinderRecipeDescriptor;

public class RotaryCraft extends OwnedObject {
	
	public RotaryCraft(JMODRepresentation owner){
		super(owner);
	}
	
	public void addGrinderRecipe(String in, String out){
		//SI.CONFIG.rotaryCraft.grinderRecipes.add(new GrinderRecipeDescriptor(in,out));
		if(owner.testForMod("RotaryCraft")) new AddGrinderRecipeDescriptor(owner,in,out);
	}
	
	public void addBlastFurnaceRecipe(String out, int temperature, int speed, float xp, Object recipe){
		if(owner.testForMod("RotaryCraft")) new AddBlastFurnaceRecipeDescriptor(owner,out,temperature,speed,xp,recipe);
	}
	
	public AddBlastFurnaceAlloyingDescriptor addBlastFurnaceAlloying(String out, String main, int temp){
		return new AddBlastFurnaceAlloyingDescriptor(owner,out,main,temp);
	}
	
	public void patchRotarycraftSteelTools(boolean value){
		config.patchRotarycraftSteelTools = value;
	}
	
}
