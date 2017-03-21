package com.jeffpeng.jmod;

import java.util.Map;

import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jeffpeng.jmod.asm.JMODAnnotationParser;
import com.jeffpeng.jmod.asm.JMODClassTransformer;
import com.jeffpeng.jmod.asm.JMODObfuscationHelper;
import com.jeffpeng.jmod.asm.annotionhandlers.InjectInterfaceHandler;
import com.jeffpeng.jmod.asm.annotionhandlers.StripMissingInterfacesHandler;
import com.jeffpeng.jmod.crafting.BlacklistCraftingResults;
import com.jeffpeng.jmod.crafting.ToolUnbreaker;

import com.jeffpeng.jmod.modintegration.applecore.AppleCoreModifyFoodValues;

import com.jeffpeng.jmod.interfaces.IAnnotationHandler;

import com.jeffpeng.jmod.modintegration.decocraft.DecoCraftDyeFix;
import com.jeffpeng.jmod.modintegration.nei.NEI_JMODConfig;
import com.jeffpeng.jmod.registry.BlockMaterialRegistry;
import com.jeffpeng.jmod.util.ForgeDeepInterface;


import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.common.ProgressManager.ProgressBar;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;

@SuppressWarnings("deprecation")
@MCVersion(value="1.7.10")
//@TransformerExclusions(value={"com.jeffpeng.jmod.asm"})
public class JMOD implements IFMLLoadingPlugin {
	static
	{
		IAnnotationHandler.register(new InjectInterfaceHandler());
		IAnnotationHandler.register(new StripMissingInterfacesHandler());
	}
	
	
	
	public static final Logger LOG = LogManager.getLogger("JMOD");

	
	public static final String MODID = "jmod";
	public static final String VERSION = "@VERSION@";
	public static final String NAME = "The JavaScript MOD Loader";
	private static final GlobalConfig GLOBALCONFIG = new GlobalConfig();
	private static boolean isServer = false;
	private static boolean devversion = ("@devversion@".equals("true"));
	protected JMODModContainer modcontainer;

	public static ForgeDeepInterface DEEPFORGE;
	private static JMOD instance;
	
	

	public JMOD() {
		instance = this;
		JMODObfuscationHelper.init();
		JMODLoader.discoverMods();
		
	}
	
	public void forgeLoaderHook(){
		
	}


	public void on(FMLConstructionEvent event) {
		if(event.getSide().isServer()) isServer = true;
		BlacklistCraftingResults.init();
		BlacklistCraftingResults.getInstance().blacklistDomain("RotaryCraft");
		DEEPFORGE = new ForgeDeepInterface();
		JMODLoader.constructMods();
		JMODLoader.inject();
		JMODLoader.runScripts();
		
		

		ProgressBar bar = ProgressManager.push("Initializing JMODs", JMODLoader.getModList().size());
		for(Map.Entry<String,JMODContainer> entry : JMODLoader.getModList().entrySet()){
			bar.step(entry.getValue().getName());
			entry.getValue().getMod().on(event);
			modcontainer.meta.description += "\n    §f"+entry.getValue().getName()+"   §b"+entry.getValue().getVersion();
		}
		ProgressManager.pop(bar);
	}

	
	public void on(FMLPreInitializationEvent event) {
		Lib.blockMaterialRegistry = new BlockMaterialRegistry();
	}

	
	public void on(FMLInitializationEvent event) {
		if(GLOBALCONFIG.preventToolBreaking) 	MinecraftForge.EVENT_BUS.register(new ToolUnbreaker());

		if(Loader.isModLoaded("NotEnoughItem"))	new NEI_JMODConfig();
		
		if(Loader.isModLoaded("AppleCore"))	{
			MinecraftForge.EVENT_BUS.register(AppleCoreModifyFoodValues.getInstance());
		}

		if(Loader.isModLoaded("NotEnoughItems"))	new NEI_JMODConfig();

	}

	
	public void on(FMLPostInitializationEvent event) {
		Lib.patchTools();
		Lib.patchArmor();
	}
	
	
	public void on(FMLLoadCompleteEvent event){
		if(Loader.isModLoaded("props"))			DecoCraftDyeFix.fix();
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
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[] { 
			JMODAnnotationParser.class.getName(),
			JMODClassTransformer.class.getName()
		};
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
