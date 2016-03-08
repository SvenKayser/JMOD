package com.jeffpeng.jmod.scripting.mods;

import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.OwnedObject;

public class Sync extends OwnedObject {

	public Sync(JMODRepresentation owner) {
		super(owner);
	}
	
	public void preventRecipeReload(boolean value){
		if(owner.testForMod("Sync")) JMOD.getGlobalConfig().preventSyncRecipeReload |= value;
	}

}
