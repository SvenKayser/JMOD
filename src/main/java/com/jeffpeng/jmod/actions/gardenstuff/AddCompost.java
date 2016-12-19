package com.jeffpeng.jmod.actions.gardenstuff;

import java.util.Optional;

import com.jaquadro.minecraft.gardenapi.api.GardenAPI;
import com.jaquadro.minecraft.gardenapi.api.machine.StandardCompostMaterial;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import net.minecraft.item.ItemStack;

public class AddCompost extends BasicAction {

	private String itemName;
	private int decomposeTime = 150;
	
	/**
	 * 
	 * @param owner
	 * @param itemName
	 * @param decomposeTime the number of ticks required to fully decompose this material.
	 * 		Typical times for most materials are 100 - 150 ticks, or 5-8 seconds.
	 * 		Example: String is 100. Rotten Flesh, Leather, treeLeaves, treeSapling, ItemFood are 150. 
	 * 			Where as logWood is 300.
	 */
	public AddCompost(JMODRepresentation owner, String itemName, int decomposeTime) {
		super(owner);
		this.itemName = itemName;
		this.decomposeTime = decomposeTime;
	}

	@Override
	public boolean on(FMLLoadCompleteEvent event){
		Optional.ofNullable(this.lib.stringToItemStack(itemName)).ifPresent( objStack -> {
			ItemStack itemStack = (ItemStack) objStack;
			this.valid = true;
			GardenAPI.instance()
					 .registries()
					 .compost()
					 .registerCompostMaterial(itemStack, new StandardCompostMaterial(this.decomposeTime, 0.125f));
			
		});
		
		return valid;
	}
}
