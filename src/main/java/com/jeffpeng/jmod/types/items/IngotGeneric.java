package com.jeffpeng.jmod.types.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;
import com.jeffpeng.jmod.descriptors.ItemStackSubstituteDescriptor;

import cpw.mods.fml.common.registry.GameRegistry;

public class IngotGeneric extends CoreItem {

	public IIcon[] icons;

	public IngotGeneric(JMODRepresentation owner) {
		super(owner);
		icons = new IIcon[config.metalingots.size()];

		this.setHasSubtypes(true);

		for (Integer c = 0; c < config.metalingots.size(); c++) {
			config.itemstacksubstitutes.add(new ItemStackSubstituteDescriptor(getPrefix() + ":" + Lib.ingotyfy(config.metalingots.get(c)), getPrefix() + ":ingotGeneric:" + c.toString()));
		}
	}

	@Override
	public void registerIcons(IIconRegister reg) {
		for (int c = 0; c < config.metalingots.size(); c++) {
			this.icons[c] = reg.registerIcon(getPrefix() + ":" + Lib.ingotyfy(config.metalingots.get(c)));
		}
	}

	@Override
	public IIcon getIconFromDamage(int meta) {
		if(meta >= config.metalingots.size())
		{
			return this.icons[0];
		}
		return this.icons[meta];
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List itemList) {
		for (int c = 0; c < config.metalingots.size(); c++) itemList.add(new ItemStack(item, 1, c));
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		int meta = stack.getItemDamage();
		if(meta >= config.metalingots.size()) return "!invalid!";
		
		return "item." + owner.getModId() + "." + Lib.ingotyfy(config.metalingots.get(meta));
	}

	@Override
	public void register() {
		super.register();
		for (int c = 0; c < config.metalingots.size(); c++) {
			ItemStack itemstack = new ItemStack(this, 9, c);
			GameRegistry.addRecipe(new ShapelessOreRecipe(itemstack, Lib.blockyfy(config.metalingots.get(c))));
			itemstack.stackSize = 1;
			OreDictionary.registerOre(Lib.ingotyfy(config.metalingots.get(c)), itemstack);
		}

	}

}
