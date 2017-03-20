package com.jeffpeng.jmod.types.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

import com.jeffpeng.jmod.Defines.Sides;
import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;


public class DynamicBlock extends CoreBlock{
	
	private int power = 0;
	private boolean powered = false;
	private int poweredSides = Sides.NONE;

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
	public void processSettings(BasicAction settings) {
		if(settings.hasSetting("power"))		this.power	 = settings.getInt("power") & 15;
		if(settings.hasSetting("powered"))		this.powered = settings.getBoolean("powered");
		if(settings.hasSetting("poweredsides"))	this.poweredSides = settings.getInt("poweredsides");
	}

	

}
