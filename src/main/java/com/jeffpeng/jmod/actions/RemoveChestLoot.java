package com.jeffpeng.jmod.actions;

import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ChestGenHooks;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.descriptors.ItemStackDescriptor;
import com.jeffpeng.jmod.primitives.BasicAction;
import com.jeffpeng.jmod.util.Reflector;

public class RemoveChestLoot extends BasicAction {

	private ItemStackDescriptor itemstack;
	private String[] targets;
	
	public RemoveChestLoot(JMODRepresentation owner, ItemStackDescriptor is, String[] targets) {
		super(owner);
		this.valid = true;
		this.targets = targets;
		this.itemstack = is;
	}
	
	@Override
	public void execute(){
		Reflector categoriesReflector = new Reflector(null,ChestGenHooks.class);
		@SuppressWarnings("unchecked")
		Map<String,ChestGenHooks> categories = (Map<String,ChestGenHooks>)categoriesReflector.get("chestInfo");
		
		
		List<ItemStack> isl = itemstack.getItemStackList();
	
		if(targets == null)
			for(Map.Entry<String,ChestGenHooks> category : categories.entrySet())	isl.forEach((v) -> category.getValue().removeItem(v));
			
		else {
			for(String categoryname : targets){
				ChestGenHooks category =  categories.get(categoryname);
				if(category != null) isl.forEach((v) -> category.removeItem(v));
			}
		}
		
		
	}
	
	@Override
	public int priority(){
		return Priorities.RemoveChestLoot;
	}

}
