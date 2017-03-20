package com.jeffpeng.jmod.actions;

import java.util.List;

import net.minecraft.item.ItemStack;

import com.jeffpeng.jmod.Defines.UseOredict;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.descriptors.ItemStackDescriptor;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.registry.GameRegistry;

public class AddSmeltingRecipe extends BasicAction {

	private ItemStackDescriptor result;
	private ItemStackDescriptor ingredient;
	private int xp = 0;
	
	public AddSmeltingRecipe(JMODRepresentation owner, ItemStackDescriptor result, ItemStackDescriptor ingredient) {
		super(owner);
		this.result = result;
		this.ingredient = ingredient;
		this.valid = true;

	}
	
	public void setXP(int xp){
		this.xp = xp;
	}
	
	@Override
	public void execute(){
		List<ItemStack> iis = ingredient.getItemStackList(UseOredict.PREFER);
		ItemStack ois = result.toItemStack();
		
		if(ois == null || iis.size() == 0){
			log.warn("The smelting recipe " + ingredient + " -> " + result + " is invalid.");
		} else {
			iis.forEach((v) -> GameRegistry.addSmelting(v, ois, xp));
		} 
	}
	
	@Override
	public int priority(){
		return Priorities.AddSmeltingRecipe;

	}

}
