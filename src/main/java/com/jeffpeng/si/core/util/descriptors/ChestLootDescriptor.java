package com.jeffpeng.si.core.util.descriptors;

import com.jeffpeng.si.core.SILib;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;

public class ChestLootDescriptor {
	public String itemstack;
	public Integer min;
	public Integer max;
	public Integer weight;
	
	public ChestLootDescriptor(String is, Integer min, Integer max, Integer weight){
		this.itemstack = is;
		this.min = min;
		this.max = max;
		this.weight = weight;
	}
	
	public WeightedRandomChestContent getWeightedRandomChestContent(){
		Object is = SILib.stringToItemStack(this.itemstack);
		if(is instanceof ItemStack)
				return new WeightedRandomChestContent((ItemStack)is,this.min,this.max,this.weight);
		else return null;
	}
}
