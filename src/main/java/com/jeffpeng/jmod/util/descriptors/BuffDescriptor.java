package com.jeffpeng.jmod.util.descriptors;

import net.minecraft.potion.Potion;

import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.interfaces.IStagedObject;
import com.jeffpeng.jmod.registry.BuffRegistry;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;


public class BuffDescriptor implements IStagedObject {
	public String name;
	public int duration;
	public int level;
	public int chance;
	private boolean valid = false;
	private Potion buff;
	
	
	public BuffDescriptor(String name, int duration, int level, int chance){
		registerAsStaged();
		this.name = name;
		this.duration = duration;
		this.level = level;
		this.chance = chance;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event){
		buff =	BuffRegistry.getInstance().getBuff(name);
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
