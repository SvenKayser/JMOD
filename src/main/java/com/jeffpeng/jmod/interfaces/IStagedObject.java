package com.jeffpeng.jmod.interfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jeffpeng.jmod.JMOD;

import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.common.ProgressManager.ProgressBar;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLStateEvent;

@SuppressWarnings("deprecation")
public interface IStagedObject extends Comparable<IStagedObject> {
	

	public static void broadcast(FMLEvent event,List<IStagedObject> list){
		long start = System.currentTimeMillis();
		JMOD.LOG.info("[ObjectStager] Broadcasting "+event.getClass().getSimpleName() + " to Staged Objects");
		int c = 0;
		ProgressBar bar = ProgressManager.push("Staging ", list.size());
		
		for(IStagedObject o : list){
			try{
				if(event instanceof FMLConstructionEvent && o.on((FMLConstructionEvent)event)) c++;
				if(event instanceof FMLPreInitializationEvent && o.on((FMLPreInitializationEvent)event)) c++;
				if(event instanceof FMLInitializationEvent && o.on((FMLInitializationEvent)event)) c++;
				if(event instanceof FMLPostInitializationEvent && o.on((FMLPostInitializationEvent)event)) c++;
				if(event instanceof FMLLoadCompleteEvent && o.on((FMLLoadCompleteEvent)event)) c++;
				if(event instanceof FMLServerStartedEvent && o.on((FMLServerStartedEvent)event)) c++;
			} catch (Throwable t){
				JMOD.LOG.error("Error staging an object: "+o.getClass().getSimpleName() + " at " + event.getClass().getSimpleName());
				t.printStackTrace();
			}
			
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bar.step(o.getClass().getSimpleName());
		}
		ProgressManager.pop(bar);
		JMOD.LOG.info("[ObjectStager] "+event.getClass().getSimpleName() + " Staged for " + c + " of " + list.size() + " Objects in " + (System.currentTimeMillis() - start) + " ms");
	}
	
	
	
	default boolean on(FMLConstructionEvent event){
		return false;
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
