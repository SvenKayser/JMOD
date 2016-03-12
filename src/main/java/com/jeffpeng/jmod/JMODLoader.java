package com.jeffpeng.jmod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.jeffpeng.jmod.primitives.JMODInfo;
import com.jeffpeng.jmod.primitives.ModCreationException;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.common.ProgressManager.ProgressBar;



@SuppressWarnings("deprecation")
public class JMODLoader {

	private static boolean FMLModsDiscovered = false;
	private static List<Path> modQueue = new ArrayList<>();
	private static Map<String,JMODContainer> modList = new HashMap<String,JMODContainer>();
	private static final String MODSDIRECTORY = "mods";
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
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
		
		ProgressBar bar = ProgressManager.push("Constructing JMODs", modQueue.size());
		
		JMOD.LOG.info("Constructing " + modQueue.size() + " JMODs");
		
		for(Path entry : modQueue){
			
			bar.step(entry.getFileName().toString());
			
			String rawjson = "";
			JMODInfo configdata = null;
			JMODContainer newmod = null;
			
			try{
				rawjson = Lib.readFile(entry, "mod.json");
			} catch (IOException e){
				JMOD.LOG.warn("[JMODLoader] Failed to load mod declaration from " + entry.toString() + ". This is an error of the mod's author. Skipping.");
				continue;
			}
			
			
			
			try {
				configdata = gson.fromJson(rawjson, JMODInfo.class);
				if(!infoDataSanity(configdata,entry.getFileName().toString())) continue;
					
			} catch (JsonSyntaxException e){
				JMOD.LOG.warn("[JMODLoader] Failed to parse JSON from " + entry.toString() + "   Probably the JSON is malformed. This is an error of the mod's author. Skipping.");
				continue;
			}
			
			if(modids.contains(configdata.modid)){
				JMOD.LOG.warn("[JMODLoader] The mod " + configdata.modid + " seems to be present more than once. Cannot load the same mod twice. This is either an error of the ModPack creator or you - so fix it! Skipping.");
				continue;
			}
			
			modids.add(configdata.modid);
			
			try {
				newmod = new JMODContainer(new JMODRepresentation(configdata,!Files.isDirectory(entry)),entry.toFile());
			} catch (ModCreationException e){
				JMOD.LOG.warn(entry.toString() +": " + e.getMessage());
				continue;
			}
			
			newmod.getMod().runScripts();
			
			modList.put(newmod.getModId(),newmod);
			
		}
		ProgressManager.pop(bar);		
		

	}
	
	protected static void inject(){
		for(Map.Entry<String,JMODContainer> entry : modList.entrySet()){
			JMOD.DEEPFORGE.addMod(entry.getValue());
			JMOD.LOG.info("[JMODLoader] successfully injected jmod \"" + entry.getKey()+"\"");
		}
		JMOD.DEEPFORGE.lockDown();
	}
	
	private static boolean isJmod(Path path){
		if(path.toString().endsWith(".jmod")){
			return true;
		}
		return false;
	}
	
	protected static void waitOnScripts(){
		long start = System.currentTimeMillis();
		boolean finished = false;
		while(!finished){
			finished = true;
			for(Map.Entry<String,JMODContainer> entry : modList.entrySet()){
				finished &= entry.getValue().getMod().isScriptingFinished();
				if(!finished)	break;
			}
			if(!finished)
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(finished) JMOD.LOG.info("Waited on scripts to complete for " + (System.currentTimeMillis() - start) + "ms");
		}
	}
	
	public static JMODRepresentation getMod(String modid){
		return modList.get(modid).getMod();
	}
	
	public static JMODContainer getModContainer(String modid){
		return modList.get(modid);
	}
	
	private static boolean infoDataSanity(JMODInfo info,String entry){
		if(info.modid == null){
			JMOD.LOG.warn("[JMODLoader] The jmod " + entry + " has no modid. That won't work. This is an error of the mod author. Skipping.");
			return false;
		}
		
		if(info.scripts == null || info.scripts.size() == 0){
			JMOD.LOG.warn("[JMODLoader] The jmod " + info.modid + " has no scritps. What is it supposed to do? Look good? Loading, but other than load the attached resources, this mod does nothing. This is an error of the mod author.");
			info.scripts = new ArrayList<>();
		}

		
		if(info.name == null){
			JMOD.LOG.warn("[JMODLoader] The jmod " + info.modid + " has no name. Assuming it's the same as the mod id. It's ugly tho. This is an error of the mod author.");
			info.name = info.modid;
		}
		
		if(info.version == null){
			JMOD.LOG.warn("[JMODLoader] The jmod " + info.name + " has no version. Assuming \"v1\". This should be fixed. This is an error of the mod author.");
		}
		return true;
		
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
