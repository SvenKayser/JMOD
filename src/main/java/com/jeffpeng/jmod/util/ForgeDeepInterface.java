package com.jeffpeng.jmod.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Table;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.eventbus.EventBus;
import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.JMODContainer;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameData;

public class ForgeDeepInterface {
	
	
	private GameData gameDataInstance;

	private List<ModContainer> modList = new ArrayList<ModContainer>();
	private Map<String, ModContainer> namedMods = new HashMap<String, ModContainer>();
	private LoadController modController;
	private List<ModContainer> activeModList = new ArrayList<ModContainer>();
	private Map<String,EventBus> eventChannels =  new HashMap<String,EventBus>();
	private boolean lockdown = false;
	
	private Class<Loader> loader = Loader.class;
	private Class<LoadController> loadcontroller = LoadController.class;
	
	private Method gameDataTrueRegisterItem;
	private Method gameDataTrueRegisterBlock;
	
	private Table<String, String, ItemStack> customItemStacks;
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ForgeDeepInterface(){
		try {
			Field gameDataInstanceField = GameData.class.getDeclaredField("mainData");
			gameDataInstanceField.setAccessible(true);
			gameDataInstance = (GameData) gameDataInstanceField.get(null);
			
			Field gameDataCustomItemStacks = GameData.class.getDeclaredField("customItemStacks");
			gameDataCustomItemStacks.setAccessible(true);
			customItemStacks = (Table<String, String, ItemStack>) gameDataCustomItemStacks.get(null);
			
			
			Class[] cArg = new Class[3];
			cArg[0] = Item.class;
			cArg[1] = String.class;
			cArg[2] = int.class;
			gameDataTrueRegisterItem = GameData.class.getDeclaredMethod("registerItem", cArg);
			gameDataTrueRegisterItem.setAccessible(true);
			
			cArg[0] = Block.class;
			gameDataTrueRegisterBlock = GameData.class.getDeclaredMethod("registerBlock", cArg);
			gameDataTrueRegisterBlock.setAccessible(true);
			
			//Forge guys gonna hate
			for(ModContainer entry : Loader.instance().getModList()){
				modList.add(entry);
			}
			
			for(Map.Entry<String, ModContainer> entry : Loader.instance().getIndexedModList().entrySet()){
				namedMods.put(entry.getKey(), entry.getValue());
			}
			
			Field a1 = loader.getDeclaredField("mods");
			a1.setAccessible(true);
			a1.set(Loader.instance(), Collections.unmodifiableList(modList));
			
			Field a2 = loader.getDeclaredField("namedMods");
			a2.setAccessible(true);
			a2.set(Loader.instance(), namedMods);
			
			Field a3 = loader.getDeclaredField("modController");
			a3.setAccessible(true);
			this.modController = (LoadController) a3.get(Loader.instance());
			
			Field a4 = loadcontroller.getDeclaredField("activeModList");
			a4.setAccessible(true);
			List<ModContainer> oldActiveModList = (List<ModContainer>) a4.get(this.modController);
			oldActiveModList.forEach((oldentry)->{this.activeModList.add(oldentry);});
			a4.set(this.modController, this.activeModList);
			
			Field a5 = loadcontroller.getDeclaredField("eventChannels");
			a5.setAccessible(true);
			ImmutableMap<String,EventBus> oldEventChannels = (ImmutableMap<String,EventBus>) a5.get(this.modController);
			

			oldEventChannels.forEach((modid,bus)->{
				this.eventChannels.put(modid, bus);
			});

		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public boolean isLocked(){
		return lockdown;
	}
	
	public void lockDown()
	{
		Builder<String, EventBus> eventBus = ImmutableMap.builder();
		this.eventChannels.forEach((modid, bus)->{
			eventBus.put(modid,bus);
		});
		
		ImmutableMap<String,EventBus> eventChannelsTemp = eventBus.build();
		
		Field a5 = null;
		try {
			a5 = loadcontroller.getDeclaredField("eventChannels");
			a5.setAccessible(true);
			a5.set(modController, eventChannelsTemp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.lockdown = true;
	}
	
	public void addMod(JMODContainer mc){
		if(!lockdown){
			modList.add(mc);
			namedMods.put(mc.getModId(),mc);
			FMLCommonHandler.instance().addModToResourcePack(mc);
			this.activeModList.add(mc);
			
			EventBus bus = new EventBus(mc.getModId());
			mc.registerBus(bus, modController);
			eventChannels.put(mc.getModId(), bus);
			//FMLCommonHandler.instance().addModToResourcePack(mc);
		} else {
			JMOD.LOG.error("It is too late to add any more mods.");
		}
		
	}
	
	public void registerItem(Item item, String name, String modId){
		if(JMOD.isDevVersion()) JMOD.LOG.info("DeepForgeRegisterItem of " + name);
		try {
			Object retint;
			retint = gameDataTrueRegisterItem.invoke(gameDataInstance, item, modId + ":" + name, -1);
			if(JMOD.isDevVersion()) JMOD.LOG.info("With ID " + retint);
			customItemStacks.put(modId, name, new ItemStack(item,0,0));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generat ed catch block
			e.printStackTrace();
		}
	}
	
	public void registerBlock(Block block, ItemBlock placer, String name, String modId){
		if(JMOD.isDevVersion()) JMOD.LOG.info("DeepForgeRegisterBlock of " + name);
		try {
			if(block == null) throw new RuntimeException("no block");
			if(name == null) throw new RuntimeException("no name");
			if(modId == null) throw new RuntimeException("no modid");
			
			Object retint;
			retint = gameDataTrueRegisterItem.invoke(gameDataInstance, placer ,modId + ":" + name, -1);
			if(JMOD.isDevVersion()) JMOD.LOG.info("With ID " + retint);
			gameDataTrueRegisterBlock.invoke(gameDataInstance, block, modId + ":" + name, -1);
			customItemStacks.put(modId, name, new ItemStack(block,0,0));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generat ed catch blocke
			e.printStackTrace();
		}
	
	}
	
	public void registerBlock(Block block, String name, String modId){
		registerBlock(block,new ItemBlock(block),name,modId);
	}
	

	
}
