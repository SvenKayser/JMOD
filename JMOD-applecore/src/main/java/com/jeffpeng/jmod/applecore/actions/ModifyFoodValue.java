package com.jeffpeng.jmod.applecore.actions;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.applecore.ModifyFoodValues;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import net.minecraft.item.ItemStack;

public class ModifyFoodValue extends BasicAction {

	UniqueIdentifier uid;
	String foodName;
	int hunger;
	float saturationModifier;
	
	public ModifyFoodValue(JMODRepresentation owner, String food, int hunger, float saturationModifier) {
		super(owner);
		this.foodName = food;
		this.hunger = hunger;
		this.saturationModifier = saturationModifier;
	}

	@Override
	public boolean on(FMLPostInitializationEvent event){
		
		ItemStack stack = (ItemStack) lib.stringToItemStack(foodName);
		
		if(stack != null && stack instanceof ItemStack) {
			uid = GameRegistry.findUniqueIdentifierFor(stack.getItem());
			
			if(uid != null) {
				valid = true;
			}
			
		}
		else {
			valid = false;
		}
		
		return valid;
	}
	
	@Override
	public void execute() {
		ModifyFoodValues store = ModifyFoodValues.getInstance();
		
		store.addModifedFoodValue(uid, hunger, saturationModifier);
	}
}
