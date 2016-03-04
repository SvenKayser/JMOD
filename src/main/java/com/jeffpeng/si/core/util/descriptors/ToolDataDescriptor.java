package com.jeffpeng.si.core.util.descriptors;

public class ToolDataDescriptor {
	public String toolclass;
	public String toolmat;
	public boolean hasmodes = false;
	public Integer durability;
	public boolean unbreakable = false;
	
	public ToolDataDescriptor(String toolmat){
		this.toolmat = toolmat;
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
