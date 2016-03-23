package com.jeffpeng.jmod.types.items;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.interfaces.IItem;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBucket;

public class CoreBucket extends ItemBucket implements IItem{
	
	private JMODRepresentation owner;
	private String internalName;

	public CoreBucket(JMODRepresentation owner, Block block) {
		super(block);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JMODRepresentation getOwner() {
		return owner;
	}

	@Override
	public void setName(String name) {
		internalName = name;
		
	}
	
	@Override
	public String getName(){
		return this.internalName;
	}
}
