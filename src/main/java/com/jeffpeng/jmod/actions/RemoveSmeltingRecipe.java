package com.jeffpeng.jmod.actions;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;
import com.jeffpeng.jmod.descriptors.ItemStackDescriptor;
import com.jeffpeng.jmod.primitives.BasicAction;

public class RemoveSmeltingRecipe extends BasicAction {
	
	private ItemStackDescriptor entry;

	public RemoveSmeltingRecipe(JMODRepresentation owner,ItemStackDescriptor istoremove) {
		super(owner);
		this.entry = istoremove;
		this.valid = true;
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(){
		List<ItemStack> isl = entry.getItemStackList();
		isl.forEach((v) -> {
			Iterator<Entry> it = FurnaceRecipes.smelting().getSmeltingList().entrySet().iterator();
			while(it.hasNext()){
				Entry mapentry = it.next();
				ItemStack output = (ItemStack)mapentry.getValue();
				if(Lib.matchItemStacks(output, v)) it.remove();
			}
		});
	}
	
	@Override
	public int priority(){
		return Priorities.RemoveSmeltingRecipe;
	}

}
