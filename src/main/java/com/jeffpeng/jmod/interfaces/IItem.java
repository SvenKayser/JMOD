package com.jeffpeng.jmod.interfaces;

import cpw.mods.fml.common.event.FMLEvent;
import net.minecraft.item.Item;


public interface IItem extends IOwned, ISettingsProcessor, IFurnaceFuel{
	
	
	
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
	
	default public void on(FMLEvent event){
		
	}
	
	default public void setRecipes(){
		
	}
}
