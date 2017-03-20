package com.jeffpeng.jmod.API.forgeevents;

import net.minecraft.item.Item;

public class JMODPatchToolEvent extends JMODCancelableForgeEvent {
	public Item item;
	public String itemname;
	
	public JMODPatchToolEvent(Item item,String itemname){
		this.item = item;
		this.itemname = itemname;
	}
}
