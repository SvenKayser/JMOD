package com.jeffpeng.jmod.crafting;

import java.util.HashMap;
import java.util.Map;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.interfaces.IFurnaceFuel;
import com.jeffpeng.jmod.interfaces.IItem;
import com.jeffpeng.jmod.primitives.OwnedObject;
import com.jeffpeng.jmod.types.blocks.CoreBlock;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.IFuelHandler;

public class FuelHandler implements IFuelHandler {
	private Map<Integer,Integer> fuelList = new HashMap<>();
	
	public void setBurnTime(ItemStack is, int burntime){
		Item item = is.getItem();
		Block block = Block.getBlockFromItem(item);
		
		if(block != null && block instanceof IFurnaceFuel){
			((IFurnaceFuel)block).setBurnTime(burntime);
			return;
		}
		
		Item.getIdFromItem(item);
		
		fuelList.put(Item.getIdFromItem(item), burntime);
	}

	@Override
	public int getBurnTime(ItemStack fuel) {
	
		Item item = fuel.getItem();
		Block block = Block.getBlockFromItem(item);
		
		if(block != null && block instanceof IFurnaceFuel){
			return ((IFurnaceFuel)block).getBurnTime();
		}
		
		if(item instanceof IFurnaceFuel){
			return ((IItem)item).getBurnTime();
		} else {
			int id = Item.getIdFromItem(item);
			if(fuelList.containsKey(id)) return fuelList.get(id);
			else return 0;
		}
	}
}
