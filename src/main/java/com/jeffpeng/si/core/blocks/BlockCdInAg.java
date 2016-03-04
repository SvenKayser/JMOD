package com.jeffpeng.si.core.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import Reika.ReactorCraft.API.NeutronShield;

public class BlockCdInAg extends SICoreBlock implements NeutronShield {

	public BlockCdInAg(Material mat) {
		super(mat);
		setStepSound(soundTypeMetal);
	}

	public double getAbsorptionChance(String arg0) {
		return 60;
	}

	@Override
	public double getRadiationSpawnMultiplier(World arg0, int arg1, int arg2, int arg3, String arg4) {
		// TODO Auto-generated method stub
		return 0;
	}
}
