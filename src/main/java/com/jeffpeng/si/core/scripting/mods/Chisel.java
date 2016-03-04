package com.jeffpeng.si.core.scripting.mods;

import com.jeffpeng.si.core.util.descriptors.CarvingGroupDescriptor;
import com.jeffpeng.si.core.util.descriptors.CarvingVariationDescriptor;

import cpw.mods.fml.common.Loader;

public class Chisel {
	public  void addGroup(String group){
		if(Loader.isModLoaded("Chisel")) new CarvingGroupDescriptor(group);
	}
	
	public  void addVariation(String group, String variation){
		if(Loader.isModLoaded("Chisel")) new CarvingVariationDescriptor(group,variation);
	}
}
