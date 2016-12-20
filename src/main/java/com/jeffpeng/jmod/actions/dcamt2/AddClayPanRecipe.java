package com.jeffpeng.jmod.actions.dcamt2;

import java.util.Optional;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import mods.defeatedcrow.api.recipe.RecipeRegisterManager;
import net.minecraft.item.ItemStack;

public class AddClayPanRecipe extends BasicAction {

	private String outStr;
	private String outStrJp;
	private String inStr;
	private String texture;
	private String display;
	
	public AddClayPanRecipe(JMODRepresentation owner, String out, String outJpBowl, String input, String texture, String display) {
		super(owner);
		this.outStr = out;
		this.inStr = input;
		this.texture = texture;
		this.display = display;
		this.outStrJp = outJpBowl;
	}

	public AddClayPanRecipe(JMODRepresentation owner, String out, String input, String texture, String display) {
		super(owner);
		this.outStr = out;
		this.inStr = input;
		this.texture = texture;
		this.display = display;
	}

	@Override
	public boolean on(FMLLoadCompleteEvent event){
		Optional<ItemStack> outItem = Optional.ofNullable(lib.stringToItemStackNoOreDict(outStr));
		Optional<ItemStack> outJpItem = Optional.ofNullable(lib.stringToItemStackNoOreDict(outStrJp));
		Optional<ItemStack> inputItem = Optional.ofNullable(lib.stringToItemStackNoOreDict(inStr));
		boolean isValid = false;
		
		if ( outItem.isPresent() && inputItem.isPresent() && 
		     this.texture != null && this.display != null && 
		    !this.texture.isEmpty() && !this.display.isEmpty()) {
			
			isValid = true;
		}
		
		if (outJpItem.isPresent()) {
			outJpItem.ifPresent(outputJP -> outItem.ifPresent(output ->  inputItem.ifPresent(input -> {
				RecipeRegisterManager.panRecipe.register(input, output, outputJP, this.texture, this.display);
			})));
		} else {
			outItem.ifPresent(output ->  inputItem.ifPresent(input -> {
				RecipeRegisterManager.panRecipe.register(input, output, this.texture, this.display);
			}));
		}
		
		return isValid;
	}
}
