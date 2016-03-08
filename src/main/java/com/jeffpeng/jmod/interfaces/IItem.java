package com.jeffpeng.jmod.interfaces;

import net.minecraft.item.Item;


public interface IItem {
	

	
	public String getPrefix();
	
	public void setName(String name);
	
	public Item setTextureName(String name);
	
	public Item setMaxStackSize(int stacksize);
	
	public void register();
	default void setRecipes(){
		
	}

}
