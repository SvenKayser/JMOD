package com.jeffpeng.jmod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jeffpeng.jmod.API.JMODAPI;
import com.jeffpeng.jmod.asm.JMODAnnotationParser;
import com.jeffpeng.jmod.asm.JMODClassTransformer;
import com.jeffpeng.jmod.asm.JMODObfuscationHelper;
import com.jeffpeng.jmod.asm.annotionhandlers.InjectInterfaceHandler;
import com.jeffpeng.jmod.asm.annotionhandlers.StripMissingInterfacesHandler;
import com.jeffpeng.jmod.crafting.BlacklistCraftingResults;
import com.jeffpeng.jmod.crafting.ToolUnbreaker;
import com.jeffpeng.jmod.descriptors.ItemStackDescriptor;
import com.jeffpeng.jmod.interfaces.IAnnotationHandler;
import com.jeffpeng.jmod.primitives.ModScriptObject;
import com.jeffpeng.jmod.registry.BlockMaterialRegistry;
import com.jeffpeng.jmod.util.CreativeTabEntryManager;
import com.jeffpeng.jmod.util.ForgeDeepInterface;
import com.jeffpeng.jmod.util.MiscHandlers;
import com.jeffpeng.jmod.util.ModId;

import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.common.ProgressManager.ProgressBar;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;

@SuppressWarnings("deprecation")
@MCVersion(value="1.7.10")
//@TransformerExclusions(value={"com.jeffpeng.jmod.asm"})
public class JMOD implements IFMLLoadingPlugin {
	private static List<String> classTransformers = new ArrayList<>();
	static
	{
		IAnnotationHandler.register(new InjectInterfaceHandler());
		IAnnotationHandler.register(new StripMissingInterfacesHandler());
	}
	
	
	
	public static Map<String,Class<? extends ModScriptObject>> modScriptList = new HashMap<>(); 

	public static final String MODID = "jmod";
	public static final String VERSION = "@VERSION@";
	public static final String NAME = "JMOD";
	public static final String ARCHIVEBASE ="com.jeffpeng.jmod";
	
	public static final Logger LOG = LogManager.getLogger("JMOD");
	public static final JMODLoader LOADER = JMODLoader.get();
	public static final EventBus BUS = new EventBus(); 
	public static final GlobalConfig GLOBALCONFIG = new GlobalConfig();
	public static final CreativeTabEntryManager CREATIVETABMANAGER = new CreativeTabEntryManager();

	private static boolean isServer = false;
	private static boolean devversion = ("@devversion@".equals("true"));
	protected JMODModContainer modcontainer;

	public static ForgeDeepInterface DEEPFORGE;
	
	
	private static JMOD instance;
	
	

	public JMOD() {
		instance = this;
		JMODAPI.BUS = BUS;
		JMOD.classTransformers.add(JMODAnnotationParser.class.getName());
		JMOD.classTransformers.add(JMODClassTransformer.class.getName());
		
		JMODObfuscationHelper.init();
		JMOD.LOADER.discoverPluginsAndMods();
		JMOD.LOADER.initPlugins();
		
		JMODLoader.markFMLModsDiscovered();
	}
	
	public void forgeLoaderHook(){
		
	}
	
	public void on(FMLConstructionEvent event) {
		if(event.getSide().isServer()) isServer = true;
		JMOD.LOADER.registerPluginsToEventBus();
		BlacklistCraftingResults.init();
		BlacklistCraftingResults.getInstance().blacklistDomain("RotaryCraft");
		DEEPFORGE = new ForgeDeepInterface();
		JMOD.LOADER.constructMods();
		JMOD.LOADER.inject();
		JMOD.LOADER.runScripts();
		ModId.init();

		ProgressBar bar = ProgressManager.push("Initializing JMODs", JMOD.LOADER.getModList().size());
		for(Map.Entry<String,JMODContainer> entry : JMOD.LOADER.getModList().entrySet()){
			bar.step(entry.getValue().getName());
			entry.getValue().getMod().on(event);
			modcontainer.meta.description += "\n    §f"+entry.getValue().getName()+"   §b"+entry.getValue().getVersion();
		}
		ProgressManager.pop(bar);
	}

	
	public void on(FMLPreInitializationEvent event) {
		BUS.register(CREATIVETABMANAGER);
		Lib.blockMaterialRegistry = new BlockMaterialRegistry();
	}

	
	public void on(FMLInitializationEvent event) {
		ItemStackDescriptor.markReady();
		MinecraftForge.EVENT_BUS.register(new MiscHandlers());
		
		if(GLOBALCONFIG.preventToolBreaking) 	MinecraftForge.EVENT_BUS.register(new ToolUnbreaker());
	}

	
	public void on(FMLPostInitializationEvent event) {
		Patcher.getInstance().patchTools();
		Patcher.getInstance().patchArmor();
	}
	
	
	public void on(FMLLoadCompleteEvent event){
		Patcher.getInstance().patchTools();
		Patcher.getInstance().patchArmor();
	}
	
	public void on(FMLServerStartedEvent event){}
	
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
	
	public static void addExtraClassTransforer(String classTransformer){
		JMOD.classTransformers.add(classTransformer);
	}
	
	@Override
	public String[] getASMTransformerClass() {
		return classTransformers.toArray(new String[classTransformers.size()]);
	}

	@Override
	public String getModContainerClass() {
		return JMODModContainer.class.getName();
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
 
		
	}

	@Override
	public String getAccessTransformerClass() {
		// TODO Auto-generated method stub
		return null;
	}
	

	

	
}
