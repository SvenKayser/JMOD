package com.jeffpeng.jmod.actions;

import java.util.List;

import javax.script.Bindings;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;
import com.jeffpeng.jmod.crafting.BlacklistCraftingResults;
import com.jeffpeng.jmod.crafting.StringListRecipe;
import com.jeffpeng.jmod.descriptors.ItemStackDescriptor;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class AddShapedRecipe extends BasicAction{
	
	private ItemStackDescriptor result;
	private List<String[]> shape;
	private StringListRecipe recipe;
	private boolean valid;
	private NBTTagCompound nbt;
	
	
	public AddShapedRecipe(JMODRepresentation owner, ItemStackDescriptor result, List<String[]> shape){
		super(owner);
		this.result = result;
		this.shape = shape;
	}
	
	public AddShapedRecipe setNBT(Bindings b){
		nbt = Lib.bindingsToNBTTagCompound(b);
		return this;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event){
		log.info("##shaped" + owner.getModId());
		if(BlacklistCraftingResults.match(result.toItemStackString(),log))	return this.valid = false;
		ItemStack resultstack = result.toItemStack();
		if(nbt!=null) resultstack.setTagCompound(nbt);
		this.recipe = new StringListRecipe(owner,resultstack,shape);
		this.valid = recipe.isValid();
		if(!this.valid) log.info(result + " is invalid");
		return this.valid;
	}
	
	@Override
	public boolean isValid() {
		return this.valid;
	}

	@Override
	public void execute() {
		GameRegistry.addRecipe(recipe);
	}
	
	@Override
	public int priority(){
		return Priorities.AddShapedRecipe;
	}
}
