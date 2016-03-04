package com.jeffpeng.si.core.util.descriptors;

import net.minecraft.potion.Potion;

import com.jeffpeng.si.core.interfaces.ISIStagedObject;
import com.jeffpeng.si.core.registry.SIBuffRegistry;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;

public class BuffDescriptor implements ISIStagedObject {
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
		buff =	SIBuffRegistry.getInstance().getBuff(name);
		if(buff != null) valid = true; else {
			FMLLog.warning("The buff (potion) " + name + " is unknown to the SIBuffRegistery. Whereever this is applied it will not work.");
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
