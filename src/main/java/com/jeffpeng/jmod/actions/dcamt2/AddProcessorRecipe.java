package com.jeffpeng.jmod.actions.dcamt2;

import java.util.Optional;
import java.util.stream.Stream;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import mods.defeatedcrow.api.recipe.RecipeRegisterManager;
import net.minecraft.item.ItemStack;

public class AddProcessorRecipe extends BasicAction {
	private String output;
	private String secondaryStr;
	private String [] inputs;
	private boolean isFoodRecipe = false;
	private float secondaryChance = 1;
	private boolean forceReturnContainer = false;
	private int tier = 0;
	
	public AddProcessorRecipe(JMODRepresentation owner, String output, String secondaryOutput, String[] inputs,
			boolean isFoodRecipe, float secondaryChance, boolean forceReturnContainer, int tier) {
		super(owner);
		this.output = output;
		this.secondaryStr = secondaryOutput;
		this.inputs = inputs;
		this.isFoodRecipe = isFoodRecipe;
		this.secondaryChance = secondaryChance;
		this.forceReturnContainer = forceReturnContainer;
		this.tier = tier;
	}
	

	@Override
	public boolean on(FMLLoadCompleteEvent event) {
		this.valid = false;
		
		Object[] inputObjs = Stream.of(this.inputs)
								   .map(lib::stringToMaybeItemStack)
								   .filter(Optional::isPresent)
								   .map(Optional::get)
								   .toArray();
		
		ItemStack secStack = lib.stringToItemStackOrFirstOreDict(secondaryStr);
		ItemStack outputStack = lib.stringToItemStackOrFirstOreDict(output);
		
		addRecipe(outputStack, secStack, inputObjs);
	
		return valid;
	}
	
	private void addRecipe(ItemStack output, ItemStack secondary, Object[] input) {
		RecipeRegisterManager.processorRecipe.addRecipe(output, isFoodRecipe, tier, forceReturnContainer, secondary, secondaryChance, input);
		this.valid = true;
	}
}
