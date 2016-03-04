package com.jeffpeng.si.core.blocks.placers;



import com.jeffpeng.si.core.SI;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public class SIMetalBlockPlacer extends ItemBlockWithMetadata {

	public SIMetalBlockPlacer(Block block){
		super(block,block);
	}
	
	public String getUnlocalizedName(ItemStack stack) {
		return "tile.si.core." + SI.REGISTRY.blockyfy(SI.CONFIG.metalblocks.get(stack.getItemDamage()));
	}
	
}
