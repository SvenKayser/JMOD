package com.jeffpeng.jmod.types.items;

import org.lwjgl.input.Keyboard;

import com.jeffpeng.jmod.JMODRepresentation;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class ItemIngotMold extends CoreItem{
	public static ItemIngotMold instance;
	
	public ItemIngotMold(JMODRepresentation owner){
		super(owner);
		instance = this;
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public boolean hasContainerItem(){
		return true;
	}
	
	@Override
	public Item getContainerItem(){
		return Item.getItemFromBlock(Blocks.sand);
	}
	
	@SubscribeEvent
	public void onToolTip(ItemTooltipEvent event){
		if(event.itemStack.getItem() instanceof ItemIngotMold){
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)){
				event.toolTip.add(StatCollector.translateToLocal("info." + getPrefix() + ".ItemIngotMold.info1"));
				event.toolTip.add(StatCollector.translateToLocal("info." + getPrefix() + ".ItemIngotMold.info2"));
			} else {
				event.toolTip.add(StatCollector.translateToLocal("info." + getPrefix() + ".shifted"));
			}
		}
	}
	
	@SubscribeEvent
	public void onToss(ItemTossEvent event){
		EntityItem entity = event.entityItem;
		ItemStack eventstack = entity.getEntityItem();
		if(eventstack.getItem() instanceof ItemIngotMold){
			event.entityItem.setEntityItemStack(new ItemStack(Item.getItemFromBlock(Blocks.sand),eventstack.stackSize));
			entity.worldObj.playSoundEffect(entity.posX, entity.posY, entity.posZ, "minecraft:game.player.hurt.fall.big", 1, 1);
		}
	}
	
}
