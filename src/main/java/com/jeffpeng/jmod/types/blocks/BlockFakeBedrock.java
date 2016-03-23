package com.jeffpeng.jmod.types.blocks;

import com.jeffpeng.jmod.JMODRepresentation;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import Reika.RotaryCraft.API.Interfaces.SurrogateBedrock;

public class BlockFakeBedrock extends CoreBlock implements SurrogateBedrock {

	public BlockFakeBedrock(JMODRepresentation owner, Material mat) {
		super(owner, mat);
	}

	@Override
	public boolean isBedrock(World world, int x, int y, int z) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public float getYield(World world, int x, int y, int z) {
		// TODO Auto-generated method stub
		return 3;
	}

}
