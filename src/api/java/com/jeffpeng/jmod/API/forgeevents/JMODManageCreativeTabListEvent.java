package com.jeffpeng.jmod.API.forgeevents;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class JMODManageCreativeTabListEvent extends JMODForgeEvent {
	private List<ItemStack> isList;
	private CreativeTabs tab;
	
	public JMODManageCreativeTabListEvent(CreativeTabs tab, List<ItemStack> isList){
		this.isList = isList;
		this.tab = tab;
	}
	
	public List<ItemStack> getList(){
		return this.isList;
	}
	
	public CreativeTabs getTab(){
		return this.tab;
	}

}
