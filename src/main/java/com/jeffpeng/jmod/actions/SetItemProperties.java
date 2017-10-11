package com.jeffpeng.jmod.actions;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

public class SetItemProperties extends BasicAction {
	
	private String itemstring;
	
	public SetItemProperties(JMODRepresentation owner, String itemstring) {
		super(owner);
		this.itemstring = itemstring;
		this.valid = true;
	}
	
	private String container;
	private Integer stackSize;
	private Integer durability;
	
	public SetItemProperties stackSize(int stackSize){		this.stackSize = stackSize;		return this;	}
	public SetItemProperties container(String container){	this.container = container;		return this;	}
	public SetItemProperties durability(int durability){	this.durability= durability;	return this;	}
	
	public void execute(){
		ItemStack is = lib.stringToItemStackNoOreDict(itemstring);
		if(is != null){
			Item item = is.getItem();
			
			if(stackSize != null) item.setMaxStackSize(this.stackSize);
			if(container != null){
				ItemStack cis = lib.stringToItemStackOrFirstOreDict(container);
				if(cis != null) item.setContainerItem(cis.getItem());
			}
			if(durability != null) item.setMaxDamage(durability);

		} else {
			log.warn("Cannot patch item properties for " + itemstring + " as it is not in the game. Omitting.");
		}
		
	}
	
}
