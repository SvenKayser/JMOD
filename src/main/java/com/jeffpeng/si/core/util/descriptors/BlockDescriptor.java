package com.jeffpeng.si.core.util.descriptors;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

import org.apache.logging.log4j.Level;

import com.jeffpeng.si.core.SI;
import com.jeffpeng.si.core.blocks.SICoreBlock;
import com.jeffpeng.si.core.interfaces.ISIStagedObject;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class BlockDescriptor implements ISIStagedObject{
	private static final String QUALIFIEDCLASSNAMEBASE = "com.jeffpeng.si.core.blocks.";

	private String name;
	private String refClass;
	private Float hardness;
	private Float blastresistance;
	private String tool;
	private int harvestlevel;
	private String material;
	private String tab;
	private String placer;
	
	private SICoreBlock instance;
	
	public BlockDescriptor(String name, String refClass, Float hardness, Float blastresistance,
			String tool, int harvestlevel, String material, String tab){
		registerAsStaged();
		this.name = name;
		this.refClass = refClass;
		this.hardness = hardness;
		this.blastresistance = blastresistance;
		this.tool = tool;
		this.harvestlevel = harvestlevel;
		this.material = material;
		this.tab = tab;
	}
	
	public BlockDescriptor placer(String placer){
		this.placer = placer;
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public boolean on(FMLInitializationEvent event){
		
		@SuppressWarnings("rawtypes")
		Class[] args = new Class[1];
		args[0] = Material.class;

		try{
			if(refClass.contains(".")){
				instance = (SICoreBlock) Class.forName(refClass).getDeclaredConstructor(args).newInstance(SI.REGISTRY.getBlockMaterial(material));
			} else {
				instance = (SICoreBlock) Class.forName(QUALIFIEDCLASSNAMEBASE+refClass).getDeclaredConstructor(args).newInstance(SI.REGISTRY.getBlockMaterial(material));
			}
			
			instance.setName(name);
			instance.setHardness(hardness);
			instance.setResistance(blastresistance);
			instance.setHarvestLevel(tool, harvestlevel);
			instance.setBlockTextureName(instance.getPrefix() + ":" + name);
			
			if(!SI.isServer) instance.presetCreativeTab(SI.REGISTRY.getCreativeTabByName(tab));
			if(placer != null){
				if(placer.contains(".")){
					instance.setPlacer((Class<ItemBlock>)Class.forName(placer));
				} else {
					instance.setPlacer((Class<ItemBlock>)Class.forName(QUALIFIEDCLASSNAMEBASE+"placers."+placer));
				}
			}
			
		} catch(ClassNotFoundException e) {
			FMLLog.log(Level.INFO, "Could not instantiate " + name + " since the referenced class is missing. Maybe you are referring to mod class not loaded, or the class is implementing an Interface of a mod not loaded?");
			e.printStackTrace();
			throw new RuntimeException();
		} catch(Exception e){
			FMLLog.log(Level.INFO, "Could not instantiate " + name + ". Possibly the constructor is malformed?");
			e.printStackTrace();
			throw new RuntimeException(material);
		}
		
		instance.register();
		if(!SI.isServer) instance.setCreativeTab();
		return true;
	}
	


	@Override
	public boolean isValid() {
		return true;
	}
}
