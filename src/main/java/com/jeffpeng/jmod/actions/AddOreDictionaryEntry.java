package com.jeffpeng.jmod.actions;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class AddOreDictionaryEntry extends BasicAction {
	public String itemstack;
	
	public String entry;
	boolean done = false;
	
	public AddOreDictionaryEntry(JMODRepresentation owner, String itemstack, String entry){
		super(owner);
		this.itemstack = itemstack;
		this.entry = entry;
		this.valid = true;
	}
	
	@Override
	public boolean on(FMLPreInitializationEvent event){
		log.info("oredict for "+itemstack+" -> " +entry+", first pass");
		
		Object is = lib.stringToItemStack(itemstack);
		if(is instanceof ItemStack){
			OreDictionary.registerOre(entry, (ItemStack)is);
			log.info("Adding " + itemstack + " to "+ entry);
			done = true;
		} else {
			log.info("Tying to oredict " + itemstack + ", but it is not a currently present itemstack (yet?). Postponing for 2nd pass.");
		}
		
		return done;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event){
		if(done) return true;
		log.info("oredict for "+itemstack+" -> " +entry+", second pass");
		
		Object is = lib.stringToItemStack(itemstack);
		if(is instanceof ItemStack){
			OreDictionary.registerOre(entry, (ItemStack)is);
			log.info("Adding " + itemstack + " to "+ entry);
		} else 
			
		if(is instanceof String && !((String) is).contains(":"))
		{
			log.info("Trying crossmatching of " + (String)is + " to "+ entry);
			if(OreDictionary.getOres((String)is).size() > 0){
				log.info("Crossmatching " + (String)is + " to "+entry);
				for(ItemStack listItem : OreDictionary.getOres((String)is)){
					log.info("Registering " + listItem.getItem().getUnlocalizedName() + " to " + entry + " because of it's membership to "+(String)is);
					OreDictionary.registerOre(entry, listItem);
				}
			}
		} else {
			log.info("Trying to oredict " + itemstack + ", but it is missing or not a proper itemstack/oredict descriptor.");
			return false;
		}
		
		return true;
			
	}
	
	@Override
	public int priority(){
		return 100;
	}
}
