package com.jeffpeng.jmod.chisel;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.chisel.actions.AddCarvingGroup;
import com.jeffpeng.jmod.chisel.actions.AddCarvingVariation;
import com.jeffpeng.jmod.primitives.ModScriptObject;
import com.jeffpeng.jmod.primitives.OwnedObject;

public class Scripting extends ModScriptObject {
	public Scripting(JMODRepresentation owner) {
		super(owner);
	}

	public  void addGroup(String group){
		if(owner.testForMod("chisel")) new AddCarvingGroup(owner,group);
	}
	
	public  void addVariation(String group, String variation){
		if(owner.testForMod("chisel")) new AddCarvingVariation(owner,group,variation);
	}
}
