package com.jeffpeng.jmod.actions;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.registry.GameRegistry;


public class AddBlockDrop extends BasicAction {
	
	public Block block;
	public ItemStack itemstack;
	public int meta;
	public String blockString;
	public String itemstackString;
	public Integer chance;
	public Boolean exclusive;
	public Boolean playeronly;
	public String mode = "harvest";
	public int[] fortune = {33,67,100};
	
	public AddBlockDrop(JMODRepresentation owner, String block,String itemstack, Integer chance, Boolean exclusive, Boolean playeronly){
		super(owner);
		this.blockString = block;
		this.itemstackString = itemstack;
		this.chance = chance;
		this.exclusive = exclusive;
		this.playeronly = playeronly;
	}
	
	public void setHarvestFail(){
		this.mode = "fail";
	}
	
	public void setSilkTouch(){
		this.mode = "silk";
	}
	
	public void setFortune(int a, int b, int c){
		this.fortune = new int[] {a,b,c};
	}

	@Override
	public boolean on(FMLLoadCompleteEvent event){
		
		String[] split = blockString.split(":");
		meta = 0;
		if(split.length == 3)
		{
			if(split[2].equals("*")) meta = -1;
			else meta = Integer.parseInt(split[2]);
		}
		
		block = GameRegistry.findBlock(split[0], split[1]);
		
		if(block == null) return false;
		
		Object is = lib.stringToItemStack(itemstackString);
		
		if(is instanceof ItemStack){
			itemstack = (ItemStack)is;
			valid = true;
			return true;
		} else return false;
	}
}
