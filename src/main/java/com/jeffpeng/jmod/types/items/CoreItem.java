package com.jeffpeng.jmod.types.items;

import java.util.Map;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.interfaces.IItem;
import com.jeffpeng.jmod.primitives.BasicAction;

public class CoreItem extends Item implements IItem {
	
	public CreativeTabs creativetab;
	private String internalName;
	protected JMODRepresentation owner;
	protected Map<String,Object> config;
	private int burnTime = 0;
	
	public CoreItem(JMODRepresentation owner){
		this.owner = owner;
		this.config = owner.getConfig();
	}
	
	@Override
	public void setName(String name){
		this.internalName = name;
		this.setUnlocalizedName(getPrefix()+"."+name);
	}
	
	public String getName(){
		return this.internalName;
	}
	
	public JMODRepresentation getOwner(){
		return owner;
	}
	
	@Override 
	public boolean hasOwner(){
		return owner != null;
	}
	
	@Override
	public void setRecipes(){
		
	}

	@Override
	public Item setTextureName(String texname){
		super.setTextureName(texname);
		return this;
	}
	
	@Override
	public void processSettings(BasicAction settings) {
		if(settings.hasSetting("burntime"))		this.burnTime	 = settings.getInt("burntime");
		if(settings.hasSetting("remainsincraftinggrid")) this.containerItemSticksInCraftingGrid = settings.getBoolean("remainsincraftinggrid");
	}
	
	private boolean containerItemSticksInCraftingGrid = false;
	
	@Override
	public boolean doesContainerItemLeaveCraftingGrid(ItemStack is)
    {
        return !this.containerItemSticksInCraftingGrid;
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
