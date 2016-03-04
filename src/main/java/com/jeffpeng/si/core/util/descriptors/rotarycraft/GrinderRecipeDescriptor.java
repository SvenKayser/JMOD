package com.jeffpeng.si.core.util.descriptors.rotarycraft;

import net.minecraft.item.ItemStack;
import Reika.RotaryCraft.API.RecipeInterface;

import com.jeffpeng.si.core.SILib;
import com.jeffpeng.si.core.interfaces.ISIExecutableObject;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;

public class GrinderRecipeDescriptor implements ISIExecutableObject {
	
	

	private boolean valid;
	
	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private String inString;
	private String outString;
	
	private ItemStack in;
	private ItemStack out;
	
	public GrinderRecipeDescriptor(String in, String out){
		this.inString = in;
		this.outString = out;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event){
		valid = false;
		Object inIs = SILib.stringToItemStack(inString);
		
		if(inIs instanceof ItemStack){
			Object outIs = SILib.stringToItemStack(outString);
			
			if(outIs instanceof ItemStack){
				valid = true;
				in = (ItemStack)inIs;
				out = (ItemStack)outIs;
			}
		}
		return valid;
	}
	
	@Override
	public void execute() {
		RecipeInterface.grinder.addAPIRecipe(in, out);
		
		
	}
}
