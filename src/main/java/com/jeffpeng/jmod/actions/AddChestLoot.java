package com.jeffpeng.jmod.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.descriptors.ItemStackDescriptor;
import com.jeffpeng.jmod.primitives.BasicAction;
import com.jeffpeng.jmod.util.Reflector;

public class AddChestLoot extends BasicAction {
	private ItemStackDescriptor itemstack;
	private Integer min;
	private Integer max;
	private Integer weight;
	private String[] targets;
	
	public AddChestLoot(JMODRepresentation owner, ItemStackDescriptor is, Integer min, Integer max, Integer weight, String[] targets){
		super(owner);
		this.itemstack = is;
		this.min = min;
		this.max = max;
		this.weight = weight;
		this.valid = true;
		this.targets = targets;
	}
	
	private List<WeightedRandomChestContent> getWeightedRandomChestContent(){
		List<ItemStack> is = itemstack.getItemStackList();
		List<WeightedRandomChestContent> returnList = new ArrayList<>();
		is.forEach((v) -> new WeightedRandomChestContent(v,this.min,this.max,this.weight));
		return returnList;
	}
	
	@Override
	public void execute(){
		Reflector categoriesReflector = new Reflector(null,ChestGenHooks.class);
		@SuppressWarnings("unchecked")
		Map<String,ChestGenHooks> categories = (Map<String,ChestGenHooks>)categoriesReflector.get("chestInfo");

		
		if(targets == null){
			for(Map.Entry<String,ChestGenHooks> category : categories.entrySet()){
				getWeightedRandomChestContent().forEach((v) -> category.getValue().addItem(v));
				
			}
		} else {
			for(String categoryname : targets){
				ChestGenHooks category =  categories.get(categoryname);
				if(category != null) getWeightedRandomChestContent().forEach((v) -> category.addItem(v));
			}
		}

	}
	
	
}
