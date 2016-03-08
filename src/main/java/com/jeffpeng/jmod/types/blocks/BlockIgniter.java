package com.jeffpeng.jmod.types.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockIgniter extends Block {

	private int meta;
	private IIcon[] icons = new IIcon[6];
	private static String textureName = "si.igniter:blockIgniter";

	public BlockIgniter(int meta) {
		super(Material.iron);

		this.meta = meta;
		this.setHardness(1F);
		this.setStepSound(Block.soundTypeMetal);
		this.setBlockName("si.igniter.igniter_block");
		this.setBlockTextureName(textureName);
		if (this.meta == 1)
			this.setLightLevel(1F);
		else
			this.setLightLevel(0);
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return this.icons[side];
	}

	@Override
	public void registerBlockIcons(IIconRegister iconregister) {

		this.icons[0] = iconregister.registerIcon(textureName + "-bottom");
		this.icons[1] = iconregister.registerIcon(textureName + "-top");

		for (int i = 2; i < 6; i++) {
			this.icons[i] = iconregister.registerIcon(textureName + "-side");
		}

	}

	public void onBlockAdded(World world, int x, int y, int z) {
		if (!world.isRemote) {

			this.checkForIgnition(world, x, y, z);

		}
	}

	public void onNeighborBlockChange(World world, int x, int y, int z,
			Block p_149695_5_) {
		if (!world.isRemote) {
			world.scheduleBlockUpdate(x, y, z, this, 0);

		}
	}

	public void checkForIgnition(World world, int x, int y, int z) {
		if (world.isBlockIndirectlyGettingPowered(x, y, z)) {

			if (world.getBlock(x, y + 1, z) == Blocks.air) {
				world.setBlock(x, y + 1, z, Blocks.fire);
			}
		} else {

			if (world.getBlock(x, y + 1, z) == Blocks.fire) {
				world.setBlock(x, y + 1, z, Blocks.air);
			}

		}

	}

	public void updateTick(World world, int x, int y, int z, Random rand) {
		if (!world.isRemote) {

			checkForIgnition(world, x, y, z);

		}
	}

}
