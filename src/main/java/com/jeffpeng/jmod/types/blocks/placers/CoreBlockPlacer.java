package com.jeffpeng.jmod.types.blocks.placers;

import java.util.Map;

import com.jeffpeng.jmod.JMODRepresentation;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;

public class CoreBlockPlacer extends ItemBlockWithMetadata {
	
	protected JMODRepresentation owner;
	protected Map<String,Object> config;

	public CoreBlockPlacer(JMODRepresentation owner, Block block) {
		super(block, block);
		this.owner = owner;
		this.config = owner.getConfig();
 
	}

}
