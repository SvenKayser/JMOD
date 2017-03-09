package com.jeffpeng.jmod.types.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSword;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.descriptors.ToolDataDescriptor;
import com.jeffpeng.jmod.interfaces.IItem;
import com.jeffpeng.jmod.interfaces.ITool;
import com.jeffpeng.jmod.primitives.BasicAction;

public class ToolSword extends ItemSword implements ITool, IItem {

	public CreativeTabs creativetab;
	private String internalName;
	private JMODRepresentation owner;
	private int burnTime = 0;

	public ToolSword(JMODRepresentation owner,ToolDataDescriptor desc) {
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
	public void setRecipes() {
		// TODO Auto-generated method stub

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
