package com.jeffpeng.jmod.actions;

import java.util.Map;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;
import com.jeffpeng.jmod.util.Reflector;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

public class AddChestLoot extends BasicAction {
	private String itemstack;
	private Integer min;
	private Integer max;
	private Integer weight;
	private String[] targets;
	
	public AddChestLoot(JMODRepresentation owner, String is, Integer min, Integer max, Integer weight, String[] targets){
		super(owner);
		this.itemstack = is;
		this.min = min;
		this.max = max;
		this.weight = weight;
		this.valid = true;
		this.targets = targets;
	}
	
	private WeightedRandomChestContent getWeightedRandomChestContent(){
		Object is = lib.stringToItemStack(this.itemstack);
		if(is instanceof ItemStack)
				return new WeightedRandomChestContent((ItemStack)is,this.min,this.max,this.weight);
		else return null;
	}
	
	@Override
	public void execute(){
		Reflector categoriesReflector = new Reflector(null,ChestGenHooks.class);
		@SuppressWarnings("unchecked")
		Map<String,ChestGenHooks> categories = (Map<String,ChestGenHooks>)categoriesReflector.get("chestInfo");
		
		
		Object is = lib.stringToItemStack(itemstack);
		if(is instanceof ItemStack)
		if(targets == null){
			for(Map.Entry<String,ChestGenHooks> category : categories.entrySet()){
				category.getValue().addItem(getWeightedRandomChestContent());
			}
		} else {
			for(String categoryname : targets){
				ChestGenHooks category =  categories.get(categoryname);
				if(category != null) category.addItem(getWeightedRandomChestContent());
			}
		}

	}
	
	
}
