package com.jeffpeng.jmod.actions;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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
import com.jeffpeng.jmod.interfaces.ISettingsProcessor;
import com.jeffpeng.jmod.interfaces.ITool;
import com.jeffpeng.jmod.primitives.BasicAction;
import com.jeffpeng.jmod.types.items.CoreFood;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@SuppressWarnings("unused")
public class AddItem extends BasicAction{
	private static int itemcounter = 0;
	private static final String QUALIFIEDCLASSNAMEBASE = "com.jeffpeng.jmod.types.items.";
	
	private String refClass;
	private IItem instance; 
	
	public AddItem(JMODRepresentation owner, String name, String refClass, int stackSize, String tab){
		super(owner);
		this.refClass = refClass;
		set("name",this.refClass+AddItem.itemcounter++);
		set("stacksize",1);
		set("tab",null);
	}

	public AddItem(JMODRepresentation owner, String refClass){
		super(owner);
		this.refClass = refClass;
		set("name",this.refClass+AddItem.itemcounter++);
		set("stacksize",1);
		set("tab",null);
	}
	
	@Deprecated
	public AddItem tooldata(ToolDataDescriptor tooldata){
		set("tooldata",tooldata);
		return this;
	}
	
	@Deprecated
	public AddItem armordata(ArmorDataDescriptor armordata){
		set("armordata",armordata);
		return this;
	}
	
	@Deprecated
	public AddItem fooddata(FoodDataDescriptor fooddata){
		set("fooddata",fooddata);
		return this;
	}
	
	@Deprecated
	public AddItem color(ColorDescriptor color){
		set("colordata",color);
		return this;
	}
	
	@Deprecated
	public AddItem colorindex(int colorindex){
		set("colorindex",colorindex);
		return this;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean on(FMLPreInitializationEvent event){
		if(JMOD.isDevVersion()) log.info("Creating item creation for " + getString("name"));
		try {
			Class<?> clazz;
			
			if(refClass.startsWith(".")){
				clazz = Class.forName(JMOD.ARCHIVEBASE+refClass);
			} else
			if (refClass.contains(".")) {
				clazz = Class.forName(refClass);
			} else {
				clazz = Class.forName(QUALIFIEDCLASSNAMEBASE + refClass);
			}

			if (ITool.class.isAssignableFrom(clazz) && IItem.class.isAssignableFrom(clazz)) {
				if(getObject("tooldata") == null || !(getObject("tooldata") instanceof ToolDataDescriptor)){
					log.warn("Item " + getString("name") + " is a tool but has no tooldata. Omitting.");
					return false;
				}
				Class[] args = new Class[2];
				args[0] = JMODRepresentation.class;
				args[1] = ToolDataDescriptor.class;
				instance = (IItem) clazz.getDeclaredConstructor(args).newInstance(owner,(ToolDataDescriptor) getObject("tooldata"));
			} else
				
			if (IArmor.class.isAssignableFrom(clazz) && IItem.class.isAssignableFrom(clazz)) {
				if(getObject("armordata") == null || !(getObject("armordata") instanceof ArmorDataDescriptor)){
					log.warn("Item " + getString("name") + " is an armor but has no armordata. Omitting.");
					return false;
				}
				ArmorDataDescriptor add = (ArmorDataDescriptor) getObject("armordata");
				Class[] args = new Class[3];
				args[0] = JMODRepresentation.class;
				args[1] = String.class;
				args[2] = String.class;
				instance = (IItem) clazz.getDeclaredConstructor(args).newInstance(owner,add.armorMaterial,add.armorType);
			} else 
			
			if (CoreFood.class.isAssignableFrom(clazz)) {
				if(getObject("fooddata") == null || !(getObject("fooddata") instanceof FoodDataDescriptor)){
					log.warn("Item " + getString("name") + " is a food but has no fooddata. Omitting.");
					return false;
				}
				Class[] args = new Class[2];
				args[0] = JMODRepresentation.class;
				args[1] = FoodDataDescriptor.class;
				instance = (IItem) clazz.getDeclaredConstructor(args).newInstance(owner,(FoodDataDescriptor) getObject("fooddata"));
			} else

			if (IItem.class.isAssignableFrom(clazz)) {
				Class[] args = new Class[1];
				args[0] = JMODRepresentation.class;
				instance = (IItem) clazz.getDeclaredConstructor(args).newInstance(owner);
			} else {
				throw new RuntimeException("[" + owner.getModId() + "] [itemregistry] This item does not implement ISIItem.");
			}
			
			if(instance instanceof IItemColor){
				if(getObject("colordata") != null && getObject("colordata") instanceof ColorDescriptor){
					ColorDescriptor cd = (ColorDescriptor) getObject("colordata");
					((IItemColor)instance).setColor(cd.red,cd.green,cd.blue);
				}
				if(getInt("colorindex") != null) ((IItemColor)instance).setColorIndex(getInt("colorindex"));
			}

			instance.setName(getString("name"));
			
			((Item) instance).setTextureName(instance.getPrefix() + ":" + getString("name"));
			((Item) instance).setMaxStackSize(getInt("stacksize"));
			
			if(instance instanceof ISettingsProcessor)
			{
				((ISettingsProcessor)instance).processSettings(this);
			} else {
				if(this.hasSetting("burntime")) owner.fuelHandler.setBurnTime(new ItemStack((Item)instance), this.getInt("burntime"));
			}
			
			
		} catch (Exception e) {
			log.warn("Could not instantiate " + getString("name") + ". Possibly the constructor is malformed?");
			e.printStackTrace();
			return false;
		}

		instance.register();
		if(instance instanceof IItem){
			((IItem)instance).on(event);
			return true;
		}
		return true;
	}
	
	@Override 
	public boolean on(FMLInitializationEvent event){
		if(instance instanceof IItem){
			((IItem)instance).on(event);
		}

		if(hasSetting("container")){
			String container = getString("container");
			if(container.equals("self")) ((Item)instance).setContainerItem((Item)instance);
			else {
				ItemStack is = lib.stringToItemStackOrFirstOreDict(container);
				if(is != null){
					((Item)instance).setContainerItem(is.getItem());
				}
			}
			
			
		}
		
		
		
		
		return false;
	}
	
	@Override 
	public boolean on(FMLPostInitializationEvent event){
		if(instance instanceof IItem){
			((IItem)instance).on(event);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event){
		instance.setRecipes();
		boolean tabbed = false;
		
		if(!JMOD.isServer() && getString("tab") != null){
			CreativeTabs tabInstance = Lib.getCreativeTabByName(getString("tab"));
			if(tabInstance != null && instance != null){
				((Item )instance).setCreativeTab(tabInstance);
				tabbed = true;
			}
		}
		
		if(instance instanceof IItem){
			((IItem)instance).on(event);
			tabbed = true;
		}
		
		return tabbed;
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public int priority(){
		return Priorities.AddItem;
	}
}
