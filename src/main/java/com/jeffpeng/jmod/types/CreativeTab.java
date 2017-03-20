package com.jeffpeng.jmod.types;


import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.jeffpeng.jmod.descriptors.ItemStackDescriptor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CreativeTab extends CreativeTabs {
	private Item iconItem;
	public CreativeTab(String tabId, String tabName, ItemStackDescriptor itemString) {
		super(CreativeTabs.getNextID(), tabId);
		ItemStack is = itemString.toItemStack(); 
		if(is != null){
			iconItem = ((ItemStack)is).getItem(); 
		} else {
			iconItem = new Item();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return iconItem;
	}
	
	
	
	
}
