package com.jeffpeng.jmod.modintegration.applecore;

import java.util.HashMap;
import java.util.Optional;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import squeek.applecore.api.food.FoodEvent;
import squeek.applecore.api.food.FoodValues;


public class AppleCoreModifyFoodValues {

    private static AppleCoreModifyFoodValues instance = new AppleCoreModifyFoodValues();

    
    
    private AppleCoreModifyFoodValues(){
    	
    }

    public static AppleCoreModifyFoodValues getInstance(){
    	return instance;
    }
	
	private HashMap<String, FoodValues> foodItems = new HashMap<String, FoodValues>();
	// private static String BLACKLIST_FOOD = "BlacklistFood";
	// public static String HUNGEROVERHAUL_MODID = "HungerOverhaul";

	public void addModifedFoodValue(String foodName, int hunger, float saturationModifier) {
		FoodValues val = new FoodValues(hunger, saturationModifier);
		
		foodItems.put(foodName, val);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
    public void getModifiedFoodValues(FoodEvent.GetFoodValues event) {
		FoodValuesLookup(event.food.getItem()).ifPresent(foodValue -> {
			   event.foodValues = foodValue;
		});
    }
	
	private Optional<FoodValues> FoodValuesLookup(Item item) {
		return Optional.ofNullable(GameRegistry.findUniqueIdentifierFor(item))
		        	   .map(id -> id.modId + ":" + id.name)
		               .flatMap(itemStr -> Optional.ofNullable(foodItems.get(itemStr)) );
	};
	
}
