package com.jeffpeng.jmod.chisel;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import team.chisel.init.ChiselItems;
import team.chisel.item.chisel.ItemChisel;

import com.cricketcraft.chisel.api.IChiselItem;
import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.JMODPlugin;
import com.jeffpeng.jmod.JMODPluginContainer;
import com.jeffpeng.jmod.crafting.ToolUnbreaker;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;

public class Plugin extends JMODPlugin{

	public Plugin(JMODPluginContainer container) {
		super(container);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public ItemStack getRepairItemStack(Item item){
		if(item instanceof IChiselItem) {
			if(Loader.isModLoaded("chisel") && item instanceof ItemChisel){
				if(item.equals(ChiselItems.chisel)) return new ItemStack(Items.iron_ingot);
				if(item.equals(ChiselItems.diamondChisel)) return new ItemStack(Items.diamond);
				if(item.equals(ChiselItems.obsidianChisel)) return new ItemStack(Blocks.obsidian);
			} else {
				if(item instanceof ToolChisel){
					return ((ToolChisel)item).getRepairItemStack();
				}
			}
		}
		return null;
	}
	
	public void on(FMLInitializationEvent event) {
		if(JMOD.GLOBALCONFIG.preventToolBreaking) 	MinecraftForge.EVENT_BUS.register(new ToolUnbreaker());
	}
	
	@Override
	public Float getRepairAmount(Item item){
		if(item instanceof IChiselItem) return 1.0F;
		return null;
	}
	
	@Override
	public boolean isTool(Item item){
		return item instanceof IChiselItem;
	}
}
