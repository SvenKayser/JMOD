package com.jeffpeng.si.core.crafting;


import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import com.cricketcraft.chisel.api.IChiselItem;
import com.jeffpeng.si.core.SI;
import com.jeffpeng.si.core.SILib;

public class SIToolRepairRecipe implements IRecipe {
	
	public float DISCOUNT = SI.CONFIG.craftingGridRepairModifier;

	@Override
	public boolean matches(InventoryCrafting ic, World world) {
		int inventorySize = ic.getSizeInventory();
		int items = 0;
		int tools = 0;
		ItemStack tool=null;
		ItemStack repairstack=null;
		ItemStack toolrepair=null;
		//FMLLog.info("TRR Called");
		
		for(int c = 0; c < inventorySize;c++){
			ItemStack is = ic.getStackInSlot(c);
			if(is == null) continue;
			Item item = is.getItem();
			items++;
			if(item instanceof ItemTool || item instanceof ItemHoe || item instanceof ItemSword || item instanceof IChiselItem){
				//FMLLog.info("TRR Is a tool");
				tools++;
				
				tool = is;
			} else {
				repairstack = is;
			}
			if(tools>1) return false;
		}
		
		if(tools != 1 || items != 2|| tool == null || tool.getItemDamage() == 0){
			//FMLLog.info(tools + " " + items);
			//FMLLog.info("TR no match");
			return false;
		}
		
		toolrepair = SILib.getRepairItemStack(tool.getItem());
		if(SILib.belongToSameOreDictEntry(toolrepair, repairstack)){
			return true;
		}
		
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting ic) {
		
		int inventorySize = ic.getSizeInventory();
		float repairamount = 0;
		ItemStack tool=null;
		//FMLLog.info("TRRGet Called");
		
		for(int c = 0; c < inventorySize;c++){
			ItemStack is = ic.getStackInSlot(c);
			if(is == null) continue;
			Item item = is.getItem();
			if(item instanceof ItemTool || item instanceof ItemHoe || item instanceof ItemSword || item instanceof IChiselItem){
				//FMLLog.info("TRR Is a tool");
				tool = is;
			}
		}
		
		repairamount = SILib.getRepairAmount(tool.getItem()) * DISCOUNT*(float)tool.getMaxDamage();
		//FMLLog.info(repairamount + " " + DISCOUNT + " " + tool.getMaxDamage());
		ItemStack returntool = tool.copy();
		returntool.setItemDamage(tool.getItemDamage()-(int)repairamount);
		
		return returntool;
		
	}

	@Override
	public int getRecipeSize() {
		
		return 2;
	}

	@Override
	public ItemStack getRecipeOutput() {
		
		return null;
	}

}
