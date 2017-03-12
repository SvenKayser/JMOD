package com.jeffpeng.jmod.types.items;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.interfaces.IItem;
import com.jeffpeng.jmod.primitives.BasicAction;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBucket;

public class CoreBucket extends ItemBucket implements IItem{
	
	private JMODRepresentation owner;
	private String internalName;
	private int burnTime = 0;

	
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
	
	@Override
	public void processSettings(BasicAction settings) {
		if(settings.hasSetting("burntime"))		this.burnTime	 = settings.getInt("burntime") & 15;
		
	}
	
	@Override 
	public int getBurnTime(){
		return this.burnTime;
	}
	
	@Override
	public void setBurnTime(int bt){
		this.burnTime = bt;
	}
}
