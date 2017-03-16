package com.jeffpeng.jmod.forgeevents;

import net.minecraft.item.Item;

public class JMODGetRepairAmountEvent extends JMODForgeEvent {
	
	private Item item;
	private Float repairamount = 0F;
	
	public Item getItem(){
		return item;
	}
	
	public void setRepairAmount(Float amount){
		repairamount = Math.max(amount, repairamount);
	}
	
	public Float getRepairAmount(){
		return repairamount;
	}
	
	
	
	public JMODGetRepairAmountEvent(Item item){
		this.item = item;
	}
	
	
	

}
