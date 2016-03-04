package com.jeffpeng.si.core.util.descriptors;

import java.util.HashMap;
import java.util.Map;

import com.cricketcraft.chisel.api.carving.CarvingUtils.SimpleCarvingGroup;
import com.jeffpeng.si.core.interfaces.ISIStagedObject;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;

public class CarvingGroupDescriptor implements ISIStagedObject {
	
	private static Map<String,CarvingGroupDescriptor> map = new HashMap<>();
	
	public static CarvingGroupDescriptor get(String name){
		if(map.containsKey(name))	return map.get(name); else return null;
	}
	
	private String name;
	private SimpleCarvingGroup group;
	
	public CarvingGroupDescriptor(String name){
		registerAsStaged();
		map.put(name, this);
		this.name = name;
	}
	
	@Override
	public boolean on(FMLPostInitializationEvent event){
		this.group = new SimpleCarvingGroup(name);
		return true;
	}
	
	public SimpleCarvingGroup getGroup(){
		return group;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}
