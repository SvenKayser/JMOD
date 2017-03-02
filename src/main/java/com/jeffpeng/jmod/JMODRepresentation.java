package com.jeffpeng.jmod;

import java.util.ArrayList;
import java.util.List;

import javax.script.Bindings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.MinecraftForge;

import com.jeffpeng.jmod.actions.AddChestLoot;
import com.jeffpeng.jmod.actions.AddShapedRecipe;
import com.jeffpeng.jmod.actions.AddShapelessRecipe;
import com.jeffpeng.jmod.actions.AddSmeltingRecipe;
import com.jeffpeng.jmod.actions.RemoveChestLoot;
import com.jeffpeng.jmod.actions.RemoveRecipe;
import com.jeffpeng.jmod.actions.RemoveSmeltingRecipe;
import com.jeffpeng.jmod.actions.SetBlockProperties;
import com.jeffpeng.jmod.crafting.AnvilHandler;
import com.jeffpeng.jmod.crafting.DropHandler;
import com.jeffpeng.jmod.crafting.ToolRepairRecipe;
import com.jeffpeng.jmod.interfaces.IEventObject;
import com.jeffpeng.jmod.interfaces.IItem;
import com.jeffpeng.jmod.interfaces.IBlock;
import com.jeffpeng.jmod.interfaces.IStagedObject;
import com.jeffpeng.jmod.interfaces.IExecutableObject;
import com.jeffpeng.jmod.modintegration.rotarycraft.PatchRoCSteelTools;
import com.jeffpeng.jmod.primitives.JMODInfo;
import com.jeffpeng.jmod.registry.PlayerData;
import com.jeffpeng.jmod.scripting.JScript;
import com.jeffpeng.jmod.tooltipper.ToolTipper;
import com.jeffpeng.jmod.validator.Validator;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLModIdMappingEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class JMODRepresentation implements IEventObject {

	private JScript script;
	private Config config = new Config();
	private Lib lib;
	private JMODContainer container;

	private Logger log;
	private JMODInfo modinfo;

	private boolean zipMod = false;
	private JMODRepresentation instance = this;
	private boolean scriptingFinished = false;
	private boolean scriptingErrored = false;
	private PlayerData playerData;
	
	protected List<IStagedObject> stageables = new ArrayList<>();

	public JMODRepresentation(JMODInfo modinfo) {
		this(modinfo, false);
	}

	public JMODRepresentation(JMODInfo modinfo, boolean zipmod) {
		this.zipMod = zipmod;
		this.modinfo = modinfo;
		this.log = LogManager.getLogger("" + modinfo.modid);
		this.lib = new Lib(this);

	}

	public void runScripts() {
		log.info("Scripts for " + this.getModId());
		script = new JScript(instance);
		for (String entry : modinfo.scripts) {
			script.evalScript(entry);
		}
		scriptingFinished = true;
	}

	public JMODInfo getModInfo() {
		return this.modinfo;
	}
	

	public void on(FMLConstructionEvent event) {

	}

	public void on(FMLPreInitializationEvent event) {
		if (!JMOD.isDevVersion())
			lib.checkDependencies();
	}

	public void on(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new ToolTipper(this));
		if (config.enhancedAnvilRepair)
			MinecraftForge.EVENT_BUS.register(new AnvilHandler(this));
		if (config.blockDrops.size() > 0)
			MinecraftForge.EVENT_BUS.register(new DropHandler(this));
	}

	public void on(FMLPostInitializationEvent event) {
		if (Loader.isModLoaded("RotaryCraft") && config.patchRotarycraftSteelTools)
			PatchRoCSteelTools.patchRoCSteelTools();
		if (config.craftingGridToolRepair)
			GameRegistry.addRecipe(new ToolRepairRecipe(this));

	}

	public void on(FMLLoadCompleteEvent event) {
		IExecutableObject.execute(RemoveRecipe.class,			stageables);
		IExecutableObject.execute(AddShapedRecipe.class,		stageables);
		IExecutableObject.execute(AddShapelessRecipe.class,		stageables);
		IExecutableObject.execute(RemoveSmeltingRecipe.class,	stageables);
		IExecutableObject.execute(AddSmeltingRecipe.class,		stageables);
		IExecutableObject.execute(RemoveChestLoot.class,		stageables);
		IExecutableObject.execute(AddChestLoot.class,			stageables);

	}
	
	public void on(FMLServerAboutToStartEvent event) {
		
	}
	
	public void on(FMLModIdMappingEvent event){
		IExecutableObject.execute(SetBlockProperties.class,		stageables);
	}

	protected void setContainer(JMODContainer container) {
		this.container = container;
	}

	public JMODContainer getContainer() {
		return this.container;
	}

	public String getModId() {
		return modinfo.modid;
	}

	public String getModName() {
		return modinfo.name;
	}

	public String getVersion() {
		return modinfo.version;
	}

	public Config getConfig() {
		return config;
	}

	public Lib getLib() {
		return lib;
	}

	public Logger getLogger() {
		return log;
	}

	public boolean isZipMod() {
		return zipMod;
	}

	public boolean testForMod(String modId) {
		if (!Validator.isValidator && !JMODLoader.isModLoaded(modId)) {
			log.warn(this.getModName() + " tries to do something that requires " + modId
					+ " to be loaded - but it isn't. This is either an error or lazy scripting.)");
			return false;
		}
		return true;
	}

	public boolean isScriptingFinished() {
		return scriptingFinished;
	}
	
	public void markScriptsAsErrored(){
		this.scriptingErrored = true;
	}
	
	public boolean hasScriptErrored(){
		return scriptingErrored;
	}
	
	public void registerItem(IItem item){
		JMOD.DEEPFORGE.registerItem(item);
	}
	
	public void registerBlock(IBlock block){
		JMOD.DEEPFORGE.registerBlock(block);
	}
	
	public void registerBlock(IBlock block, ItemBlock placer){
		JMOD.DEEPFORGE.registerBlock(block,placer);
	}
	
	public void registerStagedObject(IStagedObject o){
		stageables.add(o);
	}

	@Override
	public void on(String trigger, Object callback){
		
	}
	
	@Override
	public boolean fire(String trigger) {
		return false;
	}

}
