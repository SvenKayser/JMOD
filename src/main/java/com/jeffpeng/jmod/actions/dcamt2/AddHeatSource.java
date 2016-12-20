package com.jeffpeng.jmod.actions.dcamt2;

import java.util.Optional;
import java.util.function.Predicate;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import mods.defeatedcrow.api.recipe.RecipeRegisterManager;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class AddHeatSource extends BasicAction {

	private String blockStr;

	public AddHeatSource(JMODRepresentation owner, String block) {
		super(owner);
		this.blockStr = block;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event) {
		Optional<ItemStack> blockItem = Optional.ofNullable(lib.stringToItemStackOrFirstOreDict(blockStr));
		
		blockItem.map(ItemStack::getItem)
				 .filter(isItemBlock)
				 .map(item -> ((ItemBlock)item) );
		
		// RecipeRegisterManager.panRecipe.registerHeatSource(block, meta);
		return false;
	}
	
	Predicate<Item> isItemBlock = item -> item instanceof ItemBlock;
	
}
