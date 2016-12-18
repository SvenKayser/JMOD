package com.jeffpeng.jmod.actions.applecore;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.modintegration.applecore.AppleCoreModifyFoodValues;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;

public class ModifyFoodValue extends BasicAction {

	private String foodName;
	private int hunger;
	private float saturationModifier;
	private Logger log = LogManager.getLogger("AppleCore Mod Intergration");

	private static String BLACKLIST_FOOD = "BlacklistFood";
	private static String HUNGEROVERHAUL_MODID = "HungerOverhaul";
	
	public ModifyFoodValue(JMODRepresentation owner, String food, int hunger, float saturationModifier) {
		super(owner);
		this.foodName = food;
		this.hunger = hunger;
		this.saturationModifier = saturationModifier;
		
		//Send a message, So that HungerOverhaul does not change my values back..
		if(Loader.isModLoaded(HUNGEROVERHAUL_MODID)) {
			FMLInterModComms.sendMessage(HUNGEROVERHAUL_MODID, BLACKLIST_FOOD, foodName);
		}
				
		log.debug("Food Modify Action - Added - foodName: {}, isValid: {}", foodName, this.valid);
	}

	@Override
	public boolean on(FMLLoadCompleteEvent event){
		valid = Optional.ofNullable(lib.stringToItemStackNoOreDict(foodName)).isPresent();
		if(valid) {
			AppleCoreModifyFoodValues store = AppleCoreModifyFoodValues.getInstance();
			store.addModifedFoodValue(foodName, hunger, saturationModifier);
		}
		
		log.debug("Food Modify Action - LoadComplete - foodItem: {}, valid: {}", foodName, valid); 
		return valid;
	}

}
