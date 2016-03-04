package com.jeffpeng.si.core.util.descriptors;

import net.minecraft.item.ItemStack;
import codechicken.nei.api.API;

import com.jeffpeng.si.core.SILib;
import com.jeffpeng.si.core.interfaces.ISIStagedObject;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;

public class NEIHidingDescriptor implements ISIStagedObject {
	
	private String itemToHide;
	
	public NEIHidingDescriptor(String is){
		itemToHide = is;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event){
		Object is = SILib.stringToItemStack(itemToHide);
		if(is instanceof ItemStack){
			API.hideItem((ItemStack) is);
			return true;
		}
		return false;
	}
	

	@Override
	public boolean isValid() {
		return true;
	}

}
