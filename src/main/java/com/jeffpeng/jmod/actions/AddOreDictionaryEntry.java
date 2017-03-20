package com.jeffpeng.jmod.actions;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.descriptors.ItemStackDescriptor;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class AddOreDictionaryEntry extends BasicAction {
	public ItemStackDescriptor itemstackdescriptor;
	
	public String entry;
	boolean done = false;
	
	public AddOreDictionaryEntry(JMODRepresentation owner, ItemStackDescriptor itemstack, String entry){
		super(owner);
		this.itemstackdescriptor = itemstack;
		this.entry = entry;
		this.valid = true;
	}
	
	@Override
	public boolean on(FMLPreInitializationEvent event){
		log.info("oredict for "+itemstackdescriptor+" -> " +entry+", first pass");
		
		List<ItemStack> isl = itemstackdescriptor.getItemStackList();
		
		if(isl.size() > 0){
			isl.forEach((v)-> OreDictionary.registerOre(entry, v));
			log.info("Adding " + itemstackdescriptor + " to "+ entry);
			return done = true;
		} else {
			log.info("Tying to oredict " + itemstackdescriptor + ", but it is not a currently present itemstack (yet?). Postponing for 2nd pass.");
		}
		
		return done;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event){
		if(done) return true;
		log.info("oredict for "+itemstackdescriptor+" -> " +entry+", second pass");
		
		List<ItemStack> isl = itemstackdescriptor.getItemStackList();
		if(isl.size() > 0){
			isl.forEach((v)-> OreDictionary.registerOre(entry, v));
			log.info("Adding " + itemstackdescriptor + " to "+ entry);
		} else {
			log.info("Trying to oredict " + itemstackdescriptor + ", but it is missing or not a proper itemstack/oredict descriptor.");
			return false;
		}
		return true;
	}
	
	@Override
	public int priority(){
		return Priorities.AddOreDictionaryEntry;
	}
}
