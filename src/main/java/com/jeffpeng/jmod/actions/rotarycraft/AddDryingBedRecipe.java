package com.jeffpeng.jmod.actions.rotarycraft;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import Reika.RotaryCraft.API.RecipeInterface;

import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;

public class AddDryingBedRecipe extends BasicAction {
	private String inString;
	private String outString;
	
	private Fluid in;
	private int amount;
	private ItemStack out;

	public AddDryingBedRecipe(JMODRepresentation owner, String out, String in){
		super(owner);
		this.inString = in;
		this.outString = out;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event){
		valid = false;
		Object inFs = lib.stringToFluidStack(inString);
		
		if(inFs instanceof FluidStack){
			Object outIs = lib.stringToItemStack(outString);
			
			if(outIs instanceof ItemStack){
				valid = true;
				in = ((FluidStack)inFs).getFluid();
				amount = ((FluidStack)inFs).amount;
				out = (ItemStack)outIs;
			} 
		} 
		if(valid) RecipeInterface.dryingbed.addAPIRecipe(in, amount, out);
		return valid;
	}
}
