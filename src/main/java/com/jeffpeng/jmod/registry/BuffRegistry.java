package com.jeffpeng.jmod.registry;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.potion.Potion;

import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.API.forgeevents.JMODAddBuffsEvent;

public class BuffRegistry {
	
	private static BuffRegistry instance = new BuffRegistry();
	private Map<String,Potion>buffMap = new HashMap<String,Potion>();
			
	public static BuffRegistry getInstance(){
		return instance;
	}
	
	private BuffRegistry(){
		buffMap.put("movespeed", Potion.moveSpeed);
		buffMap.put("moveslowdown", Potion.moveSlowdown);
		buffMap.put("digspeed", Potion.digSpeed);
		buffMap.put("digslowdown", Potion.digSlowdown);
		buffMap.put("damagesoost", Potion.damageBoost);
		buffMap.put("heal",Potion.heal);
		buffMap.put("harm",Potion.harm);
		buffMap.put("jump", Potion.jump);
		buffMap.put("confusion", Potion.confusion);
		buffMap.put("regeneration", Potion.regeneration);
		buffMap.put("resistance", Potion.resistance);
		buffMap.put("fireResistance", Potion.fireResistance);
		buffMap.put("waterBreathing", Potion.waterBreathing);
		buffMap.put("invisibility", Potion.invisibility);
		buffMap.put("blindness", Potion.blindness);
		buffMap.put("nightvision", Potion.nightVision);
		buffMap.put("hunger", Potion.hunger);
		buffMap.put("weakness", Potion.weakness);
		buffMap.put("poison", Potion.poison);
		buffMap.put("wither", Potion.wither);
		buffMap.put("healthBoost", Potion.field_76434_w);
		buffMap.put("absorption", Potion.field_76444_x);
		buffMap.put("saturation", Potion.field_76443_y);
		JMOD.BUS.post(new JMODAddBuffsEvent(buffMap));
		
	}
	
	public void registerBuff(){
		
	}
	
	public static Potion getBuff(String name){
		if(instance.buffMap.containsKey(name)) return instance.buffMap.get(name);
		return null;
	}
	
	
}
