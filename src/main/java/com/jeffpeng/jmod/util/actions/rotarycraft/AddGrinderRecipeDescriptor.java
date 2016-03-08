package com.jeffpeng.jmod.util.actions.rotarycraft;

import net.minecraft.item.ItemStack;
import Reika.RotaryCraft.API.RecipeInterface;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;

public class AddGrinderRecipeDescriptor extends BasicAction {
	
	private String inString;
	private String outString;
	
	private ItemStack in;
	private ItemStack out;
	
	public AddGrinderRecipeDescriptor(JMODRepresentation owner, String in, String out){
		super(owner);
		this.inString = in;
		this.outString = out;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event){
		valid = false;
		Object inIs = lib.stringToItemStack(inString);
		
		if(inIs instanceof ItemStack){
			Object outIs = lib.stringToItemStack(outString);
			
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
