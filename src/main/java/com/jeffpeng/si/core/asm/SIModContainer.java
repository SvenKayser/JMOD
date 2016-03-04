package com.jeffpeng.si.core.asm;



import java.util.Arrays;

import com.google.common.eventbus.EventBus;
import com.jeffpeng.si.core.SI;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;


public class SIModContainer extends DummyModContainer {
	public SIModContainer(){
		super(new ModMetadata());
		ModMetadata meta = super.getMetadata();
		meta.modId = "si.core.plugin";
		meta.name = "Survival Industry FML Loading Plugin";
		meta.version = SI.VERSION;
		meta.authorList = Arrays.asList("JeffPeng");
		meta.description = "Handles core plugin tasks such as ASM hooks";
		meta.credits = "The Survival Industry Team for the pack idea, and Reika, Kinglemming, Kingrunes and many others for their sources on ASM implementations";
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
