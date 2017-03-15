package com.jeffpeng.jmod.forgeevents;

import net.minecraft.item.Item;

public class JMODIsToolEvent extends JMODCancelableEvent {
	Item item;
	
	public JMODIsToolEvent(Item item){
		this.item = item;
	}
}
