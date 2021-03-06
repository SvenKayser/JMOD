package com.jeffpeng.jmod.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.script.ScriptEngineManager;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.primitives.JMODInfo;
import com.jeffpeng.jmod.primitives.JMODPluginInfo;

public class LoaderUtil {
	
	public static final ScriptEngineManager jsManager = new ScriptEngineManager(null);
	
	public static String readFile(Path from, String file) throws IOException{
		String retstr = "";
		if(Files.isDirectory(from))
			for(String line : Files.readAllLines(from.resolve(file)))retstr += line + "\n";
		else {
			ZipFile zipfile = new ZipFile(from.toString());
			ZipEntry zipentry =  zipfile.getEntry(file);
			if(zipentry == null){
				zipfile.close();
				throw new IOException();
			}
			InputStream istream = zipfile.getInputStream(zipentry);
			retstr = IOUtils.toString(istream);
			zipfile.close();
		}
		return retstr;
	}
	
	public static String loadModJson(Path entry){
		
		String rawjson = null;
		
		try{
			rawjson = LoaderUtil.readFile(entry, "mod.json");
		} catch (IOException e){
			
		}
		
		return rawjson;
	}
	
	public static String loadPluginJson(Path entry){
		
		String rawjson = null;
		
		try{
			rawjson = LoaderUtil.readFile(entry, "plugin.json");
		} catch (IOException e){
			
		}
		
		return rawjson;
	}

	private static String stripComments(String rawjson){
		String newjson = rawjson.replaceAll("\\/\\*.*\\*\\/",""); // strips all /* […] */
		newjson = newjson.replaceAll("\\/\\/.*\\r", newjson); // strings all // until the end of the line
		return newjson;
	}
	
	public static JMODInfo parseModJson(String rawjson){
		JMODInfo jmodinfo = null;
		
		try {
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			jmodinfo = gson.fromJson(stripComments(rawjson), JMODInfo.class);
			jmodinfo = gson.fromJson(rawjson, JMODInfo.class);
			
			if(jmodinfo.authors.isEmpty()) {
				jmodinfo.authors.add("John Doe (no author specified)");
			}

		} catch (JsonSyntaxException e){
			JMOD.LOG.warn("[JMODLoader parseModJson] Failed to parse JSON - Message: {}, RawJson: {}", 
					e.getMessage(), rawjson);
		}

		return jmodinfo;
	}
	
	public static JMODPluginInfo parsePluginJson(String rawjson){
		JMODPluginInfo plugininfo = null;

		try {
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			plugininfo = gson.fromJson(stripComments(rawjson), JMODPluginInfo.class);
			if(plugininfo.authors.isEmpty()) {
				plugininfo.authors.add("John Doe (no author specified)");
			}
		} catch (JsonSyntaxException e){
			JMOD.LOG.warn("[JMODLoader parsePluginJson] Failed to parse	JSON - Message: {}, RawJson: {}", 
					e.getMessage(), rawjson);
		}

		return plugininfo;
	}
	
	public static boolean pluginInfoDataSanity(JMODPluginInfo info,String entry){
		if(info.pluginid == null){
			JMOD.LOG.warn("[JMODLoader Plugins] The plugin " + entry + " has no modid. That won't work. This is an error of the mod author. Skipping.");
			return false;
		}
		
		if(info.archivebase == null){
			JMOD.LOG.warn("[JMODLoader Plugins] The plugin " + entry + " has no archivebase. That won't work. This is an error of the mod author. Skipping.");
			return false;
		}
		
		if(info.name == null){
			JMOD.LOG.warn("[JMODLoader Plugins] The plugin" + info.pluginid + " has no name. Assuming it's the same as the plugin id. It's ugly tho. This is an error of the mod author.");
			info.name = info.pluginid;
		}
		
		if(info.version == null){
			JMOD.LOG.warn("[JMODLoader Plugins] The plugin " + info.name + " has no version. Assuming \"v1\". This should be fixed. This is an error of the mod author.");
		}
		return true;
	}
	
	public static boolean infoDataSanity(JMODInfo info,String entry){
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
}
