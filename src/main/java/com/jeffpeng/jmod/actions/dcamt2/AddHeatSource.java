package com.jeffpeng.jmod.actions.dcamt2;

import java.util.Optional;
import java.util.function.Predicate;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import mods.defeatedcrow.api.recipe.RecipeRegisterManager;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class AddHeatSource extends BasicAction {

	private String blockStr;
	private int meta;

	public AddHeatSource(JMODRepresentation owner, String block, Optional<Integer> meta) {
		super(owner);
		this.blockStr = block;
		this.meta = meta.orElse(0);
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event) {
		valid = false;
		Optional<ItemStack> blockItem = Optional.ofNullable(lib.stringToItemStackOrFirstOreDict(blockStr));
		
		blockItem.filter(Lib::itemStackIsBlockImpl)
				 .map(ItemStack::getItem)
				 .map(Block::getBlockFromItem)
				 .ifPresent(block -> {
					 RecipeRegisterManager.panRecipe.registerHeatSource(block, meta);
				 });
		
		return valid;
	}
	
	Predicate<Item> isItemBlock = item -> item instanceof ItemBlock;
	
}
