package com.jeffpeng.jmod.types.blocks.placers;

import com.jeffpeng.jmod.Config;
import com.jeffpeng.jmod.JMODRepresentation;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;

public class CoreBlockPlacer extends ItemBlockWithMetadata {
	
	protected JMODRepresentation owner;
	protected Config config;

	public CoreBlockPlacer(JMODRepresentation owner, Block block) {
		super(block, block);
		this.owner = owner;
		this.config = owner.getConfig();
 
	}

}
