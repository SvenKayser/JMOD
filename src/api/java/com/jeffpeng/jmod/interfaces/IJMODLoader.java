package com.jeffpeng.jmod.interfaces;

import cpw.mods.fml.common.ModContainer;

public interface IJMODLoader {

	boolean isModLoaded(String modid);

	boolean isPluginLoaded(String pluginid);

	ModContainer getModContainer(String modid);
	
}
