package com.jeffpeng.jmod.scripting.mods;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.OwnedObject;
import com.jeffpeng.jmod.util.actions.applecore.ModifyFoodValue;

public class Applecore extends OwnedObject {

	public Applecore(JMODRepresentation owner){
		super(owner);
	}
	
	public void modifyFoodValue(String food, int hunger, float saturationModifier) {
		if(owner.testForMod("AppleCore")) new ModifyFoodValue(owner, food, hunger, saturationModifier);
	}
}
