package com.jeffpeng.jmod.interfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jeffpeng.jmod.JMOD;

import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.common.ProgressManager.ProgressBar;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;

@SuppressWarnings("deprecation")
public interface IStagedObject extends Comparable<IStagedObject> {
	
	public static List<IStagedObject> list = new ArrayList<>();
	
	public static void sort(){
		Collections.sort(list);
	}
	
	
	public static void broadcast(FMLPreInitializationEvent event){
		long start = System.currentTimeMillis();
		JMOD.LOG.info("[ObjectStager] Broadcasting FMLPreInitializationEvent to ISIStagedObjects");
		int c = 0;
		ProgressBar bar = ProgressManager.push("Staging PreInitialization", list.size());
		for(IStagedObject o : list){
			if(o.on(event)) c++;
			bar.step(o.getClass().getSimpleName());
		}
		ProgressManager.pop(bar);
		JMOD.LOG.info("[ObjectStager] FMLPreInitializationEvent Staged for " + c + " of " + list.size() + " Objects in " + (System.currentTimeMillis() - start) + " ms");
	}
	
	
	public static void broadcast(FMLInitializationEvent event){
		long start = System.currentTimeMillis();
		JMOD.LOG.info("[ObjectStager] Broadcasting FMLInitializationEvent to ISIStagedObjects");
		int c = 0;
		ProgressBar bar = ProgressManager.push("Staging Initialization", list.size());
		for(IStagedObject o : list){
			if(o.on(event)) c++;
			bar.step(o.getClass().getSimpleName());
		}
		ProgressManager.pop(bar);
		JMOD.LOG.info("[ObjectStager] FMLInitializationEvent Staged for " + c + " of " + list.size() + " Objects in " + (System.currentTimeMillis() - start) + " ms");
	}

	
	public static void broadcast(FMLPostInitializationEvent event){
		long start = System.currentTimeMillis();
		JMOD.LOG.info("[ObjectStager] Broadcasting FMLPostInitializationEvent to ISIStagedObjects");
		int c = 0;
		ProgressBar bar = ProgressManager.push("Staging PostInitialization", list.size());
		for(IStagedObject o : list){
			if(o.on(event)) c++;
			bar.step(o.getClass().getSimpleName());
		}
		ProgressManager.pop(bar);
		JMOD.LOG.info("[ObjectStager] FMLPostInitializationEvent Staged for " + c + " of " + list.size() + " Objects in " + (System.currentTimeMillis() - start) + " ms");
	}
	
	
	public static void broadcast(FMLLoadCompleteEvent event){
		long start = System.currentTimeMillis();
		JMOD.LOG.info("[ObjectStager] Broadcasting FMLLoadCompleteEvent to ISIStagedObjects");
		int c = 0;
		ProgressBar bar = ProgressManager.push("Staging LoadComplete", list.size());
		for(IStagedObject o : list){
			if(o.on(event)) c++;
			bar.step(o.getClass().getSimpleName());
		}
		ProgressManager.pop(bar);
		JMOD.LOG.info("[ObjectStager] LoadComplete Staged for " + c + " of " + list.size() + " Objects in " + (System.currentTimeMillis() - start) + " ms");
	}
	
	public static void broadcast(FMLServerStartedEvent event){
		long start = System.currentTimeMillis();
		JMOD.LOG.info("[ObjectStager] Broadcasting FMLLoadCompleteEvent to ISIStagedObjects");
		int c = 0;
		ProgressBar bar = ProgressManager.push("Staging LoadComplete", list.size());
		for(IStagedObject o : list){
			if(o.on(event)) c++;
			bar.step(o.getClass().getSimpleName());
		}
		ProgressManager.pop(bar);
		JMOD.LOG.info("[ObjectStager] FMLLoadCompleteEvent Staged for " + c + " of " + list.size() + " Objects in " + (System.currentTimeMillis() - start) + " ms");
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
	
	default boolean on(FMLServerStartedEvent event){
		return false;
	}
	
	
	
	default int priority(){
		return 0;
	}
	
	default int compareTo(IStagedObject toCompare) {
		return toCompare.priority() - this.priority();
	}
	
	public boolean isValid();
	
	
}
