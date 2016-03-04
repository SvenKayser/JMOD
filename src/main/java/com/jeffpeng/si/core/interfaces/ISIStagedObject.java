package com.jeffpeng.si.core.interfaces;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public interface ISIStagedObject {
	
	public static List<ISIStagedObject> list = new ArrayList<>();
	
	public static void broadcast(FMLPreInitializationEvent event){
		FMLLog.info("[ObjectStager] Broadcasting FMLPreInitializationEvent to ISIStagedObjects");
		int c = 0;
		for(ISIStagedObject o : list) if(o.on(event)) c++;
		FMLLog.info("[ObjectStager] Staged " + c + " of " + list.size() + " Objects");
	}
	
	public static void broadcast(FMLInitializationEvent event){
		FMLLog.info("[ObjectStager] Broadcasting FMLInitializationEvent to ISIStagedObjects");
		int c = 0;
		for(ISIStagedObject o : list) if(o.on(event)) c++;
		FMLLog.info("[ObjectStager] Staged " + c + " of " + list.size() + " Objects");
	}

	public static void broadcast(FMLPostInitializationEvent event){
		FMLLog.info("[ObjectStager] Broadcasting FMLPostInitializationEvent to ISIStagedObjects");
		int c = 0;
		for(ISIStagedObject o : list) if(o.on(event)) c++;
		FMLLog.info("[ObjectStager] Staged " + c + " of " + list.size() + " Objects");
	}
	
	public static void broadcast(FMLLoadCompleteEvent event){
		FMLLog.info("[ObjectStager] Broadcasting FMLLoadCompleteEvent to ISIStagedObjects");
		int c = 0;
		for(ISIStagedObject o : list) if(o.on(event)) c++;
		FMLLog.info("[ObjectStager] Staged " + c + " of " + list.size() + " Objects");
	}

	
	default void registerAsStaged(){
		list.add(this);
	}
	
	default boolean on(FMLPreInitializationEvent event){
		return false;
	}
	
	default boolean on(FMLInitializationEvent event){
		return false;
	}
	
	default boolean on(FMLPostInitializationEvent event){
		return false;
	}
	
	default boolean on(FMLLoadCompleteEvent event){
		return false;
	}
	
	public boolean isValid();
	
	
}
