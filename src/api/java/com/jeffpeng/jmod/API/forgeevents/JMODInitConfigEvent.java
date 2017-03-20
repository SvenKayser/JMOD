package com.jeffpeng.jmod.API.forgeevents;

import java.util.Map;

public class JMODInitConfigEvent extends JMODForgeEvent {
	private Map<String,Object> config;
	
	public JMODInitConfigEvent(Map<String,Object> config){
		this.config = config;
	}
	
	public Map<String,Object> get(){
		return config;
	}

}
