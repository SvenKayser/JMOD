package com.jeffpeng.jmod;

import java.util.Map;

import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jeffpeng.jmod.asm.JMODClassTransformer;
import com.jeffpeng.jmod.asm.JMODObfuscationHelper;
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
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@SuppressWarnings("deprecation")
@MCVersion(value="1.7.10")
@TransformerExclusions(value={"com.jeffpeng.jmod.asm"})
public class JMOD implements IFMLLoadingPlugin {
	public static final String MODID = "jmod";
	public static final String VERSION = "@VERSION@";
	public static final String NAME = "Javascript MOD Loader";
	private static final GlobalConfig GLOBALCONFIG = new GlobalConfig();
	private static boolean isServer = false;
	private static boolean devversion = false;
	protected JMODModContainer modcontainer;

	public static ForgeDeepInterface DEEPFORGE;
	public static final JMODLoader LOADER = new JMODLoader();
	public static final Logger LOG = LogManager.getLogger("JMOD");
	private static JMODRepresentation runningMod;
	private static JMOD instance;

	public JMOD() {
		instance = this;
		JMODObfuscationHelper.init();
		LOADER.discoverMods();
	}

	@SuppressWarnings({ "unused"})
	public void on(FMLConstructionEvent event) {
		if(event.getSide().isServer()) this.isServer = true;
		if("@devversion@".equals("true")){devversion = true;}
		DEEPFORGE = new ForgeDeepInterface();
		LOADER.constructMods();
		DEEPFORGE.lockDown();
		
		ProgressBar bar = ProgressManager.push("Initializing JMODs", LOADER.getModList().size());
		for(Map.Entry<String,JMODContainer> entry : LOADER.getModList().entrySet()){
			bar.step(entry.getValue().getName());
			entry.getValue().getMod().on(event);
			modcontainer.meta.description += "\n    §f"+entry.getValue().getName()+"   §b"+entry.getValue().getVersion();
		}
		ProgressManager.pop(bar);
		IStagedObject.sort();
		broadcast(event);
		
		
	}

	
	public void on(FMLPreInitializationEvent event) {
		broadcast(event);
		runningMod = null;
	}

	
	public void on(FMLInitializationEvent event) {
		broadcast(event);
		if(GLOBALCONFIG.preventToolBreaking) 	MinecraftForge.EVENT_BUS.register(new ToolUnbreaker());
		if(Loader.isModLoaded("NotEnoughItem"))	new NEI_JMODConfig();
	}

	
	public void on(FMLPostInitializationEvent event) {
		broadcast(event);

		if(Loader.isModLoaded("RotaryCraft"))		execute(AddGrinderRecipeDescriptor.class);
		if(Loader.isModLoaded("chisel"))			execute(AddCarvingVariation.class);
		Lib.patchTools();
		Lib.patchArmor();
		
	}
	
	
	public void on(FMLLoadCompleteEvent event){
		
		broadcast(event);
		JMOD.LOG.info("##mark");
		execute(RemoveRecipe.class);
		execute(AddShapedRecipe.class);
		execute(AddShapelessRecipe.class);
		execute(AddSmeltingRecipe.class);
		execute(RemoveChestLoot.class);
		execute(AddChestLoot.class);
		if(Loader.isModLoaded("props"))			DecoCraftDyeFix.fix();
		
	}
	
	public void on(FMLServerStartedEvent event){
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
		if(event instanceof FMLLoadCompleteEvent) 			IStagedObject.broadcast((FMLLoadCompleteEvent)event); else
		if(event instanceof FMLServerStartedEvent)	 		IStagedObject.broadcast((FMLServerStartedEvent)event);
		
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
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[] { JMODClassTransformer.class.getName() };
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
