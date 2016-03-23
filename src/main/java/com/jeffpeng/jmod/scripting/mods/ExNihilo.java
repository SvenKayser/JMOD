package com.jeffpeng.jmod.scripting.mods;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.actions.exnihilo.AddSifting;
import com.jeffpeng.jmod.primitives.OwnedObject;

public class ExNihilo extends OwnedObject {
	public ExNihilo(JMODRepresentation owner){
		super(owner);
	}
	
	public void addSifting(String output, String input, int rarity){
		if(owner.testForMod("exnihilo")) new AddSifting(owner,output,input,rarity);
	}
}
