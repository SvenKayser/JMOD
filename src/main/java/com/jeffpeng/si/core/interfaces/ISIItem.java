package com.jeffpeng.si.core.interfaces;

import com.jeffpeng.si.core.SI;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;


public interface ISIItem {
	

	
	default String getPrefix(){
		return SI.MODID;
	}
	
	public void setName(String name);
	
	public Item setTextureName(String name);
	
	public Item setMaxStackSize(int stacksize);
	
	public void presetCreativeTab(CreativeTabs creativetab);
	
	public void register();
	default void setRecipes(){
		
	}

	public void setCreativeTab();
	
}
