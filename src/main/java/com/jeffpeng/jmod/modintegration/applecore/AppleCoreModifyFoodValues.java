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

	public void addModifedFoodValue(UniqueIdentifier uid, int hunger, float saturationModifier) {
		FoodValues val = new FoodValues(hunger, saturationModifier);
		
		String itemStr = uid.modId + ":" + uid.name;
		
		foodItems.put(itemStr, val);
		log.info("addModifedFoodValue - FoodName: {} hunger: {} saturation: {}", 
							uid, hunger, saturationModifier);
		
		//Send a message, So that HungerOverhaul does not change my values back..
		if(Loader.isModLoaded(HUNGEROVERHAUL_MODID)) {
			FMLInterModComms.sendMessage(HUNGEROVERHAUL_MODID, BLACKLIST_FOOD, itemStr);
		}

	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
    public void getModifiedFoodValues(FoodEvent.GetFoodValues event) {
		
		Optional<UniqueIdentifier> uid = Optional.ofNullable(GameRegistry.findUniqueIdentifierFor(event.food.getItem()));
		
		log.debug("getModifiedFoodValues - food Name: {} uid: {}",event.food.getDisplayName(), uid);
		uid.map(id -> id.modId + ":" + id.name)
			.flatMap(itemStr -> Optional.ofNullable(foodItems.get(itemStr)) )
			.ifPresent(item -> {
			   event.foodValues = new FoodValues(item.hunger, item.saturationModifier);
		   });
    }
	
}
