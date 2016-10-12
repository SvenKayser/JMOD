package com.jeffpeng.jmod.descriptors;

import com.jeffpeng.jmod.validator.Validator;

public class ColorDescriptor {
	public int red;
	public int green;
	public int blue;
	
	public ColorDescriptor(int red, int green, int blue){
		this.red = red;
		this.blue = blue;
		this.green = green;
		
		Validator.msg(this.getClass().getSimpleName() + " OK.");
	}
	
	
}
