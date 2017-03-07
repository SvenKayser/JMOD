package com.jeffpeng.jmod.chisel.actions;

import net.minecraft.block.Block;

import com.cricketcraft.chisel.api.carving.CarvingUtils;
import com.cricketcraft.chisel.api.carving.CarvingUtils.SimpleCarvingVariation;
import com.cricketcraft.chisel.api.carving.ICarvingVariation;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class AddCarvingVariation extends BasicAction  {
	public String groupString;
	public String variationString;
	public ICarvingVariation variation;
	public int meta;
	private boolean valid = false;
	
	public AddCarvingVariation(JMODRepresentation owner, String group, String variation) {
		super(owner);
		this.groupString = group;
		this.variationString = variation;
	}
	
	@Override
	public boolean on(FMLPostInitializationEvent event){
		
		String[] split = variationString.split(":");
		meta = 0;
		if(split.length == 3)
		{
			if(split[2].equals("*")) meta = -1;
			else meta = Integer.parseInt(split[2]);
		}
		
		Block block = GameRegistry.findBlock(split[0], split[1]);
		log.info("start variation " + variationString);
		if(block == null){
			log.info("Cannot create carving variation for " + variationString + " since it does not exist. Duh.");
			return false;
		}
		
		variation = new SimpleCarvingVariation(block, meta, meta);
		valid = true;
		execute();
		return true;
	}

	@Override
	public boolean isValid() {
		return valid;
	}

	@Override
	public void execute() {
		if(!valid) return;
		AddCarvingGroup groupDesc = AddCarvingGroup.get(groupString);
		if(groupDesc == null) return;
		CarvingUtils.getChiselRegistry().addVariation(groupDesc.getGroup().getName(), variation);
	}
	

}
