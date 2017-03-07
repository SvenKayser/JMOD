package com.jeffpeng.jmod.actions;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;
import com.jeffpeng.jmod.interfaces.ISettingsProcessor;
import com.jeffpeng.jmod.primitives.BasicAction;
import com.jeffpeng.jmod.types.blocks.CoreBlock;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;


public class AddBlock extends BasicAction{
	private static int blockcounter = 0;
	private static final String QUALIFIEDCLASSNAMEBASE = "com.jeffpeng.jmod.types.blocks.";

	private String refClass;
	
	private CoreBlock instance;
	
//	public AddBlock(JMODRepresentation owner, String name, String refClass, Float hardness, Float blastresistance,
//			String tool, int harvestlevel, String material, String tab){
	public AddBlock(JMODRepresentation owner, String refClass){

		super(owner);
		this.refClass = refClass;
		this.set("name",refClass + AddBlock.blockcounter++);
		this.set("hardness",1.0);
		this.set("blastresistance",1.0);
		this.set("tool","pickaxe");
		this.set("harvestlevel",1);
		this.set("material","rock");
		this.set("tab",null);
		this.set("lightlevel",0);
		this.set("opacity",null);
		
		this.valid = true;
	}

	@Override
	public boolean on(FMLPreInitializationEvent event){
		
		@SuppressWarnings("rawtypes")
		Class[] args = new Class[2];
		args[0] = JMODRepresentation.class;
		args[1] = Material.class;

		try{
			if(refClass.startsWith(".")){
				instance = (CoreBlock) Class.forName(JMOD.ARCHIVEBASE+refClass).getDeclaredConstructor(args).newInstance(owner,Lib.getBlockMaterial(getString("material")));
			} else 
			if(refClass.contains(".")){
				instance = (CoreBlock) Class.forName(refClass).getDeclaredConstructor(args).newInstance(owner,Lib.getBlockMaterial(getString("material")));
			} else {
				instance = (CoreBlock) Class.forName(QUALIFIEDCLASSNAMEBASE+refClass).getDeclaredConstructor(args).newInstance(owner,Lib.getBlockMaterial(getString("material")));
			}
			
			instance.setName(getString("name"));
			instance.setHardness(getFloat("hardness"));
			instance.setResistance(getFloat("blastresistance"));
			instance.setHarvestLevel(getString("tool"), getInt("harvestlevel"));
			instance.setBlockTextureName(instance.getPrefix() + ":" + getString("name"));
			instance.setLightLevel(getInt("lightlevel"));
			if(getInt("opacity") != null){
				instance.setOpaque(true);
				instance.setLightOpacity(getInt("opacity"));
			}
			
			if(instance instanceof ISettingsProcessor)
			{
				((ISettingsProcessor)instance).processSettings(this);
			}
			
			
//			if(instance instanceof DynamicBlock && power != null && power > 0){
//				((DynamicBlock)instance).setPowered(true);
//				((DynamicBlock)instance).setPower(power, powerSides);
//			}
			
			
			
		} catch(ClassNotFoundException e) {
			log.warn("Could not instantiate " + getString("name") + " since the referenced class is missing. Maybe you are referring to mod class not loaded, or the class is implementing an Interface of a mod not loaded?");
			e.printStackTrace();
			return false;
		} catch(Exception e){
			log.warn("Could not instantiate " + getString("name") + ". Possibly the constructor is malformed?");
			e.printStackTrace();
			return false;
		}
		
		instance.register();
		return true;
	}
	
	@Override
	public boolean on(FMLPostInitializationEvent event){
		if(!JMOD.isServer() && getString("tab") != null){
			CreativeTabs tabInstance = Lib.getCreativeTabByName(getString("tab"));
			if(tabInstance != null && instance != null){
				((Block)instance).setCreativeTab(tabInstance);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int priority(){
		return Priorities.AddBlock;
	}
}
