package com.jeffpeng.jmod.util.descriptors;

import java.util.ArrayList;
import java.util.List;

public class FoodDataDescriptor {
	public int hunger;
	public float saturation;
	public boolean wolffood;
	public boolean alwaysEdible;
	public List<BuffDescriptor> buffdata = new ArrayList<>();
	
	public FoodDataDescriptor(int hunger, float saturation,boolean wolffood,boolean alwaysEdible){
		this.hunger = hunger;
		this.saturation = saturation;
		this.wolffood = wolffood;
		this.alwaysEdible = alwaysEdible;
	}
	
	public FoodDataDescriptor buffdata(BuffDescriptor buffdata){
		this.buffdata.add(buffdata);
		return this;
	}
	
	public FoodDataDescriptor buffdata(String name, int duration, int level, int chance){
		this.buffdata.add( new BuffDescriptor(name,duration,level,chance));
		return this;
	}
}
