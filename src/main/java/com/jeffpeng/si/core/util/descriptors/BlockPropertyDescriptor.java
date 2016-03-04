package com.jeffpeng.si.core.util.descriptors;

import java.util.HashMap;
import java.util.Map;

public class BlockPropertyDescriptor {
	public Float hardness;
	public Float blastresistance;
	public Map<Integer,Integer> harvestlevel = new HashMap<>();
	public String tool;
	public Integer opacity;
	public String sound;
	public Float slipperiness;
	
	
	public BlockPropertyDescriptor hardness(Float hardness){
		this.hardness = hardness;
		return this;
	}
	
	public BlockPropertyDescriptor slipperiness(Float slipperiness){
		this.slipperiness = slipperiness;
		return this;
	}
	
	public BlockPropertyDescriptor harvestlevel(int meta, int harvestlevel){
		this.harvestlevel.put(meta, harvestlevel);
		return this;
	}
	
	public BlockPropertyDescriptor hl(int meta, int hl){
		return harvestlevel(meta,hl);
	}
	
	public BlockPropertyDescriptor harvestlevel(int harvestlevel){
		this.harvestlevel.put(0, harvestlevel);
		return this;
	}
	
	public BlockPropertyDescriptor hl(int hl){
		return harvestlevel(hl);
	}
	
	public BlockPropertyDescriptor tool(String tool){
		this.tool = tool;
		return this;
	}
	
	public BlockPropertyDescriptor sound(String sound){
		this.sound = sound;
		return this;
		
	}
	
	public BlockPropertyDescriptor opacity(int opacity){
		this.opacity = opacity;
		return this;
		
	}
	
	public BlockPropertyDescriptor blastresistance(float blastresistance){
		this.blastresistance = blastresistance;
		return this;
	}
	
	public BlockPropertyDescriptor br(float br){
		return blastresistance(br);
	}
	
}
