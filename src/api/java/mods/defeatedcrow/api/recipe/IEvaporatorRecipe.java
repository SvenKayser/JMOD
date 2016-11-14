package mods.defeatedcrow.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IEvaporatorRecipe {

	ItemStack getInput();

	ItemStack getOutput();

	FluidStack getSecondary();

	boolean returnContainer();

}
