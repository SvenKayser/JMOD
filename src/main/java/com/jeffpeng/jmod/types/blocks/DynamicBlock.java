package com.jeffpeng.jmod.types.blocks;

import java.util.Map;

import javax.script.Bindings;

import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import Reika.ReactorCraft.API.NeutronShield;
import Reika.RotaryCraft.API.Interfaces.SurrogateBedrock;

import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib.SIDES;
import com.jeffpeng.jmod.interfaces.ISettingsProcessor;
import com.jeffpeng.jmod.interfaces.ISettingsReceiver;


public class DynamicBlock extends CoreBlock implements SurrogateBedrock, NeutronShield{
	
	private static final double MAXIMUM_NEUTRON_ABSORPTION_CHANCE = 0.9;
	
	private int power = 0;
	private boolean powered = false;
	private int poweredSides = SIDES.NONE;
	private boolean surrogateBedrock = false;
	private float bedrockDustYield = 100;
	private double neutronAbsorptionChance = 0;
	private boolean spawnsRadiation = true;

	public DynamicBlock(JMODRepresentation owner, Material mat) {
		super(owner, mat);
	}
	
	@Override
    public boolean canProvidePower()
    {
        return powered;
    }
	
	@Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
    {
		JMOD.LOG.info(""+ (1<<poweredSides) + " " + side);
		
		if(((1 << side) & poweredSides) > 0 && powered) return power;
		return 0;
    }   
	
	public void setPower(int power, int sides){
		this.power = power & 15;
		this.poweredSides = sides & 63;
	}
	
	public void setPowered(boolean powerable){
		this.powered = powerable;
	}
	
	

	@Override
	public void processSettings(ISettingsReceiver settings) {
		if(settings.hasSetting("fakeBedrock"))		this.surrogateBedrock	= settings.getBoolean("fakeBedrock");
		if(settings.hasSetting("bedrockDustYield")) this.bedrockDustYield 	= settings.getFloat("bedrockDustYield");
		if(settings.hasSetting("neutronAbsorptionChance"))
			this.neutronAbsorptionChance 	= Math.min(settings.getFloat("neutronAbsorptionChance"),MAXIMUM_NEUTRON_ABSORPTION_CHANCE);
		if(settings.hasSetting("spawnsRadiation"))	this.spawnsRadiation	= settings.getBoolean("spawnsRadiation");
		
	}

	@Override
	public double getAbsorptionChance(String type) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getRadiationSpawnMultiplier(World world, int x, int y, int z, String type) {
		if(this.spawnsRadiation) return 1; return 0;
	}

	@Override
	public boolean isBedrock(World world, int x, int y, int z) {
		return this.surrogateBedrock;
	}
	
	

	@Override
	public float getYield(World world, int x, int y, int z) {
 		return this.bedrockDustYield;
	}
	
	

}
