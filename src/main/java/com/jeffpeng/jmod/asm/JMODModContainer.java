package com.jeffpeng.jmod.asm;



import java.util.Arrays;

import com.google.common.eventbus.EventBus;
import com.jeffpeng.jmod.JMOD;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;


public class JMODModContainer extends DummyModContainer {
	public JMODModContainer(){
		super(new ModMetadata());
		ModMetadata meta = super.getMetadata();
		meta.modId = "jmod.plugin";
		meta.name = "JMOD FML Loading Plugin";
		meta.version = JMOD.VERSION;
		meta.authorList = Arrays.asList("JeffPeng");
		meta.description = "Handles core plugin tasks such as ASM hooks";
		meta.credits = "The Survival Industry Team for the pack idea that eventually lead to this mod, and Reika, Kinglemming, Kingrunes and many others for their sources on ASM implementations";
		meta.url = "";
		meta.updateUrl = "";
		meta.screenshots = new String[0];
		meta.logoFile = "";
		
		
	}
	
	@Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
		bus.register(this);
		return true;
    }

}
