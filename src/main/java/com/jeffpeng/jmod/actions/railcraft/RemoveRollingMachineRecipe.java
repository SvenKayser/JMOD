package com.jeffpeng.jmod.actions.railcraft;

import java.util.List;
import java.util.Optional;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import mods.railcraft.api.crafting.RailcraftCraftingManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class RemoveRollingMachineRecipe extends BasicAction {

	private String outputItemStr;

	public RemoveRollingMachineRecipe(JMODRepresentation owner, String outputItemStr) {
		super(owner);
		this.outputItemStr = outputItemStr;
	}

	@Override
	public boolean on(FMLPostInitializationEvent event){
		
		Optional.ofNullable(lib.stringToItemStack(outputItemStr))
				.map(obj -> (ItemStack)obj)
				.ifPresent(stack -> {
					List<IRecipe> list = RailcraftCraftingManager.rollingMachine.getRecipeList();
					
					valid = list.removeIf(r -> r.getRecipeOutput().isItemEqual(stack));
				});
		
		
		return valid;
	}
}
