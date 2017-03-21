package com.jeffpeng.jmod.actions.rotarycraft;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import Reika.RotaryCraft.API.RecipeInterface;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;

public class AddRockMelterRecipe extends BasicAction {
	private String inString;
	private String outString;
	private int temperature = 0;
	private long energy = 1;
	
	private ItemStack in;
	private FluidStack out;

	public AddRockMelterRecipe(JMODRepresentation owner, String out, String in, int temperature, long energy){
		super(owner);
		this.inString = in;
		this.outString = out;
		this.temperature = temperature;
		this.energy = energy;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event){
		valid = false;
		Object inIs = lib.stringToItemStack(inString);
		
		if(inIs instanceof ItemStack){
			Object outFs = lib.stringToFluidStack(outString);
			
			if(outFs instanceof FluidStack){
				valid = true;
				in = (ItemStack)inIs;
				out = (FluidStack)outFs;
			}
		}
		if(valid) RecipeInterface.rockmelt.addAPIRecipe(in, out, temperature, energy);
		return valid;
	}
}
