package com.jeffpeng.si.core.crafting;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

import com.jeffpeng.si.core.SI;
import com.jeffpeng.si.core.SILib;
import com.jeffpeng.si.core.util.descriptors.BlockDropDescriptor;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class SIDropHandler {
	
	@SubscribeEvent(priority=EventPriority.HIGH)
	public void onBreakBlock(BreakEvent event){
		EntityPlayer player = event.getPlayer();
		if(player != null && player.capabilities.isCreativeMode) return;
		
		for(BlockDropDescriptor bdd : SI.CONFIG.blockDrops){
			if(!bdd.isValid() || bdd.mode != "fail") continue;
			
			int blockmeta = event.world.getBlockMetadata(event.x, event.y, event.z);
			
			if(		SILib.chance(bdd.chance) &&
					bdd.block.equals(event.block) &&
					(	bdd.meta == -1 || 
						blockmeta == bdd.meta
					) &&
					(	!bdd.playeronly || 
						(	player != null && 
							!(player instanceof FakePlayer) 
						)
					) 
			){
				if(bdd.mode.equals("fail") && player != null && !event.block.canHarvestBlock(player, blockmeta)){
					if(!event.world.isRemote){
						event.world.spawnEntityInWorld(new EntityItem(event.world,event.x,event.y,event.z,(ItemStack)bdd.itemstack));
					}
					if(bdd.exclusive){
						event.setCanceled(true);
						event.world.setBlockToAir(event.x, event.y, event.z);
					}
				}
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void onHarvestDrops(HarvestDropsEvent event){
		EntityPlayer player = event.harvester;
		if(player != null && player.capabilities.isCreativeMode) return;
		
		for(BlockDropDescriptor bdd : SI.CONFIG.blockDrops){
			if(!bdd.isValid() || bdd.mode == "fail") continue;
			int blockmeta = event.blockMetadata;
			if(SILib.chance(bdd.chance)){
				if(	bdd.block.equals(event.block)){
					if(	bdd.meta == -1 ||	blockmeta == bdd.meta){
						if(!bdd.playeronly || 	(	player != null &&	!(player instanceof FakePlayer))){
							if((bdd.mode.equals("silk") && event.isSilkTouching) || bdd.mode.equals("harvest")){
								if(bdd.exclusive) event.drops.clear();
								int fortune = 0;
								if(!event.isSilkTouching && event.fortuneLevel > 0) fortune = bdd.fortune[Math.max(2,event.fortuneLevel-1)];
								event.drops.add((ItemStack)bdd.itemstack.copy());
								if(fortune > 0){
									while(fortune > 100){
										fortune -= 100; event.drops.add((ItemStack)bdd.itemstack.copy());
									}
									if(fortune>0) if(SILib.chance(fortune)) event.drops.add((ItemStack)bdd.itemstack.copy());
								}
							}
						}
					}
				} 
			}
		}
	}
}
