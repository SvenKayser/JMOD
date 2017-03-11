package com.jeffpeng.jmod.types.blocks.placers;



import java.util.ArrayList;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class MetalBlockPlacer extends CoreBlockPlacer {
	
	public MetalBlockPlacer(JMODRepresentation owner, Block block){
		super(owner,block);
	}
	
	@SuppressWarnings("unchecked")
	public String getUnlocalizedName(ItemStack stack) {
		
		return "tile."+ owner.getModId() + "." + Lib.blockyfy(((ArrayList<String>)config.get("metalblocks")).get(stack.getItemDamage()));
		
	}
	
	
}
