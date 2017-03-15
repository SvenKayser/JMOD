package com.jeffpeng.jmod;

import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.fluids.Fluid;

import com.jeffpeng.jmod.interfaces.IEventObject;
import com.jeffpeng.jmod.types.items.CoreBucket;

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
	
	public void initConfig(Map<String,Object> config){
		
	}
	
	public ItemStack getRepairItemStack(Item item){
		return null;
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
	

}
