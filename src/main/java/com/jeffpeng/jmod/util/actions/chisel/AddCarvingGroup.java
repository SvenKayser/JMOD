package com.jeffpeng.jmod.util.actions.chisel;

import java.util.HashMap;
import java.util.Map;

import com.cricketcraft.chisel.api.carving.CarvingUtils.SimpleCarvingGroup;
import com.jeffpeng.jmod.interfaces.IStagedObject;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;

public class AddCarvingGroup implements IStagedObject {
	
	private static Map<String,AddCarvingGroup> map = new HashMap<>();
	
	public static AddCarvingGroup get(String name){
		if(map.containsKey(name))	return map.get(name); else return null;
	}
	
	private String name;
	private SimpleCarvingGroup group;
	
	public AddCarvingGroup(String name){
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
