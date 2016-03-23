package com.jeffpeng.jmod.descriptors;

import net.minecraft.potion.Potion;

import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.interfaces.IStagedObject;
import com.jeffpeng.jmod.primitives.OwnedObject;
import com.jeffpeng.jmod.registry.BuffRegistry;
import com.jeffpeng.jmod.validator.Validator;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;


public class BuffDescriptor extends OwnedObject implements IStagedObject {
	public String name;
	public int duration;
	public int level;
	public int chance;
	private boolean valid = false;
	private Potion buff;
	
	
	public BuffDescriptor(JMODRepresentation owner, String name, int duration, int level, int chance){
		super(owner);
		owner.registerStagedObject(this);
		this.name = name;
		this.duration = duration;
		this.level = level;
		this.chance = chance;
		
		Validator.msg(this.getClass().getSimpleName() + " OK.");
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event){
		buff =	BuffRegistry.getBuff(name);
		if(buff != null) valid = true; else {
			JMOD.LOG.warn("The buff (potion) " + name + " is unknown to the BuffRegistery. Whereever this is applied it will not work.");
		}
		
		return valid;
		
	}

	@Override
	public boolean isValid() {
		return valid;
	}
	
	public Potion getBuff(){
		return buff;
		
	}

}
