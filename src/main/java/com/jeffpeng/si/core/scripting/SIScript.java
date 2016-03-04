package com.jeffpeng.si.core.scripting;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.jeffpeng.si.core.SI;
import com.jeffpeng.si.core.scripting.mods.*;
import cpw.mods.fml.common.FMLLog;

public class SIScript {
	private ScriptEngine jsEngine;
	private ScriptEngineManager jsManager;
	private  String CONFIGPATH = "config/si.core/";
	
	private static SIScript instance;
	public static SIScript instance(){
		return instance;
	}
	public static void init(){
		instance = new SIScript();
	}
	
	public RotaryCraft RotaryCraft = new RotaryCraft();
	public Chisel Chisel = new Chisel();
	
	
	
	public void evalScript(String script){
		boolean retry = true;
		
		if(SI.isServer){
			try {
				jsEngine.eval("//#sourceURL=" + script + "\nwith(Object.bindProperties({},SI)){"+readCFGScript(script)+"}");
				
				
			} catch (ScriptException e) {
				
				String stackTraceString = "";
				@SuppressWarnings("unused")
				String jslinestr = "";

				StackTraceElement[] stackTrace = e.getCause().getStackTrace();
				for(StackTraceElement ste : stackTrace){
					stackTraceString += ste.toString()+"\n";
				}
				
				Pattern pattern = Pattern.compile("\\([a-z,\\/,\\.]*\\:[0-9]{1,3}\\)");
				Matcher matcher = pattern.matcher(stackTraceString);
				if(matcher.find()){
					jslinestr = matcher.group(0);
				}
				
				e.printStackTrace();
				throw new RuntimeException(e.getFileName() + ": " + e.getMessage() + " in " +script+" on line " +  e.getLineNumber());
			}
		} else 
		
		while(retry){
			retry = false;
			try {
				//jsEngine.eval("with(Object.bindProperties({},SI)){load('"+CONFIGPATH+"');}");
				jsEngine.eval("//#sourceURL=" + script + "\nwith(Object.bindProperties({},SI)){"+readCFGScript(script)+"}");
			} catch (ScriptException e) {
				
				Object[] options = {"Retry now","I give up","I really don't care"};
				JFrame frame = new JFrame();
				
				int opt = 0;
				
				String stackTraceString = "";
				String jslinestr = "";

				StackTraceElement[] stackTrace = e.getCause().getStackTrace();
				for(StackTraceElement ste : stackTrace){
					stackTraceString += ste.toString()+"\n";
				}
				
				Pattern pattern = Pattern.compile("\\([a-z,\\/,\\.]*\\:[0-9]{1,3}\\)");
				Matcher matcher = pattern.matcher(stackTraceString);
				if(matcher.find()){
					jslinestr = matcher.group(0);
				}
				
				opt = JOptionPane.showOptionDialog(frame,e.getMessage() + "\n in " +script+" on line " +  e.getLineNumber()+"\n\nYou can try and fix the script, and then\nreturn to this dialog to retry.",e.getFileName(),
						JOptionPane.YES_NO_OPTION,JOptionPane.ERROR_MESSAGE,null, options,options[0]);
				
				
				if(opt < 1) retry = true;
				if(opt == 1) {
					e.printStackTrace();
					e.getCause().printStackTrace();
					System.out.println(e.getCause().getClass().getName());
					throw new RuntimeException(e.getFileName() + ": " + e.getMessage() + " in " + e.getLineNumber()+"\n"+jslinestr);
				}
				
				if(opt == 2){
					FMLLog.warning("Ignored an SI script exception: " + e.getFileName() + ": " + e.getMessage() + " in " +script+" on line " +  e.getLineNumber());
					FMLLog.warning("This usually is a pretty bad idea.");
				}
			}
		}
	}
	
	public SIScript(){
		
		
		jsManager = new ScriptEngineManager(null);
		jsEngine = jsManager.getEngineByName("nashorn");
		jsEngine.put("config", SI.CONFIG);
		jsEngine.put("SI",new SIScriptObject());
	}
	
	
	
	private String readCFGScript(String script) {

		File file = new File(CONFIGPATH+script);
		try {
			return Files.toString(file, Charsets.UTF_8);
			
		} catch (IOException e) {
			FMLLog.warning("[Scripting] Could not read " + CONFIGPATH + script);
			SI.CONFIG.libraryMode = true;
			return "";
		} 
	}
	
}
