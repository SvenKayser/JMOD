package com.jeffpeng.jmod.forgeevents;

import net.minecraft.item.Item;

public class JMODPatchToolEvent extends JMODCancelableEvent {
	public Item item;
	public String itemname;
	
	public JMODPatchToolEvent(Item item,String itemname){
		this.item = item;
		this.itemname = itemname;
	}
}
