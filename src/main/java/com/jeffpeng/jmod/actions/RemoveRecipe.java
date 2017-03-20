package com.jeffpeng.jmod.actions;

import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;
import com.jeffpeng.jmod.descriptors.ItemStackDescriptor;
import com.jeffpeng.jmod.primitives.BasicAction;

public class RemoveRecipe extends BasicAction {
	
	private ItemStackDescriptor entry;

	public RemoveRecipe(JMODRepresentation owner,ItemStackDescriptor istoremove) {
		super(owner);
		this.entry = istoremove;
		this.valid = true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(){
		List<ItemStack> isl = entry.getItemStackList();
		isl.forEach((v) -> {
			Iterator<IRecipe> it = ((List<IRecipe>)CraftingManager.getInstance().getRecipeList()).iterator();
			while(it.hasNext()){
				IRecipe recipe = it.next();
				ItemStack output = recipe.getRecipeOutput();
				if(Lib.matchItemStacks(output, v)) it.remove();
			}
		});
	}
	
	@Override
	public int priority(){
		return 20;
	}

}
