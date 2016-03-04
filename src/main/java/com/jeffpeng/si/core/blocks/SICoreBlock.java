package com.jeffpeng.si.core.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;

import com.jeffpeng.si.core.SI;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SICoreBlock extends Block {

	private Class<ItemBlock> placer;
	private String internalName;

	protected CreativeTabs creativetab = null;

	public SICoreBlock(Material mat) {
		super(mat);
	}

	public String getPrefix() {
		return SI.MODID;
	}

	@SideOnly(Side.CLIENT)
	public void presetCreativeTab(CreativeTabs creativetab) {
		this.creativetab = creativetab;
	}
	
	@SideOnly(Side.CLIENT)
	public void setCreativeTab() {
		if (creativetab != null)
			super.setCreativeTab(creativetab);
	}

	public void setPlacer(Class<ItemBlock> placer) {
		this.placer = placer;
	}

	public void setName(String name) {
		this.internalName = name;
		this.setBlockName(getPrefix() + "." + name);
	}

	public String getName() {
		return this.internalName;
	}

	public boolean hasPlacer() {
		if (this.placer != null) {
			return true;
		}
		return false;
	}

	public void register() {
		if (this.placer != null) {
			GameRegistry.registerBlock(this, placer, this.internalName);
		} else
			GameRegistry.registerBlock(this, this.internalName);
	}

}
