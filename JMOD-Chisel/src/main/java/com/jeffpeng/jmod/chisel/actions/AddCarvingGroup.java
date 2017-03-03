package com.jeffpeng.jmod.chisel.actions;

import java.util.HashMap;
import java.util.Map;

import com.cricketcraft.chisel.api.carving.CarvingUtils.SimpleCarvingGroup;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;

public class AddCarvingGroup extends BasicAction {
	
	private static Map<String,AddCarvingGroup> map = new HashMap<>();
	
	public static AddCarvingGroup get(String name){
		if(map.containsKey(name))	return map.get(name); else return null;
	}
	
	private String name;
	private SimpleCarvingGroup group;
	
	public AddCarvingGroup(JMODRepresentation owner, String name){
		super(owner);
		this.name = name;
		
	}
	
	@Override
	public boolean on(FMLPostInitializationEvent event){
		this.group = new SimpleCarvingGroup(name);
		execute();
		return true;
	}
	
	public SimpleCarvingGroup getGroup(){
		return group;
	}

	@Override
	public boolean isValid() {
		return true;
	}
	
	@Override
	public void execute(){
		map.put(name, this);
	}
}
