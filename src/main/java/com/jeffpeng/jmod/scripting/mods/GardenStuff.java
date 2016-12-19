package com.jeffpeng.jmod.scripting.mods;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.actions.gardenstuff.AddCompost;
import com.jeffpeng.jmod.primitives.OwnedObject;

public class GardenStuff  extends OwnedObject {

	public GardenStuff(JMODRepresentation owner) {
		super(owner);
	}
	
	public void addCompostableItem(String itemName, int decomposeTime){
		if(owner.testForMod("GardenCore")) new AddCompost(owner, itemName, decomposeTime);
	}
}
