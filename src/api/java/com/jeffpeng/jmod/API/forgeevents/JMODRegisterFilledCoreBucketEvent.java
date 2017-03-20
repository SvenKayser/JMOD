package com.jeffpeng.jmod.API.forgeevents;

import net.minecraftforge.fluids.Fluid;

import com.jeffpeng.jmod.types.items.CoreBucket;

public class JMODRegisterFilledCoreBucketEvent extends JMODForgeEvent {
	
	public CoreBucket empty;
	public CoreBucket filled;
	public Fluid fluid;
	public int amount;
	
	public JMODRegisterFilledCoreBucketEvent(CoreBucket empty, CoreBucket filled, Fluid fluid, int amount){
		this.fluid = fluid;
		this.empty = empty;
		this.filled = filled;
		this.amount = amount;
		
	}
}
