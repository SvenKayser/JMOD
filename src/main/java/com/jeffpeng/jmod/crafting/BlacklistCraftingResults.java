package com.jeffpeng.jmod.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.apache.logging.log4j.Logger;

import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.API.Blacklist;
import com.jeffpeng.jmod.API.Blacklist.ICraftingBlacklist;
import com.jeffpeng.jmod.descriptors.ItemStackDescriptor;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameData;

public class BlacklistCraftingResults implements ICraftingBlacklist {
	
	public static final String blacklistmessage = " is blacklisted as a crafting result. This was probably done by the author of the mod this item/block belongs to. Please respect the authors choice to do so.";
	
	private static class BlacklistEntry{
		
		private BlacklistEntry(String owner,String name,int meta){
			this.owner = owner;
			this.name = name;
			this.meta = meta;
		}
		
		private String owner;
		private String name;
		private int meta;
		
		private boolean matches(BlacklistEntry entry){
			return entry.owner == owner && entry.name == name && (entry.meta==meta || meta==Blacklist.WILDCARDMETA || entry.meta == Blacklist.WILDCARDMETA);
		}
		
		
	}
	
	private List<BlacklistEntry> blacklist = new ArrayList<>();
	private List<String> blacklistDomains = new ArrayList<>();
	
	private static BlacklistCraftingResults instance;
	
	public static void init(){
		instance = new BlacklistCraftingResults();
		Blacklist.crafting = instance; 
	}
	
	public static BlacklistCraftingResults getInstance(){
		if(instance == null) init();
		return instance;
	}
	
	private BlacklistCraftingResults(){
		
	}
	
	public static boolean match(String matchstack, Logger log){
		boolean ret = match(matchstack);
		if(ret) log.warn(matchstack+blacklistmessage);
		return ret;
	}
	
	public static boolean match(String matchstack){
		String[] splits = matchstack.split("@");
		splits = splits[0].split(":");
		if(instance.blacklistDomains.contains(splits[0])) return true;
		int meta;
		if(splits.length > 2){
			meta = Integer.parseInt(splits[2]);
		} else {
			meta = Blacklist.WILDCARDMETA;
		}
		BlacklistEntry matchercandidate = new BlacklistEntry(splits[0],splits[1],meta);
		for(BlacklistEntry entry : instance.blacklist){
			if(entry.matches(matchercandidate)){
				return true;
			}
		}
		return false;
		
	}
	
	@Override
	public void blacklist(String owner, String name, int meta){
		if(Loader.instance().activeModContainer().getModId() == owner){
			blacklist.add(new BlacklistEntry(owner,name,meta));
		} else {
			JMOD.LOG.warn("The mod " + Loader.instance().activeModContainer().getModId() + " tried to blacklist an item of the mod " + owner + ". I won't allow that.");
		}
		
	}

	@Override
	public void blacklist(String output) {
		blacklist(new ItemStackDescriptor(null,output).toItemStack());
	}

	@Override
	public void blacklist(ItemStack output) {
		blacklist(output.getItem());
		
	}

	@Override
	public void blacklist(Item output,int meta) {
		String[] namesplit = GameData.getItemRegistry().getNameForObject(output).split(":");
		String owner = namesplit[0];
		String name = namesplit[1];
		blacklist(owner,name,meta);
	}
	
	@Override
	public void blacklist(Item output) {
		blacklist(output,32767);
		
	}

	@Override
	public void blacklist(Block output,int meta) {
		blacklist(Item.getItemFromBlock(output),meta);
		
	}
	
	@Override
	public void blacklist(Block output) {
		blacklist(Item.getItemFromBlock(output),32767);
		
	}
	
	public void blacklistDomain(String domain){
		blacklistDomains.add(domain);
	}
	
	@Override
	public void blacklistMyDomain() {
		blacklistDomain(Loader.instance().activeModContainer().getModId());
		
	}

}
