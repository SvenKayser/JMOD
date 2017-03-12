package com.jeffpeng.jmod;

import com.jeffpeng.jmod.primitives.JMODPluginInfo;

public class JMODPluginContainer {
	public JMODPluginInfo info;
	private JMODPlugin plugininstance; 
	
	public JMODPluginContainer(){
		info = new JMODPluginInfo();
	}
	
	public void setInstance(JMODPlugin instance){
		this.plugininstance = instance;
	}
	
	public JMODPlugin getInstance(){
		return this.plugininstance;
	}
	
	
}
