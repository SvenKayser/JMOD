package com.jeffpeng.jmod;

import java.util.Map;

import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jeffpeng.jmod.crafting.ToolUnbreaker;
import com.jeffpeng.jmod.interfaces.IExecutableObject;
import com.jeffpeng.jmod.interfaces.IStagedObject;
import com.jeffpeng.jmod.modintegration.decocraft.DecoCraftDyeFix;
import com.jeffpeng.jmod.modintegration.nei.NEI_JMODConfig;
import com.jeffpeng.jmod.util.ForgeDeepInterface;
import com.jeffpeng.jmod.util.actions.AddChestLoot;
import com.jeffpeng.jmod.util.actions.AddShapedRecipe;
import com.jeffpeng.jmod.util.actions.AddShapelessRecipe;
import com.jeffpeng.jmod.util.actions.AddSmeltingRecipe;
import com.jeffpeng.jmod.util.actions.RemoveChestLoot;
import com.jeffpeng.jmod.util.actions.RemoveRecipe;
import com.jeffpeng.jmod.util.actions.SetBlockProperties;
import com.jeffpeng.jmod.util.actions.chisel.AddCarvingVariation;
import com.jeffpeng.jmod.util.actions.rotarycraft.AddGrinderRecipeDescriptor;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.common.ProgressManager.ProgressBar;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;

@SuppressWarnings("deprecation")
@Mod(modid = JMOD.MODID, version = JMOD.VERSION, name = JMOD.NAME)
public class JMOD {
	@SidedProxy(clientSide="com.jeffpeng.jmod.ClientProxy", serverSide="com.jeffpeng.jmod.ServerProxy")
	public static Proxy proxy;
	public static final String MODID = "jmod";
	public static final String VERSION = "1.0alpha1";
	public static final String NAME = "Javascript MOD Loader";
	private static final GlobalConfig GLOBALCONFIG = new GlobalConfig();
	private static boolean isServer = false;
	private static boolean devversion;

	public static ForgeDeepInterface DEEPFORGE;
	public static final JMODLoader LOADER = new JMODLoader();
	public static final Logger LOG = LogManager.getLogger("JMOD");
	private static JMODRepresentation runningMod;
	private static JMOD instance;

	public JMOD() {
//		File nashornjar = new File(System.getProperty("java.home") + "/lib/ext/nashorn.jar");
//		try {
//			((ModClassLoader)Loader.instance().getModClassLoader()).addFile(nashornjar);
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		DEEPFORGE = new ForgeDeepInterface();
		LOADER.start();
	}

	@SuppressWarnings({ "unused"})
	@EventHandler
	public void construct(FMLConstructionEvent event) {
		proxy.determineIfServer();
		
		/*#MARK1*/if(false){
			devversion = true;
		} else {
			devversion = false;
		}
		
		
		
		ProgressBar bar = ProgressManager.push("Initializing JMODs", LOADER.getModList().size());
		for(Map.Entry<String,JMODContainer> entry : LOADER.getModList().entrySet()){
			bar.step(entry.getValue().getName());
			entry.getValue().getMod().on(event);
		}
		ProgressManager.pop(bar);
		IStagedObject.sort();
		broadcast(event);
		
		
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		broadcast(event);
		runningMod = null;
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		broadcast(event);
		if(GLOBALCONFIG.preventToolBreaking) 	MinecraftForge.EVENT_BUS.register(new ToolUnbreaker());
		runningMod = null;
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		broadcast(event);

		if(Loader.isModLoaded("RotaryCraft"))		execute(AddGrinderRecipeDescriptor.class);
		if(Loader.isModLoaded("chisel"))			execute(AddCarvingVariation.class);
		Lib.patchTools();
		Lib.patchArmor();
		runningMod = null;
	}
	
	@EventHandler
	public void loadComplete(FMLLoadCompleteEvent event){
		broadcast(event);
		execute(RemoveRecipe.class);
		execute(AddShapedRecipe.class);
		execute(AddShapelessRecipe.class);
		execute(AddSmeltingRecipe.class);
		execute(RemoveChestLoot.class);
		execute(AddChestLoot.class);
		
		
		if(Loader.isModLoaded("NotEnoughItem"))	new NEI_JMODConfig();
		if(Loader.isModLoaded("props"))			DecoCraftDyeFix.fix();
	}
	
	@EventHandler
	public void serverStarted(FMLServerStartedEvent event){
		execute(SetBlockProperties.class);
	}
	
	public static JMODRepresentation getRunningMod(){
		return runningMod;
	}
	
	public static String getRunningModId(){
		return runningMod.getModId();
	}
	
	public void execute(@SuppressWarnings("rawtypes") Class clazz){
		IExecutableObject.execute(clazz);
		
	}
	
	private void broadcast(Object event){
		if(event instanceof FMLPreInitializationEvent) 		IStagedObject.broadcast((FMLPreInitializationEvent)event); else
		if(event instanceof FMLInitializationEvent) 		IStagedObject.broadcast((FMLInitializationEvent)event); else
		if(event instanceof FMLPostInitializationEvent) 	IStagedObject.broadcast((FMLPostInitializationEvent)event); else
		if(event instanceof FMLLoadCompleteEvent) 			IStagedObject.broadcast((FMLLoadCompleteEvent)event); 
		
	}
	
	public static JMOD getInstance(){
		return instance;
	}
	
	public static GlobalConfig getGlobalConfig(){
		return GLOBALCONFIG;
	}
	
	public static boolean isServer(){
		return isServer;
	}
	
	public static void setIsServer(boolean value){
		isServer = value;
	}
	
	public static boolean isDevVersion(){
		return devversion;
	}
}
