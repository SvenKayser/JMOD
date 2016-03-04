package com.jeffpeng.si.core.util.descriptors;

import net.minecraft.item.Item;

import org.apache.logging.log4j.Level;

import com.jeffpeng.si.core.SI;
import com.jeffpeng.si.core.interfaces.ISIArmor;
import com.jeffpeng.si.core.interfaces.ISIItem;
import com.jeffpeng.si.core.interfaces.ISIItemColor;
import com.jeffpeng.si.core.interfaces.ISIStagedObject;
import com.jeffpeng.si.core.interfaces.ISITool;
import com.jeffpeng.si.core.items.SICoreFood;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ItemDescriptor implements ISIStagedObject{
	private static final String QUALIFIEDCLASSNAMEBASE = "com.jeffpeng.si.core.items.";
	
	private String name;
	private String refClass;
	private int stackSize;
	private String tab;
	private ToolDataDescriptor tooldata;
	private ArmorDataDescriptor armordata;
	private FoodDataDescriptor fooddata;
	private ColorDescriptor colordata;
	private Integer colorindex;
	
	private ISIItem instance; 
	

	public ItemDescriptor(String name, String refClass, int stackSize, String tab){
		registerAsStaged();
		this.name = name;
		this.refClass = refClass;
		this.stackSize = stackSize;
		this.tab = tab;
	}
	
	public ItemDescriptor tooldata(ToolDataDescriptor tooldata){
		this.tooldata = tooldata;
		return this;
	}
	

	
	public ItemDescriptor armordata(ArmorDataDescriptor armordata){
		this.armordata = armordata;
		return this;
	}
	
	public ItemDescriptor fooddata(FoodDataDescriptor fooddata){
		this.fooddata = fooddata;
		return this;
	}
	
	public ItemDescriptor color(ColorDescriptor color){
		this.colordata = color;
		return this;
	}
	
	public ItemDescriptor colorindex(int colorindex){
		this.colorindex = colorindex;
		return this;
	}
	
	@Override
	public boolean on(FMLInitializationEvent event){
		try {
			Class<?> clazz;

			if (refClass.contains(".")) {
				clazz = Class.forName(refClass);
			} else {
				clazz = Class.forName(QUALIFIEDCLASSNAMEBASE + refClass);
			}

			if (ISITool.class.isAssignableFrom(clazz) && ISIItem.class.isAssignableFrom(clazz)) {
				@SuppressWarnings("rawtypes")
				Class[] args = new Class[1];
				args[0] = ToolDataDescriptor.class;
				instance = (ISIItem) clazz.getDeclaredConstructor(args).newInstance(tooldata);
			} else
				
			if (ISIArmor.class.isAssignableFrom(clazz) && ISIItem.class.isAssignableFrom(clazz)) {
				@SuppressWarnings("rawtypes")
				Class[] args = new Class[2];
				args[0] = String.class;
				args[1] = String.class;
				instance = (ISIItem) clazz.getDeclaredConstructor(args).newInstance(armordata.armorMaterial,armordata.armorType);
			} else 
			
			if (SICoreFood.class.isAssignableFrom(clazz)) {
				@SuppressWarnings("rawtypes")
				Class[] args = new Class[1];
				args[0] = FoodDataDescriptor.class;
				instance = (ISIItem) clazz.getDeclaredConstructor(args).newInstance(fooddata);
			} else

			if (ISIItem.class.isAssignableFrom(clazz)) {
				instance = (ISIItem) clazz.newInstance();
			} else {
				throw new RuntimeException("[si.core] [itemregistry] This item does not implement ISIItem.");
			}
			
			if(instance instanceof ISIItemColor){
				if(colordata != null) ((ISIItemColor)instance).setColor(colordata.red,colordata.green,colordata.blue);
				if(colorindex != null) ((ISIItemColor)instance).setColorIndex(colorindex);
			}

			instance.setName(name);
			
			((Item) instance).setTextureName(instance.getPrefix() + ":" + name);
			((Item) instance).setMaxStackSize(stackSize);
			
			if(!SI.isServer) instance.presetCreativeTab(SI.REGISTRY.getCreativeTabByName(tab));
		} catch (Exception e) {
			FMLLog.log(Level.WARN, "Could not instantiate " + name + ". Possibly the constructor is malformed?");
			e.printStackTrace();
		}

		instance.register();
		if(!SI.isServer) instance.setCreativeTab();

		return true;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event){
		instance.setRecipes();
		return true;
		
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return true;
	}
}
