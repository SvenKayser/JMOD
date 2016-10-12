package com.jeffpeng.jmod.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;
import com.jeffpeng.jmod.primitives.BasicAction;
import com.jeffpeng.jmod.util.Reflector;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;

public class RemoveOreDictionaryEntry extends BasicAction {
	public String itemstack;
	
	public String entry;
	
	public RemoveOreDictionaryEntry(JMODRepresentation owner, String itemstack, String entry){
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
			Object is = lib.stringToItemStack(itemstack);
			if(is instanceof ItemStack){
				Iterator<ItemStack> it = idToStack.get(id).iterator();
				while(it.hasNext()){
					if(Lib.matchItemStacks(it.next(), (ItemStack)is)) it.remove();
				}
			}
		} else 
		
		for(int n = 0; n < idToStack.size(); n++){
			Object is = lib.stringToItemStack(itemstack);
			if(is instanceof ItemStack){
				Iterator<ItemStack> it = idToStack.get(n).iterator();
				while(it.hasNext()){
					if(Lib.matchItemStacks(it.next(), (ItemStack)is)) it.remove();
				}
			}
		}
		
		return true;
	}
	
	@Override
	public int priority(){
		return 105;
	}
}
