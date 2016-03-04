package com.jeffpeng.si.core.registry;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.material.Material;

public class SIBlockMaterialRegistry {
	private static Map<String,Material> materialmap;
	
	public SIBlockMaterialRegistry(){
		materialmap = new HashMap<String,Material>();
		
		materialmap.put("air", Material.air);
		materialmap.put("grass",Material.grass);
		materialmap.put("ground",Material.ground);
		materialmap.put("wood",Material.wood);
		materialmap.put("rock",Material.rock);
		materialmap.put("iron",Material.iron);
		materialmap.put("anvil",Material.anvil);
		materialmap.put("water",Material.water);
		materialmap.put("lava",Material.lava);
		materialmap.put("leaves",Material.leaves);
		materialmap.put("plants",Material.plants);
		materialmap.put("vine",Material.vine);
		materialmap.put("sponge",Material.sponge);
		materialmap.put("cloth",Material.cloth);
		materialmap.put("fire",Material.fire);
		materialmap.put("sand",Material.sand);
		materialmap.put("circuits",Material.circuits);
		materialmap.put("carpet",Material.carpet);
		materialmap.put("glass",Material.glass);
		materialmap.put("redstoneLight",Material.redstoneLight);
		materialmap.put("tnt",Material.tnt);
		materialmap.put("coral",Material.coral);
		materialmap.put("ice",Material.ice);
		materialmap.put("packedIce",Material.packedIce);
		materialmap.put("snow",Material.snow);
		materialmap.put("craftedSnow",Material.craftedSnow);
		materialmap.put("cactus",Material.cactus);
		materialmap.put("clay",Material.clay);
		materialmap.put("gourd",Material.gourd);
		materialmap.put("dragonEgg",Material.dragonEgg);
		materialmap.put("portal",Material.portal);
		materialmap.put("cake",Material.cake);
		materialmap.put("web",Material.web);
		materialmap.put("piston",Material.piston);
	}
	
	public Material get(String materialname){
		return materialmap.get(materialname);
	}
	
}
