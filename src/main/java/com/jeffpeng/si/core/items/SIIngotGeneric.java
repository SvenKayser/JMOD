package com.jeffpeng.si.core.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.jeffpeng.si.core.SI;
import com.jeffpeng.si.core.util.descriptors.ItemStackSubstituteDescriptor;

import cpw.mods.fml.common.registry.GameRegistry;

public class SIIngotGeneric extends SICoreItem {

	public IIcon[] icons;

	public SIIngotGeneric() {
		super();
		icons = new IIcon[SI.CONFIG.metalingots.size()];

		this.setHasSubtypes(true);

		for (Integer c = 0; c < SI.CONFIG.metalingots.size(); c++) {
			SI.CONFIG.itemstacksubstitutes.add(new ItemStackSubstituteDescriptor("si.core:" + SI.REGISTRY.ingotyfy(SI.CONFIG.metalingots.get(c)), "si.core:ingotGeneric:" + c.toString()));
		}
	}

	@Override
	public void registerIcons(IIconRegister reg) {
		for (int c = 0; c < SI.CONFIG.metalingots.size(); c++) {
			this.icons[c] = reg.registerIcon("si.core:" + SI.REGISTRY.ingotyfy(SI.CONFIG.metalingots.get(c)));
		}
	}

	@Override
	public IIcon getIconFromDamage(int meta) {
		return this.icons[meta];
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List itemList) {
		for (int c = 0; c < SI.CONFIG.metalingots.size(); c++) itemList.add(new ItemStack(item, 1, c));
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "item.si.core." + SI.REGISTRY.ingotyfy(SI.CONFIG.metalingots.get(stack.getItemDamage()));
	}

	@Override
	public void register() {
		super.register();
		for (int c = 0; c < SI.CONFIG.metalingots.size(); c++) {
			ItemStack itemstack = new ItemStack(this, 9, c);
			GameRegistry.addRecipe(new ShapelessOreRecipe(itemstack, SI.REGISTRY.blockyfy(SI.CONFIG.metalingots.get(c))));
			itemstack.stackSize = 1;
			OreDictionary.registerOre(SI.REGISTRY.ingotyfy(SI.CONFIG.metalingots.get(c)), itemstack);
		}

	}

}
