package com.jeffpeng.jmod.actions;

import java.util.ArrayList;
import java.util.List;

import javax.script.Bindings;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;
import com.jeffpeng.jmod.crafting.BlacklistCraftingResults;
import com.jeffpeng.jmod.descriptors.ItemStackDescriptor;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class AddShapelessRecipe extends BasicAction {

	public ItemStackDescriptor resultDesc;
	public Bindings ingredients;
	private NBTTagCompound nbt;
	
	public AddShapelessRecipe(JMODRepresentation owner, ItemStackDescriptor resultString, Bindings ingredients){
		super(owner);
		this.resultDesc = resultString;
		this.ingredients = ingredients;
		this.valid = true;
		
	}
	
	public AddShapelessRecipe setNBT(Bindings b){
		nbt = Lib.bindingsToNBTTagCompound(b);
		return this;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event){
		if(BlacklistCraftingResults.match(resultDesc.toString(),log))	return this.valid = false;
		
		return valid;

	}
	
	
	
	@Override
	public void execute(){
		List<Object> ingredientList = new ArrayList<>();
		ingredients.forEach((k,v) -> {
			ItemStackDescriptor isd = null;
			
			if(v instanceof String){
				isd = new ItemStackDescriptor(owner,(String) v);
			}
			
			if(v instanceof ItemStackDescriptor){
				isd = (ItemStackDescriptor)v;
			}
			
			if(isd != null){
				String oreDictName = isd.getAsOreDictEntry();
				if(oreDictName != null){
					int amount = isd.getStackSize();
					for(int c = 0; c < amount; c++) ingredientList.add(oreDictName);
				} else {
					ingredientList.add(isd.toItemStack());
				}
			}
		});

		ItemStack result = resultDesc.toItemStack();
		if (result == null){
			log.warn("Cannot resolve "  + resultDesc + " to an item. Hint: OreDict entries are not valid as craftig results.");
			return;
		}
		ItemStack resultstack = ((ItemStack) result);
		if(nbt!=null) resultstack.setTagCompound(nbt);
		GameRegistry.addRecipe(new ShapelessOreRecipe(resultstack, ingredientList.toArray()));
	}
	
	@Override
	public int priority(){
		return Priorities.AddShapelessRecipe;

	}
}
