package com.jeffpeng.jmod.descriptors;

import java.util.ArrayList;
import java.util.List;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.OwnedObject;
import com.jeffpeng.jmod.validator.Validator;

public class FoodDataDescriptor extends OwnedObject{
	public int hunger;
	public float saturation;
	public boolean wolffood;
	public boolean alwaysEdible;
	public List<BuffDescriptor> buffdata = new ArrayList<>();
	
	public FoodDataDescriptor(JMODRepresentation owner, int hunger, float saturation,boolean wolffood,boolean alwaysEdible){
		super(owner);
		this.hunger = hunger;
		this.saturation = saturation;
		this.wolffood = wolffood;
		this.alwaysEdible = alwaysEdible;
		
		Validator.msg(this.getClass().getSimpleName() + " OK.");
	}
	
	public FoodDataDescriptor buffdata(BuffDescriptor buffdata){
		this.buffdata.add(buffdata);
		return this;
	}
	
	public FoodDataDescriptor buffdata(String name, int duration, int level, int chance){
		this.buffdata.add( new BuffDescriptor(owner,name,duration,level,chance));
		return this;
	}
}
