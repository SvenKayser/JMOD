package com.jeffpeng.jmod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jeffpeng.jmod.primitives.JMODInfo;
import com.jeffpeng.jmod.primitives.ModCreationException;
import com.jeffpeng.jmod.util.LoaderUtil;

import cpw.mods.fml.common.Loader;

public class JMODLoader {

	private static boolean FMLModsDiscovered = false;
	private static List<Path> modQueue = new ArrayList<>();
	private static Map<String,JMODContainer> modList = new HashMap<String,JMODContainer>();
	private static final String MODSDIRECTORY = "mods";
	private static List<String> modids = new ArrayList<>();
	private static Object fmllock = new Object();
	
	
	public static Map<String,JMODContainer> getModList(){
		return modList;
	}
	
	protected static void discoverMods(){
		if(JMOD.DEEPFORGE != null && JMOD.DEEPFORGE.isLocked()){
			throw new RuntimeException("Cannot add any more mods at this point.");
		}
		try {
			Files.walk(Paths.get(MODSDIRECTORY)).forEach(filePath -> {
			    if(isJmod(filePath) && !modQueue.contains(filePath)){
			    	modQueue.add(filePath);
			    	JMOD.LOG.info("discovered jmod at " + filePath);
			    }
			});
		} catch (IOException e) {
			JMOD.LOG.warn("There was a rather unexpected error while discovering mods. Maybe you don't have rights over your mod directory?");
			e.printStackTrace();
		}
	}
	
	protected static void constructMods(){
		if(JMOD.DEEPFORGE != null &&JMOD.DEEPFORGE.isLocked()){
			throw new RuntimeException("Cannot add any more mods at this point.");
		}
		
		//ProgressBar bar = ProgressManager.push("Constructing JMODs", modQueue.size());
		
		JMOD.LOG.info("Constructing " + modQueue.size() + " JMODs");
		
		for(Path entry : modQueue){
			JMOD.LOG.info("Constructing " + entry.toString());
			//bar.step(entry.getFileName().toString());
			
			String rawjson = LoaderUtil.loadModJson(entry);
			if(rawjson == null){
				JMOD.LOG.warn("[JMODLoader] Failed to load mod declaration from " + entry.toString() + ". This is an error of the mod's author. Skipping.");
				continue;
			}
			JMOD.LOG.info("loaded mod declaration " + entry.toString());
			
			JMODInfo configdata = LoaderUtil.parseModJson(rawjson);
			if(configdata == null){
				JMOD.LOG.warn("[JMODLoader] Failed to parse JSON from " + entry.toString() + "   Probably the JSON is malformed. This is an error of the mod's author. Skipping.");
				continue;
			}
			JMODContainer newmod = null;
			
			if(!LoaderUtil.infoDataSanity(configdata,entry.getFileName().toString())) continue;
			
			if(modids.contains(configdata.modid)){
				JMOD.LOG.warn("[JMODLoader] The mod " + configdata.modid + " seems to be present more than once. Cannot load the same mod twice. This is either an error of the ModPack creator or you - so fix it! Skipping.");
				continue;
			}
			
			JMOD.LOG.info("sanity " + entry.toString());
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
			entry.getValue().getMod().runScripts();
		}
	}
	
	private static boolean isJmod(Path path){
		if(path.toString().endsWith(".jmod")){
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
