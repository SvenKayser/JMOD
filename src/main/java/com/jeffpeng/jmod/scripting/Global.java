package com.jeffpeng.jmod.scripting;

import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.OwnedObject;

public class Global extends OwnedObject {

	public Global(JMODRepresentation owner) {
		super(owner);
	}
	
	public void preventToolBreaking(boolean value){
		JMOD.getGlobalConfig().preventToolBreaking |= value;
	}

}
