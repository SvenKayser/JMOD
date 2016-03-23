package com.jeffpeng.jmod.actions;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import org.apache.logging.log4j.Level;

import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;
import com.jeffpeng.jmod.descriptors.ArmorDataDescriptor;
import com.jeffpeng.jmod.descriptors.ColorDescriptor;
import com.jeffpeng.jmod.descriptors.FoodDataDescriptor;
import com.jeffpeng.jmod.descriptors.ToolDataDescriptor;
import com.jeffpeng.jmod.interfaces.IArmor;
import com.jeffpeng.jmod.interfaces.IItem;
import com.jeffpeng.jmod.interfaces.IItemColor;
import com.jeffpeng.jmod.interfaces.ITool;
import com.jeffpeng.jmod.primitives.BasicAction;
import com.jeffpeng.jmod.types.items.CoreFood;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@SuppressWarnings("unused")
public class AddItem extends BasicAction{
	private static final String QUALIFIEDCLASSNAMEBASE = "com.jeffpeng.jmod.types.items.";
	
	private String name;
	private String refClass;
	private int stackSize;
	private String tab;
	private ToolDataDescriptor tooldata;
	private ArmorDataDescriptor armordata;
	private FoodDataDescriptor fooddata;
	private ColorDescriptor colordata;
	private Integer colorindex;
	private IItem instance; 
	

	public AddItem(JMODRepresentation owner, String name, String refClass, int stackSize, String tab){
		super(owner);
		this.name = name;
		this.refClass = refClass;
		this.stackSize = stackSize;
		this.tab = tab;
	}
	
	public AddItem tooldata(ToolDataDescriptor tooldata){
		this.tooldata = tooldata;
		return this;
	}
	

	
	public AddItem armordata(ArmorDataDescriptor armordata){
		this.armordata = armordata;
		return this;
	}
	
	public AddItem fooddata(FoodDataDescriptor fooddata){
		this.fooddata = fooddata;
		return this;
	}
	
	public AddItem color(ColorDescriptor color){
		this.colordata = color;
		return this;
	}
	
	public AddItem colorindex(int colorindex){
		this.colorindex = colorindex;
		return this;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean on(FMLPreInitializationEvent event){
		if(JMOD.isDevVersion()) log.info("Creating item creation for " + name);
		try {
			Class<?> clazz;

			if (refClass.contains(".")) {
				clazz = Class.forName(refClass);
			} else {
				clazz = Class.forName(QUALIFIEDCLASSNAMEBASE + refClass);
			}

			if (ITool.class.isAssignableFrom(clazz) && IItem.class.isAssignableFrom(clazz)) {
				Class[] args = new Class[2];
				args[0] = JMODRepresentation.class;
				args[1] = ToolDataDescriptor.class;
				instance = (IItem) clazz.getDeclaredConstructor(args).newInstance(owner,tooldata);
			} else
				
			if (IArmor.class.isAssignableFrom(clazz) && IItem.class.isAssignableFrom(clazz)) {

				Class[] args = new Class[3];
				args[0] = JMODRepresentation.class;
				args[1] = String.class;
				args[2] = String.class;
				instance = (IItem) clazz.getDeclaredConstructor(args).newInstance(owner,armordata.armorMaterial,armordata.armorType);
			} else 
			
			if (CoreFood.class.isAssignableFrom(clazz)) {
				Class[] args = new Class[2];
				args[0] = JMODRepresentation.class;
				args[1] = FoodDataDescriptor.class;
				instance = (IItem) clazz.getDeclaredConstructor(args).newInstance(owner,fooddata);
			} else

			if (IItem.class.isAssignableFrom(clazz)) {
				Class[] args = new Class[1];
				args[0] = JMODRepresentation.class;
				instance = (IItem) clazz.getDeclaredConstructor(args).newInstance(owner);
			} else {
				throw new RuntimeException("[" + owner.getModId() + "] [itemregistry] This item does not implement ISIItem.");
			}
			
			if(instance instanceof IItemColor){
				if(colordata != null) ((IItemColor)instance).setColor(colordata.red,colordata.green,colordata.blue);
				if(colorindex != null) ((IItemColor)instance).setColorIndex(colorindex);
			}

			instance.setName(name);
			
			((Item) instance).setTextureName(instance.getPrefix() + ":" + name);
			((Item) instance).setMaxStackSize(stackSize);
			
			
		} catch (Exception e) {
			log.warn("Could not instantiate " + name + ". Possibly the constructor is malformed?");
			e.printStackTrace();
			return false;
		}

		instance.register();
		return true;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event){
		instance.setRecipes();
		if(!JMOD.isServer()){
			CreativeTabs tabInstance = Lib.getCreativeTabByName(tab);
			if(tabInstance != null && instance != null){
				((Item )instance).setCreativeTab(tabInstance);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public int priority(){
		return 50;
	}
}
