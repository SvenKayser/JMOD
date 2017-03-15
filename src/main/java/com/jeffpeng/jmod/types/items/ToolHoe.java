package com.jeffpeng.jmod.types.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.descriptors.ToolDataDescriptor;
import com.jeffpeng.jmod.interfaces.IItem;
import com.jeffpeng.jmod.interfaces.ITool;
import com.jeffpeng.jmod.primitives.BasicAction;

public class ToolHoe extends ItemHoe implements ITool, IItem {

	public CreativeTabs creativetab;
	private String internalName;
	private JMODRepresentation owner;
	private int burnTime = 0;
	

	public ToolHoe(JMODRepresentation owner, ToolDataDescriptor desc) {
		super(ToolMaterial.valueOf(desc.toolmat));
		this.owner = owner;
	}

	@Override
	public void setName(String name) {
		this.internalName = name;
		this.setUnlocalizedName(getPrefix() + "." + name);
	}
	
	@Override
	public String getName(){
		return this.internalName;
	}

	
	@Override
	public JMODRepresentation getOwner() {
		return owner;
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
        return !containerItemSticksInCraftingGrid;
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
