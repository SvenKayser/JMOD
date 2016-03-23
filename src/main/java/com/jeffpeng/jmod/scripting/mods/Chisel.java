package com.jeffpeng.jmod.scripting.mods;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.actions.chisel.AddCarvingGroup;
import com.jeffpeng.jmod.actions.chisel.AddCarvingVariation;
import com.jeffpeng.jmod.primitives.OwnedObject;

public class Chisel extends OwnedObject {
	public Chisel(JMODRepresentation owner) {
		super(owner);
	}

	public  void addGroup(String group){
		if(owner.testForMod("chisel")) new AddCarvingGroup(owner,group);
	}
	
	public  void addVariation(String group, String variation){
		if(owner.testForMod("chisel")) new AddCarvingVariation(owner,group,variation);
	}
}
