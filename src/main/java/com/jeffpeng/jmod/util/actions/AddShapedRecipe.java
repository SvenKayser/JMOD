package com.jeffpeng.jmod.util.actions;

import java.util.List;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.crafting.StringListRecipe;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class AddShapedRecipe extends BasicAction{
	
	private String result;
	private List<String[]> shape;
	private StringListRecipe recipe;
	private boolean valid;
	
	
	public AddShapedRecipe(JMODRepresentation owner, String result, List<String[]> shape){
		super(owner);
		this.result = result;
		this.shape = shape;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event){
		this.recipe = new StringListRecipe(owner,result,shape);
		this.valid = recipe.isValid();
		if(!this.valid) log.info(result + " is invalid");
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
	
	@Override
	public int priority(){
		return 10;
	}
}
