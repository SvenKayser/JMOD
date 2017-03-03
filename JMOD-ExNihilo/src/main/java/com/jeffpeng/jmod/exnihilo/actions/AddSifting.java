package com.jeffpeng.jmod.exnihilo.actions;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import exnihilo.registries.SieveRegistry;

public class AddSifting extends BasicAction{
	private String output;
	private String input;
	private int rarity;
	private ItemStack outputstack;
	private List<ItemStack> inputlist = new ArrayList<>();
	
	public AddSifting(JMODRepresentation owner,String output, String input,int rarity) {
		super(owner);
		
		log.info("addsifting "+output+" "+input+" "+rarity);
		
		if(output == null && input == null){
			log.info("[ExNihilo/AddSifting] Cannot add sifting from or to null stacks. Really.");
			this.valid = false;
			return;
		}

		this.output = output;
		this.input = input;
		this.rarity = rarity;
		this.valid = true;
		
		
		
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event){
		if(!valid) return false;
			
		
		Object ois = lib.stringToItemStack(output);
		Object iis = lib.stringToItemStack(input);
		
		if(ois instanceof ItemStack){
			outputstack = (ItemStack)ois;
		} else {
			if(ois instanceof String && OreDictionary.doesOreNameExist((String)ois))	outputstack = lib.getFirstOreDictMatch(output);
			
			if(outputstack == null) {
				log.warn(ois.toString() + "[ExNihilo/AddSifting] Output "+(String)output+" is invalid since it's an empty ore list.");
				valid = false;
			}
		}
		
		if(iis instanceof ItemStack){
			Block iBlock = lib.getBlockFromItemStack((ItemStack)iis);
			if(iBlock == null){
				log.warn(iis.toString() + "[ExNihilo/AddSifting] Input "+(String)input+" is not a block.");
				valid = false;
			} else {
				inputlist.add((ItemStack)iis);
			}
		
		} else {
			if(iis instanceof String && OreDictionary.doesOreNameExist((String)iis)){
				List<ItemStack> oreDictInputs = OreDictionary.getOres((String)iis, false);
				for(ItemStack oreDictInput : oreDictInputs) if(lib.itemStackIsBlock(oreDictInput)) inputlist.add(oreDictInput);
			}
			
			if(inputlist.size() == 1) {
				log.warn(ois.toString() + "[ExNihilo/AddSifting] Input "+(String)input+" is invalid.");
				valid = false;
			}
		}
		
		if(valid) execute();
		
		return valid;
	}
	
	@Override
	public void execute(){
		for(ItemStack is : inputlist){
			SieveRegistry.register(lib.getBlockFromItemStack(is),outputstack.getItem(),outputstack.getItemDamage(),rarity);
		}
	}
	
	

}
