package com.jeffpeng.jmod.actions.rotarycraft;

import net.minecraft.item.ItemStack;
import Reika.RotaryCraft.API.RecipeInterface;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;

public class AddCompactorRecipe extends BasicAction {
	private String inString;
	private String outString;
	private int pressure;
	private int temperature;
	
	private ItemStack in;
	private ItemStack out;
	
	public AddCompactorRecipe(JMODRepresentation owner, String out, String in, int pressure, int temperature){
		super(owner);
		this.inString = in;
		this.outString = out;
		this.temperature = temperature;
		this.pressure = pressure;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event){
		valid = false;
		Object inIs = lib.stringToItemStack(inString);
		
		if(inIs instanceof ItemStack){
			Object outIs = lib.stringToItemStack(outString);
			
			if(outIs instanceof ItemStack){
				valid = true;
				in = (ItemStack)inIs;
				out = (ItemStack)outIs;
			}
		}
		
		valid &= (pressure > 0);
		if(valid) execute();
		return valid;
	}
	
	@Override
	public void execute(){
		RecipeInterface.compactor.addAPIRecipe(in, out, pressure, temperature);
	}
}
