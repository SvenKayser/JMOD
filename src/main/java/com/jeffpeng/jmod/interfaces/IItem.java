package com.jeffpeng.jmod.interfaces;

import net.minecraft.item.Item;


public interface IItem extends IOwned{
	
	
	
	default public String getPrefix(){
		return getOwner().getModId();
	}
	
	public void setName(String name);
	
	public String getName();
	
	public Item setTextureName(String name);
	
	public Item setMaxStackSize(int stacksize);
	
	default public void register(){
		getOwner().registerItem(this);
	}
	default public void setRecipes(){
		
	}

}
