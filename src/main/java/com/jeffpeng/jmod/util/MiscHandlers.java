package com.jeffpeng.jmod.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MiscHandlers {
	@SubscribeEvent
	public void showNBTToolTip(ItemTooltipEvent event){
		ItemStack itemstack = event.itemStack;
		if(itemstack.hasTagCompound() && 
				(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT  ) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT  )) &&
				(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))
		){
			event.toolTip.add("NBTdata:");
			NBTTagCompound nbt = itemstack.getTagCompound();
			event.toolTip.add(nbt.toString());
		}
	}
	
	
}
