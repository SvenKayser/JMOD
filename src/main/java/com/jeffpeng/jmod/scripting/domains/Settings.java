package com.jeffpeng.jmod.scripting.domains;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.OwnedObject;

public class Settings extends OwnedObject {

	public Settings(JMODRepresentation owner) {
		super(owner);

	}
	
	public void showToolHarvestLevels(boolean value){
		config.put("showToolHarvestLevels",value);
	}
	
	public void showArmorValues(boolean value){
		config.put("showArmorValues",value);
	}
	
	public void showBlockHarvestLevels(boolean value){
		config.put("showBlockHarvestLevels",value);
	}
	
	public void enhancedAnvilRepair(boolean value){
		config.put("enhancedAnvilRepair",value);
	}
	
	public void craftingGridToolRepair(boolean value){
		config.put("craftingGridToolRepair",value);
	}
}
