package com.jeffpeng.jmod.types.blocks;

import java.util.ArrayList;
import java.util.List;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.annotations.StripMissingInterfaces;

import net.minecraft.block.material.Material;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import Reika.ReactorCraft.API.NeutronShield;
import Reika.RotaryCraft.API.Interfaces.Transducerable;

@StripMissingInterfaces
public class BlockCdInAg extends CoreBlock implements NeutronShield, Transducerable {

	public BlockCdInAg(JMODRepresentation owner,Material mat) {
		super(owner, mat);
		setStepSound(soundTypeMetal);
	}
	
	@Override
	public double getAbsorptionChance(String arg0) {
		return 60;
	}

	@Override
	public double getRadiationSpawnMultiplier(World arg0, int arg1, int arg2, int arg3, String arg4) {
		return 0;
	}

	@Override
	public ArrayList<String> getMessages(World world, int x, int y, int z, int side) {
		ArrayList<String> messages = new ArrayList<>();
		messages.add(String.format(StatCollector.translateToLocal("info.jmod.neutronabsorption"),getAbsorptionChance(null)));
		return messages;
	}
}
