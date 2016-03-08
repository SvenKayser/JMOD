package com.jeffpeng.jmod.crafting;


import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import com.cricketcraft.chisel.api.IChiselItem;
import com.jeffpeng.jmod.Config;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;

public class ToolRepairRecipe implements IRecipe {
	
	private Config config;
	public float DISCOUNT;
	
	public ToolRepairRecipe(JMODRepresentation jmod){
		this.config = jmod.getConfig();
		DISCOUNT =  config.craftingGridRepairModifier;
	}
	
	 

	@Override
	public boolean matches(InventoryCrafting ic, World world) {
		int inventorySize = ic.getSizeInventory();
		int items = 0;
		int tools = 0;
		ItemStack tool=null;
		ItemStack repairstack=null;
		ItemStack toolrepair=null;

		
		for(int c = 0; c < inventorySize;c++){
			ItemStack is = ic.getStackInSlot(c);
			if(is == null) continue;
			Item item = is.getItem();
			items++;
			if(item instanceof ItemTool || item instanceof ItemHoe || item instanceof ItemSword || item instanceof IChiselItem){
				tools++;
				
				tool = is;
			} else {
				repairstack = is;
			}
			if(tools>1) return false;
		}
		
		if(tools != 1 || items != 2|| tool == null || tool.getItemDamage() == 0){
			return false;
		}
		
		toolrepair = Lib.getRepairItemStack(tool.getItem());
		if(Lib.belongToSameOreDictEntry(toolrepair, repairstack)){
			return true;
		}
		
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting ic) {
		
		int inventorySize = ic.getSizeInventory();
		float repairamount = 0;
		ItemStack tool=null;
		for(int c = 0; c < inventorySize;c++){
			ItemStack is = ic.getStackInSlot(c);
			if(is == null) continue;
			Item item = is.getItem();
			if(item instanceof ItemTool || item instanceof ItemHoe || item instanceof ItemSword || item instanceof IChiselItem){
				tool = is;
			}
		}
		
		repairamount = Lib.getRepairAmount(tool.getItem()) * DISCOUNT*(float)tool.getMaxDamage();
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
