package com.jeffpeng.jmod.types.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;
import com.jeffpeng.jmod.descriptors.BuffDescriptor;
import com.jeffpeng.jmod.descriptors.FoodDataDescriptor;
import com.jeffpeng.jmod.interfaces.IItem;
import com.jeffpeng.jmod.primitives.BasicAction;

public class CoreFood extends ItemFood implements IItem {
	
	public CreativeTabs creativetab;
	private String internalName;
	protected List<BuffDescriptor> buffs = new ArrayList<>();
	private JMODRepresentation owner;
	private int burnTime = 0;

	public CoreFood(JMODRepresentation owner, FoodDataDescriptor desc) {
		super(desc.hunger, desc.saturation, desc.wolffood);
		this.owner = owner;
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
        		if(buff.isValid() && Lib.chance(buff.chance))
       				ep.addPotionEffect(new PotionEffect(buff.getBuff().getId(), buff.duration, buff.level));
        	}
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

	@Override
	public JMODRepresentation getOwner() {
		return owner;
	}
	
	@Override
	public void processSettings(BasicAction settings) {
		if(settings.hasSetting("burntime"))		this.burnTime	 = settings.getInt("burntime") & 15;
		
	}
	
	@Override 
	public int getBurnTime(){
		return this.burnTime;
	}
	
	@Override
	public void setBurnTime(int bt){
		this.burnTime = bt;
	}

}
