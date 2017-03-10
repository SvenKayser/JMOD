package com.jeffpeng.jmod.actions;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;
import com.jeffpeng.jmod.types.fluids.CoreFluid;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class AddFluid extends BasicAction{
	
	private CoreFluid fluid;
	private boolean hasBucket = false;
	

	public AddFluid(JMODRepresentation owner,String fluidName) {
		super(owner);
		fluid = new CoreFluid(owner,fluidName);
	}
	
	public AddFluid viscosity(int v){					fluid.setViscosity(v);						return this;	}
	public AddFluid density(int d){						fluid.setDensity(d);						return this;	}
	public AddFluid temperature(int t){					fluid.setTemperature(Math.max(0, t));		return this;	}
	public AddFluid setColor(int r, int g, int b){		fluid.setColor(r, g, b);					return this;	}
	public AddFluid hasBucket(){						hasBucket = true;							return this;	}
	public AddFluid isGaseous(){						fluid.setGaseous(true); 					return this;	}
	public AddFluid isPoisonous(){						fluid.setPoisonous(true);					return this;	}
	
	@Override
	public boolean on(FMLPreInitializationEvent event){
		execute();
		return true;
	}
	
	@Override
	public void execute(){
		fluid.register();
		if(hasBucket) fluid.bucketize();
		
	}

}
