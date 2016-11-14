package defeatedcrow.addonforamt.jpaddon.api;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public interface IDryerRecipe {

	Object getInput();

	ArrayList<ItemStack> getProcessedInput();

	ItemStack getOutput();

	ItemStack getSecondary(ItemStack in);

	int getTime();

	public boolean matches(ItemStack items);

}
