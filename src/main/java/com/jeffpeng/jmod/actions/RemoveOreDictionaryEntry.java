package com.jeffpeng.jmod.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;
import com.jeffpeng.jmod.descriptors.ItemStackDescriptor;
import com.jeffpeng.jmod.primitives.BasicAction;
import com.jeffpeng.jmod.util.Reflector;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;

public class RemoveOreDictionaryEntry extends BasicAction {
	public ItemStackDescriptor itemstack;
	
	public String entry;
	
	public RemoveOreDictionaryEntry(JMODRepresentation owner, ItemStackDescriptor itemstack, String entry){
		super(owner);
		this.itemstack = itemstack;
		this.entry = entry;
		this.valid = true;
	}
	
	public boolean on(FMLLoadCompleteEvent event){
		
		@SuppressWarnings("unchecked")
		List<ArrayList<ItemStack>> idToStack = (List<ArrayList<ItemStack>>) new Reflector(null,OreDictionary.class).get("idToStack");
		
		if(entry != null){
			int id = OreDictionary.getOreID(entry);
			List<ItemStack> isl = itemstack.getItemStackList();
			isl.forEach((v) -> {
				Iterator<ItemStack> it = idToStack.get(id).iterator();
				while(it.hasNext()){
					if(Lib.matchItemStacks(it.next(), v)) it.remove();
				}
			});
		} else 
		
		for(int n = 0; n < idToStack.size(); n++){
			final int nx = n;
			List<ItemStack> isl = itemstack.getItemStackList();
			isl.forEach((v) -> {
				Iterator<ItemStack> it = idToStack.get(nx).iterator();
				while(it.hasNext()){
					if(Lib.matchItemStacks(it.next(), v)) it.remove();
				}
			});
		}
		return true;
	}
	
	@Override
	public int priority(){
		return Priorities.RemoveOreDictionaryEntry;
	}
}
