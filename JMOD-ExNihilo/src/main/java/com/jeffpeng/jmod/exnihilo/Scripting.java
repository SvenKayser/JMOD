package com.jeffpeng.jmod.exnihilo;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.exnihilo.actions.AddSifting;
import com.jeffpeng.jmod.primitives.ModScriptObject;
import com.jeffpeng.jmod.primitives.OwnedObject;

public class Scripting extends ModScriptObject {
	public Scripting(JMODRepresentation owner){
		super(owner);
	}
	
	public void addSifting(String output, String input, int rarity){
		if(owner.testForMod("exnihilo")) new AddSifting(owner,output,input,rarity);
	}
}
