package com.jeffpeng.jmod.scripting;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.JMODContainer;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.ModScriptObject;
import com.jeffpeng.jmod.util.LoaderUtil;

public class JScript {
	private static Map<String,String> extraScriptingObjects = new HashMap<>();
	private ScriptEngine jsEngine;
	
	private JMODRepresentation jmod;
	private Map<String, Object> config;
	
	public void evalScript(String script){
		boolean retry = true;
		
		if(JMOD.isServer()){
			try {
				jsEngine.eval(readScript(script));
			} catch (ScriptException e) {
				
				String stackTraceString = "";
				@SuppressWarnings("unused")
				String jslinestr = "";

				StackTraceElement[] stackTrace = e.getCause().getStackTrace();
				for(StackTraceElement ste : stackTrace){
					stackTraceString += ste.toString()+"\n";
				}
				
				Pattern pattern = Pattern.compile("\\(<eval>:[0-9]{1,3}\\)");
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
				jsEngine.eval(readScript(script));
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
				
				Pattern pattern = Pattern.compile("\\(<eval>:[0-9]{1,3}\\)");
				Matcher matcher = pattern.matcher(stackTraceString);
				if(matcher.find()){
					jslinestr = matcher.group(0);
				}
				
				String message = e.getMessage() + "\n in " +script+" on line " +  jslinestr+"\n\nYou can try and fix the script, and then\nreturn to this dialog to retry.";
				
				opt = JOptionPane.showOptionDialog(frame,message,jmod.getModName()+": "+script,
						JOptionPane.YES_NO_OPTION,JOptionPane.ERROR_MESSAGE,null, options,options[0]);
				
				
				if(opt < 1) retry = true;
				if(opt == 1) {
					e.printStackTrace();
					e.getCause().printStackTrace();
					System.out.println(e.getCause().getClass().getName());
					jmod.markScriptsAsErrored();
				}
				
				if(opt == 2){
					JMOD.LOG.warn("Ignored an SI script exception: " + e.getFileName() + ": " + e.getMessage() + " in " +script+" on line " +  e.getLineNumber());
					JMOD.LOG.warn("This usually is a pretty bad idea.");
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public JScript(JMODRepresentation jmod){
		
		this.jmod = jmod;
		jsEngine = LoaderUtil.jsManager.getEngineByName("nashorn");
		jsEngine.put("config", config);
		Object globalScope;
		try {
			globalScope = jsEngine.eval("this");
			Object jsObject = jsEngine.eval("Object");
			Invocable inv = (Invocable)jsEngine;
			inv.invokeMethod(jsObject, "bindProperties", globalScope,new ScriptObject(this));
			@SuppressWarnings("rawtypes")
			Class[] args = new Class[1];
			args[0] = JMODRepresentation.class;
			
			for(Map.Entry<String, String> entry : extraScriptingObjects.entrySet()){
				try {
					System.out.println("###aso" + globalScope.getClass());
					System.out.println("###aso " + entry.getKey() + " " + entry.getValue());
					ModScriptObject	instance = (ModScriptObject) Class.forName(entry.getValue()).getDeclaredConstructor(args).newInstance(jmod);
					
					((Map<String,Object>)globalScope).put(entry.getKey(), instance);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException
						| ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
				
				
			}
			
			
		} catch (ScriptException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	private String readScript(String script) {
		
		JMODContainer container = jmod.getContainer();
		
		if(container == null) throw new RuntimeException(jmod.getModId() + " is null container");
		
		try {
			return LoaderUtil.readFile(container.getSource().toPath(), script);
			
		} catch (IOException e) {
			JMOD.LOG.warn("[Scripting] Could not read " + script + " from " + container.getSource());
			return "";
		} 
	}
	
	public JMODRepresentation getMod(){
		return jmod;
	}
	
	public static void addExtraScriptingObject(String scriptObjectName, String className){
		extraScriptingObjects.put(scriptObjectName, className);
	}
	
	
	
	
	
}
