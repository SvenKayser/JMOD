package com.jeffpeng.jmod.actions.dcamt2;

import java.util.List;
import java.util.Optional;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import defeatedcrow.addonforamt.jpaddon.api.IDryerRecipe;
import defeatedcrow.addonforamt.jpaddon.api.RecipeManagerJP;
import net.minecraft.item.ItemStack;

public class RemoveDryingRecipe extends BasicAction {
	private String outputStr;

	public RemoveDryingRecipe(JMODRepresentation owner, String outputStr) {
		super(owner);
		this.outputStr = outputStr;
	}

	@Override
	public boolean on(FMLLoadCompleteEvent event){
		valid = false;
		Optional.ofNullable(lib.stringToItemStack(outputStr))
				.map(obj -> (ItemStack)obj)
				.ifPresent(stack -> {
					List<? extends IDryerRecipe> list = RecipeManagerJP.dryerRecipe.getRecipes();
					list.removeIf(recipe -> 
						recipe.getOutput().isItemEqual(stack)
					);
				});
		return valid;
	}
}
