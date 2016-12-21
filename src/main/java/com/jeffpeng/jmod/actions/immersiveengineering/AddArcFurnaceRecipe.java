package com.jeffpeng.jmod.actions.immersiveengineering;

import java.util.Optional;
import java.util.function.Supplier;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import net.minecraft.item.ItemStack;

public class AddArcFurnaceRecipe extends BasicAction {

	private String outputStack;
	private String inputStack;
	private int time;
	private int energyPerTick;
	private Optional<String> slagItem;
	private String [] additives;
	
	public AddArcFurnaceRecipe(JMODRepresentation owner, String outputStack, String inputStack, int time,
			int energyPerTick, Optional<String> slagItem, String[] additives) {
		super(owner);
		this.outputStack = outputStack;
		this.inputStack = inputStack;
		this.time = time;
		this.energyPerTick = energyPerTick;
		this.slagItem = slagItem;
		this.additives = additives;
	}
	
	@Override
	public boolean on(FMLLoadCompleteEvent event){
		valid = false;
		Optional<ItemStack> inputOpt = lib.stringToMaybeItemStack(inputStack);
		Optional<ItemStack> outputOpt = lib.stringToMaybeItemStack(outputStack);
		ItemStack slag = slagItem.flatMap(lib::stringToMaybeItemStack).orElseGet(IE_Slag);
	
		inputOpt.ifPresent(inputIS -> {
			outputOpt.ifPresent(outputIS -> {
				
				valid = true;
				ArcFurnaceRecipe.addRecipe(outputIS, inputIS, slag, 
										   time, energyPerTick, (Object[])additives);			
				
			});
		});
		
		return valid;
	}
	
	private Supplier<ItemStack> IE_Slag = () -> {
		return lib.stringToMaybeItemStack("ImmersiveEngineering:material:13").get();
	};
}
