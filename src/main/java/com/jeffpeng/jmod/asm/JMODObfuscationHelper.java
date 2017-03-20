package com.jeffpeng.jmod.asm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.launchwrapper.Launch;

public class JMODObfuscationHelper {
	
	private static boolean deobf = false;
	
	
	
	private static Map<String,String> list = new HashMap<>();
	
	
	public static void init(){
		byte[] bs;
		try {
			bs = Launch.classLoader.getClassBytes("net.minecraft.world.World");
			if (bs != null)
	        {
	            deobf = true;
	        }
		} catch (IOException e) {}
		
		list.put("net.minecraft.inventory.ContainerRepair$2", "zw");
		list.put("net.minecraft.block.BlockSapling", "anj");
		list.put("net.minecraft.client.multiplayer.ServerList", "bjp");
		list.put("net.minecraft.creativetab.CreativeTabs", "abt");
		list.put("net.minecraft.client.gui.inventory.GuiContainerCreative", "bfl");
	
	}
	
	public static boolean isDeobf(){
		return deobf;
	}
	
	public static String get(String name){
		if(deobf) return name; else return list.get(name);
	}

}
