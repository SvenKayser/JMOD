package com.jeffpeng.jmod.scripting;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.OwnedObject;

public class Settings extends OwnedObject {

	public Settings(JMODRepresentation owner) {
		super(owner);

	}
	
	public void showToolHarvestLevels(boolean value){
		config.showToolHarvestLevels = value;
	}
	
	public void showArmorValues(boolean value){
		config.showArmorValues = value;
	}
	
	public void showBlockHarvestLevels(boolean value){
		config.showBlockHarvestLevels = value;
	}
	
	public void enhancedAnvilRepair(boolean value){
		config.enhancedAnvilRepair = value;
	}
	
	public void craftingGridToolRepair(boolean value){
		config.craftingGridToolRepair = value;
	}
}
