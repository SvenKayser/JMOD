package com.jeffpeng.si.core.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.jeffpeng.si.core.SI;
import com.jeffpeng.si.core.interfaces.ISIItem;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SICoreItem extends Item implements ISIItem {
	
	public CreativeTabs creativetab;
	private String internalName;
	
	public SICoreItem(){
		
	}
	
	
	public String getPrefix(){
		return SI.MODID;
	}
	
	@Override
	public void setName(String name){
		this.internalName = name;
		this.setUnlocalizedName(getPrefix()+"."+name);
	}
	
	public String getName(){
		return this.internalName;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void presetCreativeTab(CreativeTabs creativetab){
		this.creativetab = creativetab;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void setCreativeTab(){
		if(creativetab != null) super.setCreativeTab(creativetab);
	}
	
	@Override
	public void register(){
		GameRegistry.registerItem(this, this.internalName);
	}
	
	@Override
	public void setRecipes(){
		
	}

	@Override
	public Item setTextureName(String texname){
		super.setTextureName(texname);
		return this;
	}
}
