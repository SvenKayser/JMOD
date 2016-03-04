package com.jeffpeng.si.core.crafting;

import iguanaman.hungeroverhaul.config.IguanaConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;

import com.cricketcraft.chisel.api.IChiselItem;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class SIToolUnbreaker {
	
	private int hoeUseCost = 1;
	
	public SIToolUnbreaker(){
		if(Loader.isModLoaded("HungerOverhaul")){
			hoeUseCost = IguanaConfig.hoeToolDamageMultiplier;
		}
	}

	@SubscribeEvent
	public void onItemBreak(PlayerDestroyItemEvent event){
		FMLLog.info("breakevent");
		if(event.original.getItem() instanceof ItemTool ||event.original.getItem() instanceof ItemHoe /*|| event.original.getItem() instanceof ItemSword*/){
			
			ItemStack retainedItem = event.original.copy();
			retainedItem.setItemDamage(retainedItem.getMaxDamage());
			
			FMLLog.info(retainedItem.getUnlocalizedName() + retainedItem.getItemDamage());
			
			
			
			if(!event.entityPlayer.worldObj.isRemote){
				event.entityPlayer.setCurrentItemOrArmor(0, retainedItem);
				event.entityPlayer.inventory.markDirty();
				event.entityPlayer.inventoryContainer.detectAndSendChanges();
			}
				
			if(event.entityPlayer.worldObj.isRemote){
				Minecraft.getMinecraft().playerController.sendUseItem(event.entityPlayer, event.entityPlayer.worldObj, retainedItem);
			}
			
			FMLLog.info("break");
		}
	}
	
	@SubscribeEvent
	public void onBreakSpeed(BreakSpeed event){
		EntityPlayer player = event.entityPlayer;
		if(player != null){
			ItemStack equipped = player.getCurrentEquippedItem();
			if(equipped == null) return;
			if(equipped.isItemStackDamageable() && equipped.getMaxDamage() <= equipped.getItemDamage()){
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public void onTooltipBrokenTool(ItemTooltipEvent event){
		if(event.itemStack.isItemStackDamageable() && event.itemStack.getMaxDamage() <= event.itemStack.getItemDamage()){
			event.toolTip.add(StatCollector.translateToLocal("info.si.core.tooltips.brokentool"));
		}
		
		if(event.itemStack.isItemStackDamageable() && event.itemStack.getItem() instanceof ItemHoe && (event.itemStack.getMaxDamage() - event.itemStack.getItemDamage() <= hoeUseCost)){
			event.toolTip.add(StatCollector.translateToLocal("info.si.core.tooltips.brokenhoe"));
		}
	}
	
	@SubscribeEvent
	public void onUseHoe(UseHoeEvent event){
		
		EntityPlayer player = event.entityPlayer;
		if(player != null){
			ItemStack equipped = player.getCurrentEquippedItem();
			if(equipped.isItemStackDamageable() && (equipped.getMaxDamage() - equipped.getItemDamage() <= hoeUseCost)){
				event.setCanceled(true);
			}
		}
	}
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
	
	@SubscribeEvent
	public void onAttackEntity(LivingAttackEvent event){
		if(event.source.getEntity() instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)event.source.getEntity();
			if(player != null){
				ItemStack equipped = player.getCurrentEquippedItem();
				
				if(equipped != null && equipped.isItemStackDamageable() && equipped.getMaxDamage() <= equipped.getItemDamage()){
					event.setCanceled(true);
				}
			}
		}
	}
}
