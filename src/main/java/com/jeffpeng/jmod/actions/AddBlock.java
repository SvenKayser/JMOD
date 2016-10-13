package com.jeffpeng.jmod.actions;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;
import com.jeffpeng.jmod.primitives.BasicAction;
import com.jeffpeng.jmod.types.blocks.CoreBlock;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class AddBlock extends BasicAction{
	private static final String QUALIFIEDCLASSNAMEBASE = "com.jeffpeng.jmod.types.blocks.";

	private String name;
	private String refClass;
	private Float hardness;
	private Float blastresistance;
	private String tool;
	private int harvestlevel;
	private String material;
	private String tab;
	
	private CoreBlock instance;
	
	public AddBlock(JMODRepresentation owner, String name, String refClass, Float hardness, Float blastresistance,
			String tool, int harvestlevel, String material, String tab){
		super(owner);
		this.name = name;
		this.refClass = refClass;
		this.hardness = hardness;
		this.blastresistance = blastresistance;
		this.tool = tool;
		this.harvestlevel = harvestlevel;
		this.material = material;
		this.tab = tab;
		this.valid = true;
	}
	

	@Override
	public boolean on(FMLPreInitializationEvent event){
		
		@SuppressWarnings("rawtypes")
		Class[] args = new Class[2];
		args[0] = JMODRepresentation.class;
		args[1] = Material.class;

		try{
			if(refClass.contains(".")){
				instance = (CoreBlock) Class.forName(refClass).getDeclaredConstructor(args).newInstance(owner,Lib.getBlockMaterial(material));
			} else {
				instance = (CoreBlock) Class.forName(QUALIFIEDCLASSNAMEBASE+refClass).getDeclaredConstructor(args).newInstance(owner,Lib.getBlockMaterial(material));
			}
			
			instance.setName(name);
			instance.setHardness(hardness);
			instance.setResistance(blastresistance);
			instance.setHarvestLevel(tool, harvestlevel);
			instance.setBlockTextureName(instance.getPrefix() + ":" + name);
			
			
			
		} catch(ClassNotFoundException e) {
			log.warn("Could not instantiate " + name + " since the referenced class is missing. Maybe you are referring to mod class not loaded, or the class is implementing an Interface of a mod not loaded?");
			e.printStackTrace();
			return false;
		} catch(Exception e){
			log.warn("Could not instantiate " + name + ". Possibly the constructor is malformed?");
			e.printStackTrace();
			return false;
		}
		
		instance.register();
		return true;
	}
	
	@Override
	public boolean on(FMLPostInitializationEvent event){
		if(!JMOD.isServer()){
			CreativeTabs tabInstance = Lib.getCreativeTabByName(tab);
			if(tabInstance != null && instance != null){
				((Block)instance).setCreativeTab(tabInstance);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int priority(){
		return 200;
	}
}
