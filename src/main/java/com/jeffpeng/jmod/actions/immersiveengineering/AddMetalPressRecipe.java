package com.jeffpeng.jmod.actions.immersiveengineering;

import java.util.Optional;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import blusunrize.immersiveengineering.api.crafting.MetalPressRecipe;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import net.minecraft.item.ItemStack;

public class AddMetalPressRecipe extends BasicAction {
	
	private String outputStack;
	private String inputStack;
	private int energy;
	private String mold;
	
	/**
	 * 
	 * @param owner
	 * @param outputStack, 
	 * @param inputStack, input item for press, 
	 * 		the size of this stack, the number of input items used in each operation of the press.
	 * @param energy, cost for pressing is 2400 for gears, plates, and rods.
	 * @param mold, this must be a mold Item as defined in IE
	 */
	
	public AddMetalPressRecipe(JMODRepresentation owner, String outputStack, String inputStack, int energy,
			String mold) {
		super(owner);
		this.outputStack = outputStack;
		this.inputStack = inputStack;
		this.energy = energy;
		this.mold = mold;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event){
		valid = false;
		Optional<ItemStack> outputOpt = lib.stringToMaybeItemStackNoOreDic(outputStack);
		Optional<ItemStack> inputOpt  = lib.stringToMaybeItemStackNoOreDic(inputStack);
		Optional<ItemStack> moldOpt = lib.stringToMaybeItemStackNoOreDic(mold);
		
		outputOpt.ifPresent(output -> {
			inputOpt.ifPresent(input -> {
				moldOpt.ifPresent(m -> {
					MetalPressRecipe.addRecipe(output, input, m, energy);
					valid = true;
				});
			});
		});
					
		return valid;
	}
}
