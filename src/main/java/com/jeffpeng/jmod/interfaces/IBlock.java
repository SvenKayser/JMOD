package com.jeffpeng.jmod.interfaces;

public interface IBlock extends IOwned, IFurnaceFuel{
	public String getName();
	default public String getPrefix(){
		return getOwner().getModId();
	}
	
	default public void register(){
		getOwner().registerBlock(this);
	}
	
}
