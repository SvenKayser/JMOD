package com.jeffpeng.jmod.types.items;

import java.util.ArrayList;
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

import cpw.mods.fml.common.registry.GameRegistry;
@SuppressWarnings("unchecked")
public class IngotGeneric extends CoreItem {

	public IIcon[] icons;
	public IngotGeneric(JMODRepresentation owner) {
		super(owner);
		
		icons = new IIcon[((ArrayList<String>)config.get("metalingots")).size()];

		this.setHasSubtypes(true);
	}

	@Override
	public void registerIcons(IIconRegister reg) {
		for (int c = 0; c < ((ArrayList<String>)config.get("metalingots")).size(); c++) {
			this.icons[c] = reg.registerIcon(getPrefix() + ":" + Lib.ingotyfy(((ArrayList<String>)config.get("metalingots")).get(c)));
		}
	}

	@Override
	public IIcon getIconFromDamage(int meta) {
		if(meta >= ((ArrayList<String>)config.get("metalingots")).size())
		{
			return this.icons[0];
		}
		return this.icons[meta];
	}

	@SuppressWarnings({"rawtypes" })
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List itemList) {
		for (int c = 0; c < ((ArrayList<String>)config.get("metalingots")).size(); c++) itemList.add(new ItemStack(item, 1, c));
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		int meta = stack.getItemDamage();
		if(meta >= ((ArrayList<String>)config.get("metalingots")).size()) return "!invalid!";
		
		return "item." + owner.getModId() + "." + Lib.ingotyfy(((ArrayList<String>)config.get("metalingots")).get(meta));
	}

	@Override
	public void register() {
		super.register();
		for (int c = 0; c < ((ArrayList<String>)config.get("metalingots")).size(); c++) {
			ItemStack itemstack = new ItemStack(this, 9, c);
			GameRegistry.addRecipe(new ShapelessOreRecipe(itemstack, Lib.blockyfy(((ArrayList<String>)config.get("metalingots")).get(c))));
			itemstack.stackSize = 1;
			OreDictionary.registerOre(Lib.ingotyfy(((ArrayList<String>)config.get("metalingots")).get(c)), itemstack);
		}

	}

}
