package com.jeffpeng.si.core.util.descriptors;

public class OreGenerationDescriptor {
	public String blocktogenerate;
	public String blocktoreplace;
	public int chancesperchunk;
	public int veinsize;
	public int dimension;
	public int starty;
	public int endy;
	public int spread;
	

	
	public OreGenerationDescriptor(){
		this.blocktoreplace = "minecraft:stone";
		this.spread = 16;
		this.dimension = 0;
	}
	
	public OreGenerationDescriptor blockToGenerate(String blockgen){
		this.blocktogenerate = blockgen;
		return this;
	}
	
	public OreGenerationDescriptor blockToReplace(String blockrepl){
		this.blocktoreplace = blockrepl;
		return this;
	}
	
	public OreGenerationDescriptor chancesPerChunk(int chances){
		this.chancesperchunk = chances;
		return this;
	}
	
	public OreGenerationDescriptor dimension(int dim){
		this.dimension = dim;
		return this;
	}
	
	public OreGenerationDescriptor veinSize(int size){
		this.veinsize = size;
		return this;
	}
	
	public OreGenerationDescriptor startY(int y){
		this.starty = y;
		return this;
	}
	
	public OreGenerationDescriptor endY(int y){
		this.endy = y;
		return this;
	}
	
	public OreGenerationDescriptor spread(int spread){
		this.spread = spread;
		return this;
	}
}
