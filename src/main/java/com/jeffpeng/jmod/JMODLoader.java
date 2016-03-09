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

import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.common.ProgressManager.ProgressBar;



@SuppressWarnings("deprecation")
public class JMODLoader {

	private List<Path> modQueue = new ArrayList<>();
	private Map<String,JMODContainer> modList = new HashMap<String,JMODContainer>();
	private static final String MODSDIRECTORY = "mods";
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private List<String> modids = new ArrayList<>();
	
	public Map<String,JMODContainer> getModList(){
		return modList;
	}
	
	protected void discoverMods(){
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
	
	protected void constructMods(){
		if(JMOD.DEEPFORGE.isLocked()){
			throw new RuntimeException("Cannot add any more mods at this point.");
			
		}
		
		ProgressBar bar = ProgressManager.push("Constructing JMODs", modQueue.size());
		
		JMOD.LOG.info("Constructing " + modQueue.size() + " JMODs");
		
		for(Path entry : modQueue){
			
			bar.step(entry.toString());
			
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
			} catch (JsonSyntaxException e){
				JMOD.LOG.warn("[JMODLoader] Failed to parse JSON from " + entry.toString() + "   Probably the JSON is malformed. This is an error of the mod's author. Skipping.");
				continue;
			}
			
			if(modids.contains(configdata.modid)){
				JMOD.LOG.warn("[JMODLoader] The mod " + configdata.modid + " seems to be present more than once. Cannot load the same mod twice - fix it!");
			}
			
			modids.add(configdata.modid);
			
			try {
				newmod = new JMODContainer(new JMODRepresentation(configdata,!Files.isDirectory(entry)),entry.toFile());
			} catch (ModCreationException e){
				JMOD.LOG.warn(entry.toString() +": " + e.getMessage());
				continue;
			}
			
			
			this.modList.put(newmod.getModId(),newmod);
			JMOD.DEEPFORGE.addMod(newmod);
			JMOD.LOG.info("[JMODLoader] successfully injected jmod \"" + newmod.getName()+"\"");
		}
		
		ProgressManager.pop(bar);
		
		
	}
	
	private boolean isJmod(Path path){
		
		
		if(path.toString().endsWith(".jmod")){
			return true;
		}
		return false;
	}
	
	
	
	public JMODRepresentation getMod(String modid){
		return modList.get(modid).getMod();
	}
	
	public JMODContainer getModContainer(String modid){
		return modList.get(modid);
	}
	
}
