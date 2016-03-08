package com.jeffpeng.jmod.util.actions;

import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;
import com.jeffpeng.jmod.primitives.BasicAction;

public class RemoveSmeltingRecipe extends BasicAction {
	
	private String entry;

	public RemoveSmeltingRecipe(JMODRepresentation owner,String istoremove) {
		super(owner);
		this.entry = istoremove;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void execute(){
		Object is = lib.stringToItemStack(entry);
		if(is instanceof ItemStack){
			@SuppressWarnings("unchecked")
			Iterator<Entry> it = FurnaceRecipes.smelting().getSmeltingList().entrySet().iterator();
			while(it.hasNext()){
				Entry mapentry = it.next();
				ItemStack output = (ItemStack)mapentry.getKey();
				if(Lib.matchItemStacks(output, (ItemStack)is)) it.remove();
			}
		}
	}
	
	@Override
	public int priority(){
		return 20;
	}

}
