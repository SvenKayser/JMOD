package com.jeffpeng.jmod.actions.rotarycraft;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import Reika.RotaryCraft.API.RecipeInterface;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;

public class AddWetterRecipe extends BasicAction {
	private String inString;
	private String finString;
	private String outString;
	private ItemStack in;
	private ItemStack out;
	private Fluid fluid;
	private int amount;
	private int time;

	public AddWetterRecipe(JMODRepresentation owner, String out, String in, String fin, int time){
		super(owner);
		this.inString = in;
		this.outString = out;
		this.finString = fin;
		this.time = time;
		
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event){
		valid = false;
		
		Object outIs = lib.stringToItemStack(outString);
		
		if(outIs instanceof ItemStack){
			Object inIs = lib.stringToItemStack(inString);
			if(inIs instanceof ItemStack){
				Object inFs = lib.stringToFluidStack(finString);
				if(inFs instanceof FluidStack){
					valid = true;
					fluid = ((FluidStack)inFs).getFluid();
					amount = ((FluidStack)inFs).amount;
					in = (ItemStack)inIs;
					out = (ItemStack)outIs;
				} 
			} 
		} 

		if(valid) RecipeInterface.wetter.addAPIRecipe(in, fluid, amount, out, time);
		return valid;
	}
}
