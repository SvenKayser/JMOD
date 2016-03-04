package com.jeffpeng.si.core;

import java.io.File;
import java.net.MalformedURLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import com.jeffpeng.si.core.crafting.SIAnvilHandler;
import com.jeffpeng.si.core.crafting.SIDropHandler;
import com.jeffpeng.si.core.crafting.SIToolRepairRecipe;
import com.jeffpeng.si.core.crafting.SIToolUnbreaker;
import com.jeffpeng.si.core.interfaces.ISIStagedObject;
import com.jeffpeng.si.core.interfaces.ISIExecutableObject;
import com.jeffpeng.si.core.modintegration.decocraft.DecoCraftDyeFix;
import com.jeffpeng.si.core.registry.SIRegistry;
import com.jeffpeng.si.core.scripting.SIScript;
import com.jeffpeng.si.core.tooltipper.ToolTipper;
import com.jeffpeng.si.core.util.Patcher;
import com.jeffpeng.si.core.util.descriptors.ArmorMaterialDescriptor;
import com.jeffpeng.si.core.util.descriptors.BlockDescriptor;
import com.jeffpeng.si.core.util.descriptors.CarvingVariationDescriptor;
import com.jeffpeng.si.core.util.descriptors.ItemDescriptor;
import com.jeffpeng.si.core.util.descriptors.ShapedRecipeDescriptor;
import com.jeffpeng.si.core.util.descriptors.ToolMaterialDescriptor;
import com.jeffpeng.si.core.util.descriptors.rotarycraft.GrinderRecipeDescriptor;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModClassLoader;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = SI.MODID, version = SI.VERSION)
public class SI {
	
	@SidedProxy(clientSide="com.jeffpeng.si.core.SIClientProxy", serverSide="com.jeffpeng.si.core.SIServerProxy")
	public static SIProxy proxy;
	public static boolean isServer = false;
	
	private static boolean devversion;
	
	public static final String MODID = "si.core";
	public static final String VERSION = "1.5.1";
	public static final Logger LOG = LogManager.getLogger("si.core"); 

	public static SIRegistry REGISTRY = new SIRegistry();
	public static CreativeTabs CREATIVETAB;
	public static SIConfig CONFIG;

	public SI() {
		
	}
	
	

	@SuppressWarnings("unused")
	@EventHandler
	public void construct(FMLConstructionEvent event) {
		File nashornjar = new File(System.getProperty("java.home") + "/lib/ext/nashorn.jar");
		
		try {
			((ModClassLoader)Loader.instance().getModClassLoader()).addFile(nashornjar);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SI.LOG.info("test");
		proxy.determineIfServer();
		CONFIG = new SIConfig();
		SIScript.init();
		SIScript.instance().evalScript("main.js");
		
		/*#MARK1*/if(false){
			devversion = true;
		} else {
			
			devversion = false;
		}
		

	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		CREATIVETAB = new SICreativeTab();
		broadcast(event);
		execute(BlockDescriptor.class);
		execute(ItemDescriptor.class);
		if(!devversion)SILib.checkDependencies();
		if(SI.CONFIG.runWorldGeneration)	REGISTRY.registerOreGeneration();
		
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		broadcast(event);
		proxy.registerCreativeTabEntries();
		MinecraftForge.EVENT_BUS.register(new ToolTipper());
		if(SI.CONFIG.useBuffedAnvilRepair)	MinecraftForge.EVENT_BUS.register(new SIAnvilHandler());
		if(SI.CONFIG.preventToolBreaking) 	MinecraftForge.EVENT_BUS.register(new SIToolUnbreaker());
		if(SI.CONFIG.alterBlockDrops)		MinecraftForge.EVENT_BUS.register(new SIDropHandler());
		

	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		broadcast(event);
		
		execute(ToolMaterialDescriptor.class);
		execute(ArmorMaterialDescriptor.class);
		if(SI.CONFIG.updateTools)			Patcher.patchTools();
		if(SI.CONFIG.updateArmor)			Patcher.patchArmor();
		if(SI.CONFIG.updateBlockProperties)	Patcher.patchBlockProperties();
		if(SI.CONFIG.precessChestLoot)		Patcher.processChestLoot();
		if(Loader.isModLoaded("chisel"))	execute(CarvingVariationDescriptor.class);
		if(SI.CONFIG.craftingGridToolRepair)
			GameRegistry.addRecipe(new SIToolRepairRecipe());
		if(Loader.isModLoaded("RotaryCraft") && SI.CONFIG.patchRotarycraftSteelTools)
			Patcher.patchRoCSteelTools();
		
		ISIExecutableObject.execute(GrinderRecipeDescriptor.class);
	}
	
	@EventHandler
	public void loadComplete(FMLLoadCompleteEvent event) {
		broadcast(event);
		if(SI.CONFIG.removeOreDictEntries)	Patcher.removeOreDictEntries();
		if(SI.CONFIG.purgeRecipes)			Patcher.purgeRecipes();
		if(SI.CONFIG.addOredictEntries)		REGISTRY.registerOreDict();
		execute(ShapedRecipeDescriptor.class);
		if(SI.CONFIG.addRecipes)			REGISTRY.registerRecipesToGame();
		if(Loader.isModLoaded("props"))		DecoCraftDyeFix.fix();
	}
	
	private void broadcast(Object event){
		if(event instanceof FMLPreInitializationEvent) 		ISIStagedObject.broadcast((FMLPreInitializationEvent)event); else
		if(event instanceof FMLInitializationEvent) 		ISIStagedObject.broadcast((FMLInitializationEvent)event); else
		if(event instanceof FMLPostInitializationEvent) 	ISIStagedObject.broadcast((FMLPostInitializationEvent)event); else
		if(event instanceof FMLLoadCompleteEvent) 			ISIStagedObject.broadcast((FMLLoadCompleteEvent)event); 
		
	}
	
	private void execute(@SuppressWarnings("rawtypes") Class clazz){
		ISIExecutableObject.execute(clazz);
		
	}
	
	@SubscribeEvent
	public void patchBlocksPropertiesHook(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityPlayer)
			Patcher.patchBlockProperties();

	}
	
	
}
