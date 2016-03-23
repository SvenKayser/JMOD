package com.jeffpeng.jmod.descriptors;

import com.jeffpeng.jmod.validator.Validator;

public class ItemStackSubstituteDescriptor {
	public String source;
	public String target;
	
	public ItemStackSubstituteDescriptor(String source, String target){
		this.source = source;
		this.target = target;
		
		Validator.msg(this.getClass().getSimpleName() + " OK.");
	}
	
}
