package com.jeffpeng.jmod.actions.applecore;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.modintegration.applecore.AppleCoreModifyFoodValues;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import net.minecraft.item.ItemStack;

public class ModifyFoodValue extends BasicAction {

	private String foodName;
	private int hunger;
	private float saturationModifier;
	private Logger log = LogManager.getLogger("AppleCore Mod Intergration");

	public ModifyFoodValue(JMODRepresentation owner, String food, int hunger, float saturationModifier) {
		super(owner);
		this.foodName = food;
		this.hunger = hunger;
		this.saturationModifier = saturationModifier;
		
		this.valid = Optional.ofNullable(lib.stringToItemStackNoOreDict(foodName)).isPresent();
		
		log.debug("Food Modify Action - Added - foodName: {}, isValid: {}", foodName, this.valid);
	}

//	@Override
//	public boolean on(FMLPostInitializationEvent event){
//		ItemStack is = lib.stringToItemStackOrFirstOreDict(foodName);
//		if(is != null){
//			valid = true;
//		} 
//		
//		log.debug("Food Modify Action - PostInit - foodName: {}isValid: {}", foodName, valid);
//		// if(valid) execute();
//		
//		return valid;
//	}
	
	@Override
	public void execute() {
		AppleCoreModifyFoodValues store = AppleCoreModifyFoodValues.getInstance();
		log.debug("Food Modify Action - Execute - foodItem: {}, ", foodName); 
		store.addModifedFoodValue(foodName, hunger, saturationModifier);
	}
}
