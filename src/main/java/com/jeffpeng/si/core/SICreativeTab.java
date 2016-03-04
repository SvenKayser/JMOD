package com.jeffpeng.si.core;


import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SICreativeTab extends CreativeTabs {
	private Item iconItem;
	public SICreativeTab() {
		super(CreativeTabs.getNextID(), "si.core");
		iconItem = new Item();
		iconItem.setTextureName("si.core:itemCTEnblem").setMaxStackSize(1).setUnlocalizedName("si.core.itemCTEnblem");
		GameRegistry.registerItem(iconItem, "itemCTEnblem");

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
