package com.jeffpeng.jmod.modintegration.applecore;

import java.util.HashMap;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import squeek.applecore.api.food.FoodEvent;
import squeek.applecore.api.food.FoodValues;


public class AppleCoreModifyFoodValues {

    private static AppleCoreModifyFoodValues instance = new AppleCoreModifyFoodValues();

    private AppleCoreModifyFoodValues(){}

    public static AppleCoreModifyFoodValues getInstance(){
    	return instance;
    }
	
	private HashMap<String, FoodValues> foodItems = new HashMap<String, FoodValues>();

	
	public void addModifedFoodValue(ItemStack foodStack, int hunger, float saturationModifier) {
		FoodValues val = new FoodValues(hunger, saturationModifier);
		foodItems.put(foodStack.getUnlocalizedName(), val);
		
		//Send a message, So that HungerOverhaul does not change my values back..
		if(Loader.isModLoaded("HungerOverhaul")) {
			FMLInterModComms.sendMessage("HungerOverhaul", "BlacklistFood", foodStack);
		}

	}
	
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
    public void getModifiedFoodValues(FoodEvent.GetFoodValues event) {
		FoodValues val = foodItems.get(event.food.getUnlocalizedName());
		if(val != null) {
			event.foodValues = val;
		}
    }
	
}
