package com.jeffpeng.si.core.scripting.mods;

import com.jeffpeng.si.core.SILib;
import com.jeffpeng.si.core.util.descriptors.rotarycraft.BlastFurnaceAlloyingDescriptor;
import com.jeffpeng.si.core.util.descriptors.rotarycraft.BlastFurnaceRecipeDescriptor;
import com.jeffpeng.si.core.util.descriptors.rotarycraft.GrinderRecipeDescriptor;

public class RotaryCraft {
	public void addGrinderRecipe(String in, String out){
		//SI.CONFIG.rotaryCraft.grinderRecipes.add(new GrinderRecipeDescriptor(in,out));
		new GrinderRecipeDescriptor(in,out);
	}
	
	public void addBlastFurnaceRecipe(String out, int temperature, int speed, float xp, Object recipe){
		new BlastFurnaceRecipeDescriptor(out,temperature,speed,xp,SILib.convertPattern(recipe));
	}
	
	public BlastFurnaceAlloyingDescriptor addBlastFurnaceAlloying(String out, String main, int temp){
		return new BlastFurnaceAlloyingDescriptor(out,main,temp);
	}
	
}
