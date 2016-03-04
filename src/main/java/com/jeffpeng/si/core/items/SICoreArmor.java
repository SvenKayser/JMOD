package com.jeffpeng.si.core.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import com.jeffpeng.si.core.SI;
import com.jeffpeng.si.core.interfaces.ISIArmor;
import com.jeffpeng.si.core.util.descriptors.ColorDescriptor;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SICoreArmor extends ItemArmor implements ISIArmor {
	
	private String internalName;
	public CreativeTabs creativetab;
	private String matName;
	private String armorType;
	
	public SICoreArmor(String mat,String armorType){
		super(ItemArmor.ArmorMaterial.valueOf(mat),2,translateArmorType(armorType));
		this.matName = mat;
		this.armorType = armorType;
	}
	
	private static int translateArmorType(String armorType){
		int armortypeint = 0;
		
		switch(armorType){
			case "helmet":armortypeint = 0;break;
			case "chest":armortypeint = 1;break;
			case "chestplate":armortypeint = 1;break;
			case "leggings":armortypeint = 2;break;
			case "boots":armortypeint = 3;break;
		}
		
		return armortypeint;
	}

	@Override
	public void setName(String name){
		this.internalName = name;
		this.setUnlocalizedName(getPrefix()+"."+name);
	}

	@Override
	public Item setTextureName(String texname){
		//super.setTextureName(texname);
		switch(this.armorType){
			case "helmet":super.setTextureName("si.core:itemHelmetGeneric");break;
			case "chest":super.setTextureName("si.core:itemChestplateGeneric");break;
			case "chestplate":super.setTextureName("si.core:itemChestplateGeneric");break;
			case "leggings":super.setTextureName("si.core:itemLeggingsGeneric");break;
			case "boots":super.setTextureName("si.core:itemBootsGeneric");break;
		}
		
		return this;
	}

	@Override
	public Item setMaxStackSize(int stacksize) {
		// TODO Auto-generated method stub
		return null;
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
	public boolean hasColor(ItemStack is){
		return true;
	}
	
	@Override 
	public int getColor(ItemStack is){
		ColorDescriptor armorColor = SI.CONFIG.colors.get(matName);
		int color = armorColor.red*256*256 + armorColor.green*256 + armorColor.blue;
		return color;
	}

}
