package com.jeffpeng.jmod.modintegration.applecore;

import java.util.HashMap;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import squeek.applecore.api.food.FoodEvent;
import squeek.applecore.api.food.FoodValues;


public class AppleCoreModifyFoodValues {

    private static AppleCoreModifyFoodValues instance = new AppleCoreModifyFoodValues();

    
	private Logger log;
    
    private AppleCoreModifyFoodValues(){
		this.log = LogManager.getLogger("AppleCore Mod Intergration");
    }

    public static AppleCoreModifyFoodValues getInstance(){
    	return instance;
    }
	
	private HashMap<String, FoodValues> foodItems = new HashMap<String, FoodValues>();
	private static String BLACKLIST_FOOD = "BlacklistFood";
	public static String HUNGEROVERHAUL_MODID = "HungerOverhaul";

	public void addModifedFoodValue(String foodName, int hunger, float saturationModifier) {
		FoodValues val = new FoodValues(hunger, saturationModifier);
		
		
		foodItems.put(foodName, val);
		
		//Send a message, So that HungerOverhaul does not change my values back..
		if(Loader.isModLoaded(HUNGEROVERHAUL_MODID)) {
			FMLInterModComms.sendMessage(HUNGEROVERHAUL_MODID, BLACKLIST_FOOD, foodName);
		}

	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
    public void getModifiedFoodValues(FoodEvent.GetFoodValues event) {
		Optional<UniqueIdentifier> uid = Optional.ofNullable(
								GameRegistry.findUniqueIdentifierFor(event.food.getItem()));
		
		Optional<FoodValues> fv = uid.map(id -> id.modId + ":" + id.name)
									 .flatMap(itemStr -> 
									 	Optional.ofNullable(foodItems.get(itemStr)) );
			
			fv.ifPresent(foodVal -> {
			   event.foodValues = foodVal;
		   });
		log.debug("ModifedFoodValue - getValues - food DisplayName: {} uid: {} isModified: {}",
				event.food.getDisplayName(), uid, fv.isPresent());
    }
	
}
