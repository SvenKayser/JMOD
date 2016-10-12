package com.jeffpeng.jmod.types.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSword;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.descriptors.ToolDataDescriptor;
import com.jeffpeng.jmod.interfaces.IItem;
import com.jeffpeng.jmod.interfaces.ITool;

public class ToolSword extends ItemSword implements ITool, IItem {

	public CreativeTabs creativetab;
	private String internalName;
	private JMODRepresentation owner;

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

}
