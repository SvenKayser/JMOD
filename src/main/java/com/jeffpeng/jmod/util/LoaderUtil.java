package com.jeffpeng.jmod.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.primitives.JMODInfo;

public class LoaderUtil {
	
	public static final ScriptEngineManager jsManager = new ScriptEngineManager(null);
	private static ScriptEngine jsEngine = jsManager.getEngineByName("nashorn");
	
    
	
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
	
	@SuppressWarnings("unchecked")
	public static JMODInfo parseModJson(String rawjson){
		JMODInfo jmodinfo = null;
		
		try {
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			jmodinfo = gson.fromJson(rawjson, JMODInfo.class);
			
			if(jmodinfo.authors.isEmpty()) {
				jmodinfo.authors.add("John Doe (no author specified)");
			}

		} catch (JsonSyntaxException e){
			JMOD.LOG.warn("[JMODLoader] Failed to parse	JSON - Message: {}, RawJson: {}", 
					e.getMessage(), rawjson);
		}
		
		return jmodinfo;
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
