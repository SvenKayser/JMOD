package com.jeffpeng.jmod.actions.rotarycraft;

import net.minecraft.item.ItemStack;
import Reika.RotaryCraft.API.RecipeInterface;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;

public class AddBlastFurnaceRecipe extends BasicAction {
	
	private String out;
	private ItemStack outStack;
	private int temperature;
	private int speed;
	private float xp;
	private Object recipe;
	
	public AddBlastFurnaceRecipe(JMODRepresentation owner, String out, int temperature, int speed, float xp, Object recipe){
		super(owner);
		this.out = out;
		this.temperature = temperature;
		this.speed = speed;
		this.xp = xp;
		this.recipe = recipe;
	}
	
	@Override
	public boolean on(FMLPostInitializationEvent event){
		ItemStack is = lib.stringToItemStackOrFirstOreDict(out);
		if(is != null){
			outStack = is;
			valid = true;
			execute();
		} else {
			valid = false;
		}
		return valid;
	}
	
	@Override
	public void execute(){
		RecipeInterface.blastfurn.addAPIRecipe(outStack, temperature, lib.convertPatterToShapedOreRecipe(outStack, recipe), speed, xp);
	}

}
