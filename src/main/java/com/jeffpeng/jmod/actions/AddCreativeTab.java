package com.jeffpeng.jmod.actions;

import com.jeffpeng.jmod.CreativeTab;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLInitializationEvent;

public class AddCreativeTab extends BasicAction {
	
	private String tabItem;
	private String tabName;
	private String tabId;

	public AddCreativeTab(JMODRepresentation owner,String tabId, String tabName, String tabItem) {
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
		return 60;
	}
}
