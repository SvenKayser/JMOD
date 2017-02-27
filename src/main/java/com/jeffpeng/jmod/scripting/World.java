package com.jeffpeng.jmod.scripting;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.interfaces.IEventObject;
import com.jeffpeng.jmod.primitives.OwnedObject;

public class World extends OwnedObject implements IEventObject {

	public World(JMODRepresentation owner) {
		super(owner);
	}

	@Override
	public void on(String trigger) {
		
		
	}

	@Override
	public boolean fire(String trigger) {
		// TODO Auto-generated method stub
		return false;
	}
	
	 
	
	
}
