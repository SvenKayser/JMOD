package com.jeffpeng.jmod.types.blocks;

import com.jeffpeng.jmod.JMODRepresentation;

import net.minecraft.block.material.Material;

public class BlockPitchblende extends CoreBlock {

	public BlockPitchblende(JMODRepresentation owner,Material mat) {
		super(owner, mat);
		setStepSound(soundTypeMetal);
	}

}
