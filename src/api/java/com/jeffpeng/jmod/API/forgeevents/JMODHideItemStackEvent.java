package com.jeffpeng.jmod.API.forgeevents;

import net.minecraft.item.ItemStack;

public class JMODHideItemStackEvent extends JMODForgeEvent {
	private ItemStack is;
	
	public JMODHideItemStackEvent(ItemStack is){
		this.is = is;
	}
	
	public ItemStack get(){
		return is;
	}
}
