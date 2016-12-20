package com.jeffpeng.jmod.actions.dcamt2;

import java.util.Optional;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import mods.defeatedcrow.api.recipe.RecipeRegisterManager;
import net.minecraft.item.ItemStack;

public class AddProcessorRecipe extends BasicAction {
	private String outStr;
	private String secondaryStr;
	private String inputs;
	private boolean isFoodRecipe = false;
	private float secondaryChance = 1;
	private boolean forceReturnContainer = false;
	private int tier = 0;
	
	public AddProcessorRecipe(JMODRepresentation owner, String output, String secondaryOutput, String inputs,
			boolean isFoodRecipe, float secondaryChance, boolean forceReturnContainer, int tier) {
		super(owner);
		this.outStr = output;
		this.secondaryStr = secondaryOutput;
		this.inputs = inputs;
		this.isFoodRecipe = isFoodRecipe;
		this.secondaryChance = secondaryChance;
		this.forceReturnContainer = forceReturnContainer;
		this.tier = tier;
	}
	

	@Override
	public boolean on(FMLLoadCompleteEvent event) {
		Optional<ItemStack> outputItem = Optional.ofNullable(lib.stringToItemStackNoOreDict(outStr));
		Optional<ItemStack> secondaryItem = Optional.ofNullable(lib.stringToItemStackNoOreDict(secondaryStr));
		Optional<Object[]> inputItems = Optional.ofNullable(lib.stringToItemStackArray(inputs));
		boolean isValid = false;
		
		if ( outputItem.isPresent() && inputItems.isPresent()) {
			isValid = true;
			outputItem.ifPresent(output -> inputItems.ifPresent(input -> {
				if (secondaryItem.isPresent()) {
					secondaryItem.ifPresent(secondary -> {
						RecipeRegisterManager.processorRecipe.addRecipe(output, isFoodRecipe, tier, forceReturnContainer, secondary, secondaryChance, input);
					});
				} else {
					RecipeRegisterManager.processorRecipe.addRecipe(output, isFoodRecipe, tier, forceReturnContainer, null, secondaryChance, input);
				}
			}));
		}
		
		return isValid;
	}
}
