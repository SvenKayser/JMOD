package com.jeffpeng.jmod.scripting.domains;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.interfaces.IEventObject;
import com.jeffpeng.jmod.primitives.OwnedObject;

public class Players extends OwnedObject implements IEventObject {

	public Players(JMODRepresentation owner) {
		super(owner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void on(String trigger, Object callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean fire(String trigger) {
		// TODO Auto-generated method stub
		return false;
	}

}
