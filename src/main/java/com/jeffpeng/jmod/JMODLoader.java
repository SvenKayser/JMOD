package com.jeffpeng.jmod;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.launchwrapper.LaunchClassLoader;

import com.jeffpeng.jmod.primitives.JMODInfo;
import com.jeffpeng.jmod.primitives.ModCreationException;
import com.jeffpeng.jmod.scripting.JScript;
import com.jeffpeng.jmod.util.LoaderUtil;
import com.jeffpeng.jmod.util.Reflector;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.FMLLaunchHandler;

public class JMODLoader {
	
	private static LaunchClassLoader lcl = (LaunchClassLoader) new Reflector((FMLLaunchHandler) new Reflector(null,FMLLaunchHandler.class).get("INSTANCE"),FMLLaunchHandler.class).get("classLoader");
	// ^ Yes, that happened.
	private static boolean FMLModsDiscovered = false;
	private static List<Path> modQueue = new ArrayList<>();
	private static List<Path> pluginQueue = new ArrayList<>();
	private static Map<String,JMODContainer> modList = new HashMap<String,JMODContainer>();
	private static final String MODSDIRECTORY = "mods";
	private static List<String> modids = new ArrayList<>();
	private static List<String> pluginids = new ArrayList<>();
	private static Object fmllock = new Object();
	private static Map<String,JMODPluginContainer> pluginList = new HashMap<>();
	
	public static Map<String,JMODPluginContainer> getPluginList(){
		return pluginList;
	}
	
	
	public static Map<String,JMODContainer> getModList(){
		return modList;
	}
	
	protected static void discoverPluginsAndMods(){
		if(JMOD.DEEPFORGE != null && JMOD.DEEPFORGE.isLocked()){
			throw new RuntimeException("Cannot discover any more plugins or mods at this point.");
		}
		try {
			Files.walk(Paths.get(MODSDIRECTORY),FileVisitOption.FOLLOW_LINKS).forEach(filePath -> {
			    if(isPluginOrMod(filePath) && !pluginQueue.contains(filePath)){
			    	pluginQueue.add(filePath);
			    	modQueue.add(filePath);
			    	JMOD.LOG.info("[JMODLoader Discoverer] discovered jmod(plugin) at " + filePath);
			    }
			});
		} catch (IOException e) {
			JMOD.LOG.warn("There was a rather unexpected error while discovering plugins and mods. Maybe you don't have rights over your mod directory?");
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	protected static void initPlugins(){
		if(JMOD.DEEPFORGE != null &&JMOD.DEEPFORGE.isLocked()){
			throw new RuntimeException("Cannot add any more plugins at this point.");
		}
		
		JMOD.LOG.info("Initializing " + pluginQueue.size() + " JMOD Plugins");
		
		for(Path entry : pluginQueue){
			String rawjson = LoaderUtil.loadPluginJson(entry);
			if(rawjson == null){
				JMOD.LOG.warn("[JMODLoader Plugins] Failed to load plugin declaration from " + entry.toString() + ". Probably not a plugin. Skipping.");
				continue;
			}
			JMOD.LOG.info("[JMODLoader Plugins] loaded plugin declaration " + entry.toString());
			
			JMODPluginContainer newPlugin = new JMODPluginContainer();
			newPlugin.info = LoaderUtil.parsePluginJson(rawjson);
			if(newPlugin.info == null){
				JMOD.LOG.warn("[JMODLoader Plugins] Failed to parse JSON from " + entry.toString() + "   Probably the JSON is malformed. This is an error of the mod's author. Skipping.");
				continue;
			}
			
			if(!LoaderUtil.pluginInfoDataSanity(newPlugin.info,entry.getFileName().toString())) continue;
			
			if(pluginids.contains(newPlugin.info.pluginid)){
				JMOD.LOG.warn("[JMODLoader Plugins] The plugin " + newPlugin.info.pluginid + " seems to be present more than once. Cannot load the same plugin twice. This is either an error of the ModPack creator or you - so fix it! Skipping.");
				continue;
			}
			
			JMOD.LOG.info("[JMODLoader Plugins] " + entry.toString() + " passed sanity check.");
			pluginids.add(newPlugin.info.pluginid);
			
			try {
				lcl.addURL(entry.toUri().toURL());
			} catch (MalformedURLException e) {
				JMOD.LOG.warn("failed to add " + entry.toString() +" to the classpath");
				e.printStackTrace();
				continue;
			}
			
			System.out.println("###jml " + newPlugin.info.scriptingobjects);
			System.out.println("###jml " + newPlugin.info.scriptingobjects.getClass());
			
			if(newPlugin.info.scriptingobjects != null) for(Map.Entry<String, String> soentry : newPlugin.info.scriptingobjects.entrySet()){
				JScript.addExtraScriptingObject(soentry.getKey(), newPlugin.info.archivebase + "." + (String) soentry.getValue());
			}
			
			if(newPlugin.info.classtransformers != null) for(String lientry : (List<String>) newPlugin.info.classtransformers){
				JMOD.addExtraClassTransforer(newPlugin.info.archivebase + "." + lientry);
			}
			
			@SuppressWarnings("rawtypes")
			Class[] args = new Class[1];
			args[0] = JMODPluginContainer.class;
			try {
				newPlugin.setInstance((JMODPlugin) Class.forName(newPlugin.info.archivebase + ".Plugin").getDeclaredConstructor(args).newInstance(newPlugin));
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
					| SecurityException | ClassNotFoundException e) {
				JMOD.LOG.info("[JMODLoader Plugins] The plugin " + newPlugin.info.pluginid + " apparently doesn't ship its own Plugin.class. That's okay, but not optimal. Going with the default.");
				newPlugin.setInstance(new JMODPlugin(newPlugin));
			}
			
			pluginList.put(newPlugin.info.pluginid,newPlugin);
			
		}
	}
	
	protected static void constructMods(){
		if(JMOD.DEEPFORGE != null &&JMOD.DEEPFORGE.isLocked()){
			throw new RuntimeException("Cannot add any more mods at this point.");
		}
		
		//ProgressBar bar = ProgressManager.push("Constructing JMODs", modQueue.size());
		
		JMOD.LOG.info("[JMODLoader Mods] Constructing " + modQueue.size() + " JMODs");
		
		for(Path entry : modQueue){
			//bar.step(entry.getFileName().toString());
			String rawjson = LoaderUtil.loadModJson(entry);
			if(rawjson == null){
				JMOD.LOG.warn("[JMODLoader Mods] Failed to load mod declaration from " + entry.toString() + ". Probably no jmod. Skipping.");
				continue;
			}
			JMOD.LOG.info("[JMODLoader Mods] loaded mod declaration " + entry.toString());
			
			JMODInfo configdata = LoaderUtil.parseModJson(rawjson);
			if(configdata == null){
				JMOD.LOG.warn("[JMODLoader Mods] Failed to parse JSON from " + entry.toString() + "   Probably the JSON is malformed. This is an error of the mod's author. Skipping.");
				continue;
			}
			JMODContainer newmod = null;
			
			if(!LoaderUtil.infoDataSanity(configdata,entry.getFileName().toString())) continue;
			
			if(modids.contains(configdata.modid)){
				JMOD.LOG.warn("[JMODLoader Mods] The mod " + configdata.modid + " seems to be present more than once. Cannot load the same mod twice. This is either an error of the ModPack creator or you - so fix it! Skipping.");
				continue;
			}
			
			JMOD.LOG.info("[JMODLoader Mods] " + entry.toString() + " passed sanity check.");
			modids.add(configdata.modid);
			
			try {
				newmod = new JMODContainer(new JMODRepresentation(configdata,!Files.isDirectory(entry)),entry.toFile());
			} catch (ModCreationException e){
				JMOD.LOG.warn("modCreationException " + entry.toString() +": " + e.getMessage());
				continue;
			}
			
			//newmod.getMod().runScripts();
			
			modList.put(newmod.getModId(),newmod);
			
		}
		//ProgressManager.pop(bar);		
		

	}
	
	protected static void inject(){
		for(Map.Entry<String,JMODContainer> entry : modList.entrySet()){
			JMOD.DEEPFORGE.addMod(entry.getValue());
			JMOD.LOG.info("[JMODLoader] successfully injected jmod \"" + entry.getKey()+"\"");
		}
		JMOD.DEEPFORGE.lockDown();
	}
	
	protected static void runScripts(){
		for(Map.Entry<String,JMODContainer> entry : modList.entrySet()){
			JMOD.LOG.info("'###rsc " + entry.getValue().getModId());
			entry.getValue().getMod().runScripts();
		}
	}
	
	private static boolean isPluginOrMod(Path path){
		if(path.toString().endsWith(".jmodplugin") || path.toString().endsWith(".jmod")){
			return true;
		}
		return false;
	}
	
	public static JMODRepresentation getMod(String modid){
		return modList.get(modid).getMod();
	}
	
	public static JMODContainer getModContainer(String modid){
		return modList.get(modid);
	}
	
	public static void markFMLModsDiscovered(){
		JMOD.LOG.info("markFMLModsDiscovered");
		FMLModsDiscovered = true;
		synchronized (fmllock) {
			fmllock.notifyAll();
		}
	}
	
	public static boolean isPluginLoaded(String pluginid){
		return pluginList.containsKey(pluginid);
	}
	
	public static boolean isModLoaded(String modid){
		if(FMLModsDiscovered)	return Loader.isModLoaded(modid);
		
		synchronized (fmllock) {
			long start = System.currentTimeMillis();
			while(!FMLModsDiscovered){
				try {
					fmllock.wait();
				} catch (InterruptedException e) {
					
				}
			}
			JMOD.LOG.info("Waited " + (System.currentTimeMillis() - start) + "ms for FMLMods to be discovered.");
		}
		return Loader.isModLoaded(modid);
	}
	
}
