package com.jeffpeng.jmod.modintegration.applecore;

import java.util.HashMap;
import java.util.Optional;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import squeek.applecore.api.food.FoodEvent;
import squeek.applecore.api.food.FoodValues;


public class AppleCoreModifyFoodValues {

    private static AppleCoreModifyFoodValues instance = new AppleCoreModifyFoodValues();

    
    
    private AppleCoreModifyFoodValues(){
    	
    }

    public static AppleCoreModifyFoodValues getInstance() {
    	return instance;
    }
	
	private static HashMap<String, FoodValues> modifiedFoodItems = new HashMap<>();
	

	public void addModifedFoodValue(String foodName, FoodValues foodValues) {
		modifiedFoodItems.put(foodName, foodValues);
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
    public void getModifiedFoodValues(FoodEvent.GetFoodValues event) {
		FoodValuesLookup(event.food.getItem()).ifPresent(foodValue -> {
			   event.foodValues = foodValue;
		});
    }
	
	private Optional<FoodValues> FoodValuesLookup(Item item) {
		return Optional.ofNullable(GameRegistry.findUniqueIdentifierFor(item))
		        	   .map(id -> id.modId + ":" + id.name)
		        	   .filter(itemStr -> modifiedFoodItems.containsKey(itemStr))
		               .flatMap(itemStr -> Optional.ofNullable(modifiedFoodItems.get(itemStr)) );
	};
	
}
