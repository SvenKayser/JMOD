package com.jeffpeng.jmod.actions.immersiveengineering;

import java.util.Optional;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import net.minecraft.item.ItemStack;

public class AddCrusherRecipe extends BasicAction {
	
	private String outputStack;
	private String inputStack;
	private int energy;
	
	private Optional<String> secondaryOutput;
	private float secondaryOutputChance = 0.0f;
	
	public AddCrusherRecipe(JMODRepresentation owner, String outputStack, String inputStack, int energy,
			String secondaryOutput, float secondaryOutputChance) {
		super(owner);
		this.outputStack = outputStack;
		this.inputStack = inputStack;
		this.energy = energy;
		this.secondaryOutput = Optional.of(secondaryOutput);
		this.secondaryOutputChance = secondaryOutputChance;
	}
	
	public AddCrusherRecipe(JMODRepresentation owner, String outputStack, String inputStack, int energy) {
		super(owner);
		this.outputStack = outputStack;
		this.inputStack = inputStack;
		this.energy = energy;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event){
		valid = false;
		Optional<ItemStack> inputOpt = lib.stringToMaybeItemStackNoOreDic(inputStack);
		Optional<ItemStack> outputOpt = lib.stringToMaybeItemStackNoOreDic(outputStack);
	
		inputOpt.ifPresent(inputIS -> {
			outputOpt.ifPresent(outputIS -> {
				valid = true;
				CrusherRecipe recipe = CrusherRecipe.addRecipe(outputIS, inputIS, energy);
								
				secondaryOutput.flatMap(lib::stringToMaybeItemStackNoOreDic)
							   .ifPresent( secIS -> {
								   recipe.addToSecondaryOutput(secIS, secondaryOutputChance);
								});
			});
		});
		
		return valid;
	}
	
}
