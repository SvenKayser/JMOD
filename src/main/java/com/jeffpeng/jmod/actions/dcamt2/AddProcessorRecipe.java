package com.jeffpeng.jmod.actions.dcamt2;

import java.util.List;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.crafting.StringListRecipe;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import mods.defeatedcrow.api.recipe.RecipeRegisterManager;
import net.minecraft.item.ItemStack;

public class AddProcessorRecipe extends BasicAction {
	private String output;
	private String secondaryStr;
	private List<String[]> inputs;
	private boolean isFoodRecipe = false;
	private float secondaryChance = 1;
	private boolean forceReturnContainer = false;
	private int tier = 0;
	
	public AddProcessorRecipe(JMODRepresentation owner, String output, String secondaryOutput, List<String[]> inputs,
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
		
		StringListRecipe shapper = new StringListRecipe(owner, output, inputs);
		ItemStack secStack = lib.stringToItemStackOrFirstOreDict(secondaryStr);
		
		addRecipe(shapper.getRecipeOutput(), secStack, shapper.getIngredientArray());
	
		return valid;
	}
	
	private void addRecipe(ItemStack output, ItemStack secondary, Object[] input) {
		RecipeRegisterManager.processorRecipe.addRecipe(output, isFoodRecipe, tier, forceReturnContainer, secondary, secondaryChance, input);
		this.valid = true;
	}
}
