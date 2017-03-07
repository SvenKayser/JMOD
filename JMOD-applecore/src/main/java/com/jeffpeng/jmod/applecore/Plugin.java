package com.jeffpeng.jmod.applecore;

import net.minecraftforge.common.MinecraftForge;

import com.jeffpeng.jmod.JMODPlugin;
import com.jeffpeng.jmod.JMODPluginContainer;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;

public class Plugin extends JMODPlugin {

	public Plugin(JMODPluginContainer container) {
		super(container);
	}
	
	@Override
	public void on(FMLInitializationEvent event) {
		if(Loader.isModLoaded("AppleCore"))	{
			MinecraftForge.EVENT_BUS.register(ModifyFoodValues.getInstance());
		}
	}

}
