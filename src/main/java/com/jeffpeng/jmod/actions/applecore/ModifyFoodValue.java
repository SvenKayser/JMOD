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
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import net.minecraft.item.ItemStack;
import squeek.applecore.api.food.FoodValues;

public class ModifyFoodValue extends BasicAction {

	private String foodName;
	private FoodValues foodValues;
	
	private Logger log = LogManager.getLogger("AppleCore Mod Intergration");

	private Optional<UniqueIdentifier> uid = Optional.empty();
	
	private static String MSG_KEY_BLACKLIST_FOOD = "BlacklistFood";
	private static String HUNGEROVERHAUL_MODID = "HungerOverhaul";
	
	public ModifyFoodValue(JMODRepresentation owner, String food, int hunger, float saturationModifier) {
		super(owner);
		this.foodName = food;
		foodValues = new FoodValues(hunger, saturationModifier);
			
		// If HungerOverhaul is installed, Send a IMC  So that HungerOverhaul does not also try to change the food values
		if(Loader.isModLoaded(HUNGEROVERHAUL_MODID)) {
			FMLInterModComms.sendMessage(HUNGEROVERHAUL_MODID, MSG_KEY_BLACKLIST_FOOD, foodName);
			log.debug("Food Modify Action - Send IMC to HungerOverhaul - foodName: {}", foodName);
		}
		
		log.debug("Food Modify Action - Added - foodName: {}, hunger: {}, Saturation: {}, isValid: {}", 
				foodName, foodValues.hunger, foodValues.saturationModifier, this.valid);
	}

	@Override
	public boolean on(FMLLoadCompleteEvent event) {
		this.valid = false;
		
		
		Optional.ofNullable(lib.stringToItemStackNoOreDict(foodName))
				.filter(obj -> obj instanceof ItemStack)
				.ifPresent(obj -> {
					
					ItemStack itemStack= (ItemStack) obj;
					uid = Optional.ofNullable(GameRegistry.findUniqueIdentifierFor(itemStack.getItem()));

					this.valid = true;
					
					AppleCoreModifyFoodValues store = AppleCoreModifyFoodValues.getInstance();
					store.addModifedFoodValue(foodName, foodValues);
		});
		
		log.debug("Food Modify Action - Load Complete - foodName: {}, hunger: {}, Saturation: {}, isValid: {}, Uid: {}", 
				foodName, foodValues.hunger, foodValues.saturationModifier, 
				this.valid, this.uid.map(UniqueIdentifier::toString).orElse("No uid Found"));
		
		return valid;
	}
}
