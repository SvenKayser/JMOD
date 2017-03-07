package com.jeffpeng.jmod.chisel;

import com.cricketcraft.chisel.api.IChiselItem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;



import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ToolUnbreaker {
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onUseChisel(PlayerInteractEvent event){
		EntityPlayer player = event.entityPlayer;
		if(player != null){
			ItemStack equipped = player.getCurrentEquippedItem();
			if(equipped == null) return;
			
			if(equipped.getItem() instanceof IChiselItem && equipped.isItemStackDamageable() && equipped.getMaxDamage() <= equipped.getItemDamage()){
				event.setCanceled(true);
			}
		}
	}
}
