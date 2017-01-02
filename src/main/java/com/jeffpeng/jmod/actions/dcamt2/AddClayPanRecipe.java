package com.jeffpeng.jmod.actions.dcamt2;

import java.util.stream.Stream;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import mods.defeatedcrow.api.recipe.RecipeRegisterManager;
import net.minecraft.item.ItemStack;

public class AddClayPanRecipe extends BasicAction {

	private String outputStr;
	private String inputStr;
	private String texture;
	private String display;
	
	public AddClayPanRecipe(JMODRepresentation owner, String out, String input, String texture, String display) {
		super(owner);
		this.outputStr = out;
		this.inputStr = input;
		this.texture = texture;
		this.display = display;
	}

	@Override
	public boolean on(FMLLoadCompleteEvent event) {
		this.valid = false;
		
		Stream.of(lib.stringToMaybeItemStackNoOreDic(inputStr), 
				  lib.stringToMaybeItemStackNoOreDic(outputStr));
		
		lib.stringToMaybeItemStackNoOreDic(inputStr).ifPresent(input -> {
			lib.stringToMaybeItemStackNoOreDic(outputStr).ifPresent(output -> {
				if(this.recipeTests()) {
					addRecipe(input, output);
				}
			});
		});
		
		return this.valid;
	}
	
	private void addRecipe(ItemStack input, ItemStack output) {
		RecipeRegisterManager.panRecipe.register(input, output, this.texture, this.display);
		this.valid = true;
	}
	
	private boolean recipeTests() {
		return this.texture != null && !this.texture.isEmpty() && 
			   this.display != null && !this.display.isEmpty();
	}
}
