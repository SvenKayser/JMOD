package com.jeffpeng.jmod.types.items;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.interfaces.IItem;
import com.jeffpeng.jmod.interfaces.ITool;
import com.jeffpeng.jmod.primitives.BasicAction;
import com.jeffpeng.jmod.descriptors.ToolDataDescriptor;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemShears;

public class ToolShears extends ItemShears implements ITool, IItem{

	public CreativeTabs creativetab;
	private String internalName;
	private JMODRepresentation owner;
	private ToolMaterial toolMat;
	private int burnTime = 0;

	public ToolShears(JMODRepresentation owner,ToolDataDescriptor desc) {
		toolMat = ToolMaterial.valueOf(desc.toolmat);
		this.setMaxDamage(toolMat.getMaxUses());
		this.owner = owner;
	}
	
	@Override
	public String getPrefix() {
		return owner.getModId();
	}

	@Override
	public void setName(String name) {
		this.internalName = name;
		this.setUnlocalizedName(getPrefix() + "." + name);
	}

	@Override
	public void setRecipes() {

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
  public int getItemEnchantability()
  {
    return this.toolMat.getEnchantability();
  }

	@Override
	public JMODRepresentation getOwner() {
		return owner;
	}

	@Override
	public String getName() {
		return this.internalName;
	}
	
	@Override
	public void setBurnTime(int bt){
		this.burnTime = bt;
	}

}
