package com.jeffpeng.jmod.types.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;
import com.jeffpeng.jmod.descriptors.ItemStackSubstituteDescriptor;
import com.jeffpeng.jmod.types.blocks.placers.MetalBlockPlacer;

import cpw.mods.fml.common.registry.GameRegistry;

public class MetalBlock extends CoreBlock {

	public IIcon[] icons;
	
	public MetalBlock(JMODRepresentation owner, Material mat) {
		super(owner, mat);
		this.setStepSound(soundTypeMetal);
		this.icons = new IIcon[config.metalblocks.size()];

		for (Integer c = 0; c < config.metalblocks.size(); c++) {
			config.itemstacksubstitutes.add(new ItemStackSubstituteDescriptor(getPrefix() + ":" + Lib.blockyfy(config.metalblocks.get(c)),
					getPrefix() + ":blockMetalGeneric:" + c.toString()));
		}

	}

	@Override
	public void registerBlockIcons(IIconRegister reg) {
		for (int c = 0; c < config.metalblocks.size(); c++) {
			this.icons[c] = reg.registerIcon(getPrefix() + ":" + Lib.blockyfy(config.metalblocks.get(c)));
		}
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		if(meta >= config.metalblocks.size())
		{
			return this.icons[0];
		}
		return this.icons[meta];
	}

	@Override
	public int damageDropped(int meta) {
		return meta;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List itemList) {
		for (int c = 0; c < config.metalblocks.size(); c++) itemList.add(new ItemStack(item, 1, c));
	}

	@Override
	public void register() {
		setPlacer(new MetalBlockPlacer(owner,this));
		super.register();
		for (int c = 0; c < config.metalblocks.size(); c++) {
			ItemStack itemstack = new ItemStack(this, 1, c);
			GameRegistry.addRecipe(new ShapedOreRecipe(itemstack, "XXX", "XXX", "XXX", 'X', Lib.ingotyfy(config.metalblocks.get(c))));
			OreDictionary.registerOre(Lib.blockyfy(config.metalblocks.get(c)), itemstack);
		}

	}
}
