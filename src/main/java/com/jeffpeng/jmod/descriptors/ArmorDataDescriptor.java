package com.jeffpeng.jmod.descriptors;

import com.jeffpeng.jmod.validator.Validator;

public class ArmorDataDescriptor {
	public String armorType;
	public String armorMaterial;
	
	
	public ArmorDataDescriptor(String armorMaterial, String armorType){
		this.armorMaterial = armorMaterial;
		this.armorType = armorType;
		
		Validator.msg(this.getClass().getSimpleName() + " OK.");
	}
}
