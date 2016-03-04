package com.jeffpeng.si.core.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.jeffpeng.si.core.SI;
import com.jeffpeng.si.core.util.descriptors.ItemStackSubstituteDescriptor;

import cpw.mods.fml.common.registry.GameRegistry;

public class SIMetalBlock extends SICoreBlock {

	public IIcon[] icons;

	public SIMetalBlock(Material mat) {
		super(mat);
		this.setStepSound(soundTypeMetal);
		this.icons = new IIcon[SI.CONFIG.metalblocks.size()];

		for (Integer c = 0; c < SI.CONFIG.metalblocks.size(); c++) {
			SI.CONFIG.itemstacksubstitutes.add(new ItemStackSubstituteDescriptor("si.core:" + SI.REGISTRY.blockyfy(SI.CONFIG.metalblocks.get(c)),
					"si.core:blockMetalGeneric:" + c.toString()));
		}

	}

	@Override
	public void registerBlockIcons(IIconRegister reg) {
		for (int c = 0; c < SI.CONFIG.metalblocks.size(); c++) {
			this.icons[c] = reg.registerIcon("si.core:" + SI.REGISTRY.blockyfy(SI.CONFIG.metalblocks.get(c)));
		}
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return this.icons[meta];
	}

	@Override
	public int damageDropped(int meta) {
		return meta;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List itemList) {
		for (int c = 0; c < SI.CONFIG.metalblocks.size(); c++) itemList.add(new ItemStack(item, 1, c));
	}

	@Override
	public void register() {
		super.register();
		for (int c = 0; c < SI.CONFIG.metalblocks.size(); c++) {
			ItemStack itemstack = new ItemStack(this, 1, c);
			GameRegistry.addRecipe(new ShapedOreRecipe(itemstack, "XXX", "XXX", "XXX", 'X', SI.REGISTRY.ingotyfy(SI.CONFIG.metalblocks.get(c))));
			OreDictionary.registerOre(SI.REGISTRY.blockyfy(SI.CONFIG.metalblocks.get(c)), itemstack);
		}

	}
}
