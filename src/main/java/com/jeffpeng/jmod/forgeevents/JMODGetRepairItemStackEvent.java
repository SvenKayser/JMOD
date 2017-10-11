
package com.jeffpeng.jmod.forgeevents;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class JMODGetRepairItemStackEvent extends JMODCancelableForgeEvent {
	private ItemStack ris;
	private Item item;
	
	public void setRepairItemStack(ItemStack is){
		this.ris = is;
		this.setCanceled(true);
	}
	
	public ItemStack getRepairItemStack(){
		return ris;
	}
	
	public Item getItem(){
		return item;
	}
	
	public JMODGetRepairItemStackEvent(Item item){
		this.item = item;
	}
}
