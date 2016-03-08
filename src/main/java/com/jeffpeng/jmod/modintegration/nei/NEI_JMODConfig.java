package com.jeffpeng.jmod.modintegration.nei;

import com.jeffpeng.jmod.JMOD;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEI_JMODConfig  implements IConfigureNEI {
	
	@Override
	public void loadConfig() {
		API.registerRecipeHandler(new RecipeHandler());
		
	}

	@Override
	public String getName() {
		return JMOD.NAME;
	}

	@Override
	public String getVersion() {
		return JMOD.VERSION;
	}

}
