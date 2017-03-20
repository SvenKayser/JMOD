package com.jeffpeng.jmod.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.jeffpeng.jmod.API.forgeevents.JMODManageCreativeTabListEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabEntryManager {
	
	public static final Object WILDCARDTAB = new Object(); 
	
	private Map<Object, List<ItemStack>> addList = new HashMap<>();
	private Map<Object, List<ItemStack>> hideList = new HashMap<>();
	
	
	public CreativeTabEntryManager(){
		
	}
	
	public void add(CreativeTabs tab, ItemStack is){
		checkTab(tab);
		addList.get(tab).add(is);
	}
	
	public void add(ItemStack is){
		checkTab(WILDCARDTAB);
		addList.get(WILDCARDTAB).add(is);
	}
	
	public void hide(CreativeTabs tab, ItemStack is){
		checkTab(tab);
		hideList.get(tab).add(is);
	}
	
	public void hide(ItemStack is){
		checkTab(WILDCARDTAB);
		hideList.get(WILDCARDTAB).add(is);
	}
	 
	
	private void checkTab(Object tab){
		
		if(!addList.containsKey(tab)){
			addList.put(tab, new ArrayList<ItemStack>());
		}
		
		if(!hideList.containsKey(tab)){
			hideList.put(tab, new ArrayList<ItemStack>());
		}
	}
	
	private boolean matchIS(ItemStack is1, ItemStack is2){
		if(is1.getItem() == is2.getItem()){
			int is1d = is1.getItemDamage();
			int is2d = is2.getItemDamage();
			if(is1d == is2d || is1d == Short.MAX_VALUE || is2d == Short.MAX_VALUE){
				if(!is1.hasTagCompound() && !is2.hasTagCompound()) return true;
				else {
					if(is1.stackTagCompound.equals(is2.stackTagCompound)) return true;
				}
			}
		}
		return false;
	}
	
	@SubscribeEvent
	public void handler(JMODManageCreativeTabListEvent event){
		if(event.getTab() == CreativeTabs.tabInventory) return;
		List<ItemStack> islist = event.getList();
		addList.get(event.getTab()).forEach((is) -> islist.add(is));
		addList.get(WILDCARDTAB).forEach((is) -> islist.add(is));
		
		Consumer<ItemStack> consumer = (compIs) -> {
			Iterator<ItemStack> targetListIterator = islist.iterator();
			while(targetListIterator.hasNext()){
				ItemStack is = targetListIterator.next();
				if(matchIS(is,compIs)) targetListIterator.remove();
			}
		};
			
		hideList.get(event.getTab()).forEach(consumer);
		hideList.get(WILDCARDTAB).forEach(consumer);
		
	}
}
