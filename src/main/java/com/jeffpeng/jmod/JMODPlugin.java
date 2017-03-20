package com.jeffpeng.jmod;

import java.util.Map;

import com.jeffpeng.jmod.interfaces.IEventObject;

import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLModIdMappingEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;

public class JMODPlugin implements IEventObject{
	public JMODPlugin(JMODPluginContainer container){
	}
	
	@Override
	public void on(String trigger, Object callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean fire(String trigger) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void on(FMLConstructionEvent event) {
		
	}
	
	public void on(FMLPreInitializationEvent event) {
	
	}
	
	public void on(FMLInitializationEvent event) {
		
	}
	
	public void on(FMLPostInitializationEvent event) {
		
	}
	
	public void on(FMLLoadCompleteEvent event) {
		
	}
	
	public void on(FMLServerStartedEvent event) {
		
	}
	
	public void on(FMLServerAboutToStartEvent event) {
	
	}
	
	public void on(FMLModIdMappingEvent event){
		
	}

	public void initConfig(Map<String, Object> config) {
		// TODO Auto-generated method stub
		
	}
	

}
