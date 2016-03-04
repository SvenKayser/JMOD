package com.jeffpeng.si.core.modintegration.nei;

import com.jeffpeng.si.core.SI;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEI_SIConfig implements IConfigureNEI {

	@Override
	public void loadConfig() {
		API.registerRecipeHandler(new SIRecipeHandler());
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return SI.VERSION;
	}

}
