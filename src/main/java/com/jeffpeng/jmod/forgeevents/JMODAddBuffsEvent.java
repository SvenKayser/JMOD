package com.jeffpeng.jmod.forgeevents;

import java.util.Map;

import net.minecraft.potion.Potion;

public class JMODAddBuffsEvent extends JMODForgeEvent {
	public Map<String,Potion> buffMap;
	
	public JMODAddBuffsEvent(Map<String,Potion> buffMap){
		this.buffMap = buffMap;
	}

}
