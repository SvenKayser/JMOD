package com.jeffpeng.jmod.actions.applecore;

import java.util.Optional;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.modintegration.applecore.AppleCoreModifyFoodValues;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import net.minecraft.item.ItemStack;

public class ModifyFoodValue extends BasicAction {

	UniqueIdentifier uid;
	String foodName;
	int hunger;
	float saturationModifier;
	private Logger log = LogManager.getLogger("AppleCore Mod Intergration");

	public ModifyFoodValue(JMODRepresentation owner, String food, int hunger, float saturationModifier) {
		super(owner);
		this.foodName = food;
		this.hunger = hunger;
		this.saturationModifier = saturationModifier;
		
		log.debug("Food Modify action added for foodItem: {}", food);
	}

	@Override
	public boolean on(FMLPostInitializationEvent event){
		
		ItemStack stack = (ItemStack) lib.stringToItemStack(foodName);
		
		if(stack != null && stack instanceof ItemStack) {
			Optional.ofNullable(GameRegistry.findUniqueIdentifierFor(stack.getItem()))
					.ifPresent(uid -> {
						valid = true;
						this.uid = uid;
			});
			
			// foodName is not in GameRegistry
			if(!valid) {
				Stream.of(foodName).filter(name -> name.contains(":"))
								   .map(name -> new UniqueIdentifier(name))
								   .forEach(uid -> {
									   valid = true;
									   this.uid = uid;  
								   });
			}
		}
		else {
			valid = false;
		}
		
		log.debug("Post Init Modify Food action - foodName: {} isValid: {}", foodName, valid);
		return valid;
	}
	
	@Override
	public void execute() {
		AppleCoreModifyFoodValues store = AppleCoreModifyFoodValues.getInstance();
		log.debug("Execute Modify food action - uid: {} hunger: {} sat: {}", uid, hunger, saturationModifier);
		store.addModifedFoodValue(uid, hunger, saturationModifier);
	}
}
