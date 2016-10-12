package com.jeffpeng.jmod.actions;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;
import com.jeffpeng.jmod.worldgeneration.ConfigurableOreGenerator;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class AddOreGeneration extends BasicAction {
	public String blocktogenerate;
	public String blocktoreplace;
	public int chancesperchunk;
	public int veinsize;
	public int dimension;
	public int starty;
	public int endy;
	public int spread;
	public int weight;
	public int seedoffset;
	
	private static int seedoffsetstatic = 1; 
	
	public AddOreGeneration(JMODRepresentation owner){
		super(owner);
		this.blocktoreplace = "minecraft:stone";
		this.blocktogenerate = "minecraft:gravel";
		this.veinsize = 6;
		this.starty = 0;
		this.endy = 128;
		this.chancesperchunk = 10;
		this.spread = 16;
		this.dimension = 0;
		this.weight = 5000;
		this.valid = true;
		this.seedoffset = seedoffsetstatic++;
	}
	
	public AddOreGeneration blockToGenerate(String blockgen){
		this.blocktogenerate = blockgen;
		return this;
	}
	
	public AddOreGeneration blockToReplace(String blockrepl){
		this.blocktoreplace = blockrepl;
		return this;
	}
	
	public AddOreGeneration chancesPerChunk(int chances){
		this.chancesperchunk = chances;
		return this;
	}
	
	public AddOreGeneration dimension(int dim){
		this.dimension = dim;
		return this;
	}
	
	public AddOreGeneration veinSize(int size){
		this.veinsize = size;
		return this;
	}
	
	public AddOreGeneration startY(int y){
		this.starty = y;
		return this;
	}
	
	public AddOreGeneration endY(int y){
		this.endy = y;
		return this;
	}
	
	public AddOreGeneration spread(int spread){
		this.spread = spread;
		return this;
	}
	
	public AddOreGeneration weight(int weight){
		this.weight = weight;
		return this;
	}
	
	
	
	@Override
	public boolean on(FMLInitializationEvent event){
		GameRegistry.registerWorldGenerator(new ConfigurableOreGenerator(this),this.weight);
		return true;
	}
}
