package com.jeffpeng.jmod.actions;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.descriptors.ItemStackDescriptor;
import com.jeffpeng.jmod.primitives.BasicAction;
import com.jeffpeng.jmod.types.CreativeTab;

import cpw.mods.fml.common.event.FMLInitializationEvent;

public class AddCreativeTab extends BasicAction {
	
	private ItemStackDescriptor tabItem;
	private String tabName;
	private String tabId;

	public AddCreativeTab(JMODRepresentation owner,String tabId, String tabName, ItemStackDescriptor tabItem) {
		super(owner);
		this.tabName = tabName;
		this.tabItem = tabItem;
		this.tabId = tabId;
	}
	
	@Override
	public boolean on(FMLInitializationEvent event){
		new CreativeTab(tabId,tabName,tabItem);
		return true;
	}
	
	@Override
	public int priority()
	{
		return Priorities.AddCreativeTab;
	}
}
