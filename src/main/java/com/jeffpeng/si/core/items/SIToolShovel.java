package com.jeffpeng.si.core.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSpade;

import com.jeffpeng.si.core.SI;
import com.jeffpeng.si.core.interfaces.ISIItem;
import com.jeffpeng.si.core.interfaces.ISITool;
import com.jeffpeng.si.core.util.descriptors.ToolDataDescriptor;

import cpw.mods.fml.common.registry.GameRegistry;

public class SIToolShovel extends ItemSpade implements ISITool, ISIItem {

	public CreativeTabs creativetab;
	private String internalName;

	public SIToolShovel(ToolDataDescriptor desc) {
		super(ToolMaterial.valueOf(desc.toolmat));
	}

	@Override
	public String getPrefix() {
		return SI.MODID;
	}

	@Override
	public void setName(String name) {
		this.internalName = name;
		this.setUnlocalizedName(getPrefix() + "." + name);
	}

	@Override
	public void presetCreativeTab(CreativeTabs creativetab) {
		this.creativetab = creativetab;
	}

	@Override
	public void register() {
		GameRegistry.registerItem(this, this.internalName);

	}

	@Override
	public void setCreativeTab() {
		if (creativetab != null)
			super.setCreativeTab(creativetab);
	}

	@Override
	public void setRecipes() {
		// TODO Auto-generated method stub

	}

}
