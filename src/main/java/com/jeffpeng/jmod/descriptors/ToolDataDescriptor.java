package com.jeffpeng.jmod.descriptors;

import com.jeffpeng.jmod.validator.Validator;

public class ToolDataDescriptor {
	public String toolclass;
	public String toolmat;
	public boolean hasmodes = false;
	public Integer durability;
	public boolean unbreakable = false;
	
	public ToolDataDescriptor(String toolmat){
		this.toolmat = toolmat;
		
		Validator.msg(this.getClass().getSimpleName() + " OK.");
	}
	
	public ToolDataDescriptor durability(Integer durability){
		this.durability = durability;
		return this;
	}
	
	public ToolDataDescriptor toolclass(String toolclass){
		this.toolclass = toolclass;
		return this;
	}
	
	public ToolDataDescriptor unbreakable(boolean bool){
		this.unbreakable=bool;
		return this;
	}
	
	public ToolDataDescriptor hasModes(boolean bool){
		this.hasmodes = bool;
		return this;
	}
	
	
	

}
