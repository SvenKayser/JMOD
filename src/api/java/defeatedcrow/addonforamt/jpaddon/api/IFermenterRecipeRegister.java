package defeatedcrow.addonforamt.jpaddon.api;

import java.util.List;

import net.minecraft.item.ItemStack;

public interface IFermenterRecipeRegister {

	/**
	 * DryRackのレシピのうち乾燥レシピ <br>
	 * 稼働条件以外はDrierと相違ない <br>
	 * secondaryはinputの返却容器専用であり任意に登録は出来ない <br>
	 * また、inputには鉱石辞書名を使用できる。"ingotIron"のように、文字列として""で囲めば良い。
	 * 
	 * @param output
	 *            : 完成品
	 * @param time
	 *            : 必要日数
	 * @param input
	 *            : 材料アイテム(ItemStack, String)
	 */
	void addRecipe(ItemStack output, int time, Object input);

	List<? extends IFermenterRecipe> getRecipes();

	IFermenterRecipe getRecipe(ItemStack input);

}
