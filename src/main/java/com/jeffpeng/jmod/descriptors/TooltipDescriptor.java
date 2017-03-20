package com.jeffpeng.jmod.descriptors;

import com.jeffpeng.jmod.validator.Validator;

public class TooltipDescriptor {
	public ItemStackDescriptor[] target;
	public String[] lines;
	
	public TooltipDescriptor(ItemStackDescriptor[] target, String[] lines){
		this.target = target;
		this.lines = lines;
		
		Validator.msg(this.getClass().getSimpleName() + " OK.");
	}
}
