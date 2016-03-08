package com.jeffpeng.jmod;


import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CreativeTab extends CreativeTabs {
	private Item iconItem;
	public CreativeTab(String tabId, String tabName, String itemString) {
		super(CreativeTabs.getNextID(), tabId);
		Object is = Lib.stringToItemStackStatic(itemString); 
		if(is instanceof ItemStack){
			iconItem = ((ItemStack)is).getItem(); 
		} else {
			iconItem = new Item();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		//return SI.REGISTRY.getItemAsItem("CTEnblem");
		
		
		return iconItem;
		
		//TODO: Crashes. Why?
		//return (Item)SI.REGISTRY.getItem("ctenblem");
	}
	
	
	
	
}
