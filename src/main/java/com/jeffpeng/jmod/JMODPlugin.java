package com.jeffpeng.jmod;

import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

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
	
	public void initConfig(Map<String,Object> config){
		
	}
	
	public ItemStack getRepairItemStack(Item item){
		return null;
	}
	
	public Float getRepairAmount(Item item){
		return null;
	}
	
	public boolean isTool(Item item){
		return false;
	}
	
	public void addBuffs(Map<String,Potion> buffMap){
		
	}
	
	public boolean patchTool(Item item, String itemname){
		return false;
	}
	
	
	public boolean updateToolMaterial(Item item, ToolMaterial toolmat){
		return false;
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
	
	public static boolean isToolCycle(Item item){
		for(Map.Entry<String, JMODPluginContainer> entry : JMODLoader.getPluginList().entrySet()){
			if(entry.getValue().getInstance().isTool(item)) return true;
		}
		return false;
	}
	
	public static Float getRepairAmountCycle(Item item){
		for(Map.Entry<String, JMODPluginContainer> entry : JMODLoader.getPluginList().entrySet()){
			Float f = entry.getValue().getInstance().getRepairAmount(item);
			if(f != null) return f;
		}
		return null;
	}
	
	public static ItemStack getRepairItemStackCycle(Item item){
		for(Map.Entry<String, JMODPluginContainer> entry : JMODLoader.getPluginList().entrySet()){
			ItemStack is = entry.getValue().getInstance().getRepairItemStack(item);
			if(is != null) return is;
		}
		return null;
	}
	
	public static void addBuffsCycle(Map<String,Potion> buffMap){
		for(Map.Entry<String, JMODPluginContainer> entry : JMODLoader.getPluginList().entrySet()){
			entry.getValue().getInstance().addBuffs(buffMap);
		}
	}
	
	public static boolean patchToolCycle(Item item,String itemname){
		for(Map.Entry<String, JMODPluginContainer> entry : JMODLoader.getPluginList().entrySet()){
			if(entry.getValue().getInstance().patchTool(item,itemname)) return true;
		}
		return false;
	}
	
	public static boolean updateToolMaterialCycle(Item item,ToolMaterial toolmat){
		for(Map.Entry<String, JMODPluginContainer> entry : JMODLoader.getPluginList().entrySet()){
			if(entry.getValue().getInstance().updateToolMaterial(item,toolmat)) return true;
		}
		return false;
	}
	
}
