package com.jeffpeng.si.core.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import com.jeffpeng.si.core.SILib;
import com.jeffpeng.si.core.interfaces.ISIItem;
import com.jeffpeng.si.core.util.descriptors.BuffDescriptor;
import com.jeffpeng.si.core.util.descriptors.FoodDataDescriptor;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SICoreFood extends ItemFood implements ISIItem {
	
	public CreativeTabs creativetab;
	private String internalName;
	protected List<BuffDescriptor> buffs = new ArrayList<>();

	public SICoreFood(FoodDataDescriptor desc) {
		super(desc.hunger, desc.saturation, desc.wolffood);
		if(desc.alwaysEdible) this.setAlwaysEdible();
		buffs = desc.buffdata;
	}

	
	public String getName(){
		return this.internalName;
	}
	
	protected void onFoodEaten(ItemStack is, World world, EntityPlayer ep)
    {
        if (!world.isRemote)
        	for(BuffDescriptor buff : buffs){
        		if(buff.isValid() && SILib.chance(buff.chance))
       				ep.addPotionEffect(new PotionEffect(buff.getBuff().getId(), buff.duration, buff.level));
        	}
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
	public Item setTextureName(String texname){
		super.setTextureName(texname);
		return this;
	}

	@Override
	public void setName(String name){
		this.internalName = name;
		this.setUnlocalizedName(getPrefix()+"."+name);
	}

}
