package com.jeffpeng.jmod.primitives;

import org.apache.logging.log4j.Logger;

import com.jeffpeng.jmod.Config;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;

public class OwnedObject {
	protected JMODRepresentation owner;
	protected Config config;
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

}
