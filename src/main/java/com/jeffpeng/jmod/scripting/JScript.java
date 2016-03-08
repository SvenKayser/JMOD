package com.jeffpeng.jmod.scripting;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.jeffpeng.jmod.Config;
import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.JMODContainer;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;

public class JScript {
	private ScriptEngine jsEngine;
	private static final ScriptEngineManager jsManager = new ScriptEngineManager(null);
	private JMODRepresentation jmod;
	private Config config;
	
	public void evalScript(String script){
		boolean retry = true;
		
		if(JMOD.isServer()){
			try {
				jsEngine.eval("//#sourceURL=" + script + "\nwith(Object.bindProperties({},SI)){"+readScript(script)+"}");
				
				
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
				jsEngine.eval("//#sourceURL=" + script + "\nwith(Object.bindProperties({},SI)){"+readScript(script)+"}");
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
				
				String message = e.getMessage() + "\n in " +script+" on line " +  e.getLineNumber()+"\n\nYou can try and fix the script, and then\nreturn to this dialog to retry.";
				
				JMOD.LOG.info(message);
				
				opt = JOptionPane.showOptionDialog(frame,message,e.getFileName(),
						JOptionPane.YES_NO_OPTION,JOptionPane.ERROR_MESSAGE,null, options,options[0]);
				
				
				if(opt < 1) retry = true;
				if(opt == 1) {
					e.printStackTrace();
					e.getCause().printStackTrace();
					System.out.println(e.getCause().getClass().getName());
					throw new RuntimeException(e.getFileName() + ": " + e.getMessage() + " in " + e.getLineNumber()+"\n"+jslinestr);
				}
				
				if(opt == 2){
					JMOD.LOG.warn("Ignored an SI script exception: " + e.getFileName() + ": " + e.getMessage() + " in " +script+" on line " +  e.getLineNumber());
					JMOD.LOG.warn("This usually is a pretty bad idea.");
				}
			}
		}
	}
	
	public JScript(JMODRepresentation jmod){
		
		this.jmod = jmod;
		jsEngine = jsManager.getEngineByName("nashorn");
		jsEngine.put("config", config);
		jsEngine.put("SI",new ScriptObject(this));
	}
	
	
	
	private String readScript(String script) {
		
		JMODContainer container = jmod.getContainer();
		
		if(container == null) throw new RuntimeException(jmod.getModId() + " is null container");
				
		try {
			return Lib.readFile(container.getSource().toPath(), script);
			
		} catch (IOException e) {
			JMOD.LOG.warn("[Scripting] Could not read " + script + " from " + container.getSource());
			return "";
		} 
	}
	
	public JMODRepresentation getMod(){
		return jmod;
	}
	
	
	
}
