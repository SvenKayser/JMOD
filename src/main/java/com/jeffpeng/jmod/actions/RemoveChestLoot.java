package com.jeffpeng.jmod.actions;

import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ChestGenHooks;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;
import com.jeffpeng.jmod.util.Reflector;

public class RemoveChestLoot extends BasicAction {

	private String itemstack;
	private String[] targets;
	
	public RemoveChestLoot(JMODRepresentation owner, String is, String[] targets) {
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
		
		
		Object is = lib.stringToItemStack(itemstack);
		if(is instanceof ItemStack)
			if(targets == null)
				for(Map.Entry<String,ChestGenHooks> category : categories.entrySet())	category.getValue().removeItem((ItemStack)is);
				
			else {
				for(String categoryname : targets){
					ChestGenHooks category =  categories.get(categoryname);
					if(category != null) category.removeItem((ItemStack)is);
				}
			}
		
		
	}
	
	@Override
	public int priority(){
		return Priorities.RemoveChestLoot;
	}

}
