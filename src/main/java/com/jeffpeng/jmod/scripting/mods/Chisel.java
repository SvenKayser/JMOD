package com.jeffpeng.jmod.scripting.mods;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.OwnedObject;
import com.jeffpeng.jmod.util.actions.chisel.AddCarvingVariation;
import com.jeffpeng.jmod.util.actions.chisel.AddCarvingGroup;

public class Chisel extends OwnedObject {
	public Chisel(JMODRepresentation owner) {
		super(owner);
	}

	public  void addGroup(String group){
		if(owner.testForMod("chisel")) new AddCarvingGroup(group);
	}
	
	public  void addVariation(String group, String variation){
		if(owner.testForMod("chisel")) new AddCarvingVariation(owner,group,variation);
	}
}
