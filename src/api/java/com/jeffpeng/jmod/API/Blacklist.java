package com.jeffpeng.jmod.API;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Blacklist {
	
	public static final int WILDCARDMETA = 32767;

	public static ICraftingBlacklist crafting;
	
	public interface ICraftingBlacklist{
		public void blacklist(String output);
		public void blacklist(String owner, String name, int meta);
		public void blacklist(ItemStack output);
		public void blacklist(Item output);
		public void blacklist(Item output, int meta);
		public void blacklist(Block output);
		public void blacklist(Block output, int meta);
		
		public void blacklistMyDomain();

		
		
		
	}
	
	
}
