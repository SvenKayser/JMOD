package com.jeffpeng.jmod.util.actions;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.registry.GameRegistry;

public class AddSmeltingRecipe extends BasicAction {

	private String result;
	private String ingredient;
	private int xp = 0;
	
	public AddSmeltingRecipe(JMODRepresentation owner, String result, String ingredient) {
		super(owner);
		this.result = result;
		this.ingredient = ingredient;

	}
	
	public void setXP(int xp){
		this.xp = xp;
	}
	
	@Override
	public void execute(){
		Object iis = lib.stringToItemStack(ingredient);
		ItemStack ois = lib.stringToItemStackNoOreDict(result);
		
		if(ois == null){
			log.warn("The smelting recipe " + ingredient + " -> " + result + " is invalid. Note: You cannot use OreDict entries for the output.");
		} else
		
		if(iis instanceof ItemStack){
			GameRegistry.addSmelting((ItemStack)iis, ois, xp);
		} else
		
		if(iis instanceof String){
			String iod = (String)iis;
			if(!OreDictionary.doesOreNameExist(iod)){
				log.warn("Cannot add smelting recipe for ore dictionary entry " + iod + " since it does not exist.");
			} else {
				List<ItemStack> ingredients = OreDictionary.getOres(iod);
				ingredients.forEach(ingredientis -> {
					GameRegistry.addSmelting(ingredientis, ois, xp);
				});
			}
		}
	}
	
	@Override
	public int priority(){
		return 10;
	}

}
