package com.jeffpeng.jmod.types.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.jeffpeng.jmod.Config;
import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.interfaces.IItem;

public class CoreItem extends Item implements IItem {
	
	public CreativeTabs creativetab;
	private String internalName;
	protected JMODRepresentation owner;
	protected Config config;
	
	public CoreItem(JMODRepresentation owner){
		this.owner = owner;
		this.config = owner.getConfig();
	}
	
	
	public String getPrefix(){
		return owner.getModId();
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
	public void register(){
		JMOD.DEEPFORGE.registerItem(this, this.internalName, owner.getModId());
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
