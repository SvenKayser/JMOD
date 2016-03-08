package com.jeffpeng.jmod.util.actions;

import java.util.Iterator;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;
import com.jeffpeng.jmod.primitives.BasicAction;

public class RemoveRecipe extends BasicAction {
	
	private String entry;

	public RemoveRecipe(JMODRepresentation owner,String istoremove) {
		super(owner);
		this.entry = istoremove;
		this.valid = true;
	}
	
	@Override
	public void execute(){
		Object is = lib.stringToItemStack(entry);
		if(is instanceof ItemStack){
			@SuppressWarnings("rawtypes")
			Iterator it = CraftingManager.getInstance().getRecipeList().iterator();
			while(it.hasNext()){
				IRecipe recipe = ((IRecipe)it.next());
				ItemStack output = recipe.getRecipeOutput();
				if(Lib.matchItemStacks(output, (ItemStack)is)) it.remove();
			}
		}
	}
	
	@Override
	public int priority(){
		return 20;
	}

}
