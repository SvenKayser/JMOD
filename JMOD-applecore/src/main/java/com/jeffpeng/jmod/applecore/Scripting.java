package com.jeffpeng.jmod.applecore;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.ModScriptObject;
import com.jeffpeng.jmod.primitives.OwnedObject;
import com.jeffpeng.jmod.applecore.actions.ModifyFoodValue;

public class Scripting extends ModScriptObject {

	public Scripting(JMODRepresentation owner){
		super(owner);
	}
	
	public void modifyFoodValue(String food, int hunger, float saturationModifier) {
		if(owner.testForMod("AppleCore")) new ModifyFoodValue(owner, food, hunger, saturationModifier);
	}
}
