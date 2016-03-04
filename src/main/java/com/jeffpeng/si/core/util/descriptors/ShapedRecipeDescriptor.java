package com.jeffpeng.si.core.util.descriptors;

import java.util.List;

import net.minecraft.item.crafting.IRecipe;

import com.jeffpeng.si.core.crafting.StringListRecipe;
import com.jeffpeng.si.core.interfaces.ISIExecutableObject;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class ShapedRecipeDescriptor implements ISIExecutableObject{
	
	private String result;
	private List<String[]> shape;
	private StringListRecipe recipe;
	private boolean valid;
	
	
	public ShapedRecipeDescriptor(String result, List<String[]> shape){
		registerAsStaged();
		this.result = result;
		this.shape = shape;
	}
	
	@Override
	public boolean on(FMLPostInitializationEvent event){
		this.recipe = new StringListRecipe(result,shape);
		this.valid = recipe.isValid();
		return this.valid;
	}
	
	@Override
	public boolean isValid() {
		return this.valid;
	}

	@Override
	public void execute() {
		GameRegistry.addRecipe(recipe);
		
	}
}
