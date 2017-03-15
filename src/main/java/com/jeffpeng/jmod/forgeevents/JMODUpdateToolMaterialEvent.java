package com.jeffpeng.jmod.forgeevents;

import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;

public class JMODUpdateToolMaterialEvent extends JMODCancelableEvent {
	public Item item;
	public ToolMaterial toolmat;
	
	public JMODUpdateToolMaterialEvent(Item item,ToolMaterial toolmat){
		this.item = item;
		this.toolmat = toolmat;
	}
}
