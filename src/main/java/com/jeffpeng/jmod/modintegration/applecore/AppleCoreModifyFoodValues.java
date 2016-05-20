package com.jeffpeng.jmod.modintegration.applecore;

import java.util.HashMap;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.food.FoodEvent;
import squeek.applecore.api.food.FoodValues;


public class AppleCoreModifyFoodValues {

    private static AppleCoreModifyFoodValues instance = new AppleCoreModifyFoodValues();

    private AppleCoreModifyFoodValues(){}

    public static AppleCoreModifyFoodValues getInstance(){
    	return instance;
    }
	
	private HashMap<String, FoodValues> foodItems = new HashMap<String, FoodValues>();

	
	public void addModifedFoodValue(String foodUnlocalizedName, int hunger, float saturationModifier) {
		FoodValues val = new FoodValues(hunger, saturationModifier);
		foodItems.put(foodUnlocalizedName, val);
	}
	
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
    public void getModifiedFoodValues(FoodEvent.GetFoodValues event) {
		FoodValues val = foodItems.get(event.food.getUnlocalizedName());
		if(val != null) {
			event.foodValues = val;
		}
    }
	
}
