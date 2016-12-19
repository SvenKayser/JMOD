package com.jeffpeng.jmod.actions.immersiveengineering;

import java.util.Optional;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import net.minecraft.item.ItemStack;

public class RemoveCrusherRecipe extends BasicAction {
	private String outputItemName;

	public RemoveCrusherRecipe(JMODRepresentation owner, String outputItemName) {
		super(owner);
		this.outputItemName = outputItemName;
	}

	@Override
	public boolean on(FMLLoadCompleteEvent event){
		valid = false;
		Optional.ofNullable(lib.stringToItemStack(outputItemName))
				.filter(obj -> obj instanceof ItemStack)
				.map(obj -> (ItemStack)obj)
				.ifPresent(outputIS -> {
					valid = true;
					CrusherRecipe.removeRecipes(outputIS);
				});
		return valid;
	}
}
