package com.jeffpeng.jmod.util.actions.applecore;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.modintegration.applecore.AppleCoreModifyFoodValues;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import net.minecraft.item.ItemStack;

public class ModifyFoodValue extends BasicAction {

	String foodUnlocalizedName;
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
		
		ItemStack fs = (ItemStack) lib.stringToItemStack(foodName);
		if(fs != null && fs instanceof ItemStack) {
			foodUnlocalizedName = fs.getUnlocalizedName();
			
			valid = true;
		}
		else {
			valid = false;
		}
		
		return valid;
	}
	
	@Override
	public void execute() {
		AppleCoreModifyFoodValues store = AppleCoreModifyFoodValues.getInstance();
		
		store.addModifedFoodValue(foodUnlocalizedName, hunger, saturationModifier);
	}
}
