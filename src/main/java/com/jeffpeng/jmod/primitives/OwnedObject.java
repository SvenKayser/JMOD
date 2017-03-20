package com.jeffpeng.jmod.primitives;

import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;
import com.jeffpeng.jmod.interfaces.IEventObject;
import com.jeffpeng.jmod.interfaces.IOwned;


public class OwnedObject implements IEventObject, IOwned {
	protected JMODRepresentation owner;
	protected Map<String,Object> config;
	protected Lib lib;
	protected Logger log;
	
	public OwnedObject(JMODRepresentation owner){
		this.owner = owner;
		this.lib = owner.getLib();
		this.config = owner.getConfig();
		this.log = owner.getLogger();
	}
	

	
	public JMODRepresentation getOwner(){
		return owner;
	}

	@Override
	public void on(String trigger, Object callback) {
		
	}

	@Override
	public boolean fire(String trigger) {
		return false;
		
	}
	
	@Override
	public boolean hasOwner(){
		return this.owner != null;
	}

}
