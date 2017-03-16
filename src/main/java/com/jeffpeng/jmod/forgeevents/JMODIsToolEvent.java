package com.jeffpeng.jmod.forgeevents;

import net.minecraft.item.Item;

public class JMODIsToolEvent extends JMODCancelableForgeEvent {
	Item item;
	
	public JMODIsToolEvent(Item item){
		this.item = item;
	}
}
