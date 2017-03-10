package com.jeffpeng.jmod.crafting;


import java.util.Map;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import com.jeffpeng.jmod.JMODPlugin;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;

public class ToolRepairRecipe implements IRecipe {
	
	private Map<String,Object> config;
	public float DISCOUNT;
	
	public ToolRepairRecipe(JMODRepresentation jmod){
		this.config = jmod.getConfig();
		DISCOUNT =  (float)config.get("craftingGridRepairModifier");
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
			if(item instanceof ItemTool || item instanceof ItemHoe || item instanceof ItemSword || JMODPlugin.isToolCycle(item)){
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
			if(item instanceof ItemTool || item instanceof ItemHoe || item instanceof ItemSword || JMODPlugin.isToolCycle(item)){
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
