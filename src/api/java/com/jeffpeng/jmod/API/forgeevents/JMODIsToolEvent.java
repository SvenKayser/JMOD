package com.jeffpeng.jmod.API.forgeevents;

import net.minecraft.item.Item;

public class JMODIsToolEvent extends JMODCancelableForgeEvent {
	Item item;
	
	public JMODIsToolEvent(Item item){
		this.item = item;
	}
}
