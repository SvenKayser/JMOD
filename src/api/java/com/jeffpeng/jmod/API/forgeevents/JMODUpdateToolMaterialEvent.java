package com.jeffpeng.jmod.API.forgeevents;

import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;

public class JMODUpdateToolMaterialEvent extends JMODCancelableForgeEvent {
	public Item item;
	public ToolMaterial toolmat;
	
	public JMODUpdateToolMaterialEvent(Item item,ToolMaterial toolmat){
		this.item = item;
		this.toolmat = toolmat;
	}
}
