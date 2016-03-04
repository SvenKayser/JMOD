package com.jeffpeng.si.core.util.descriptors.rotarycraft;

import net.minecraft.item.ItemStack;
import Reika.RotaryCraft.API.RecipeInterface;

import com.jeffpeng.si.core.SILib;
import com.jeffpeng.si.core.crafting.StringListRecipe;
import com.jeffpeng.si.core.interfaces.ISIExecutableObject;
import com.jeffpeng.si.core.interfaces.ISIStagedObject;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;

public class BlastFurnaceRecipeDescriptor implements ISIStagedObject {
	
	private String out;
	private ItemStack outStack;
	private int temperature;
	private int speed;
	private float xp;
	private Object recipe;
	private boolean valid;
	
	public BlastFurnaceRecipeDescriptor(String out, int temperature, int speed, float xp, Object recipe){
		this.out = out;
		this.temperature = temperature;
		this.speed = speed;
		this.xp = xp;
		this.recipe = recipe;
	}

	@Override
	public boolean isValid() {
		return valid;
	}
	
	@Override
	public boolean on(FMLPostInitializationEvent event){
		Object is = SILib.stringToItemStack(out);
		if(is instanceof ItemStack){
			outStack = (ItemStack)is;
			valid = true;
			RecipeInterface.blastfurn.addAPIRecipe(outStack, temperature, new StringListRecipe(out,SILib.convertPattern(recipe)), speed, xp);
		} else {
			valid = false;
		}
		
		return valid;
	}

}
