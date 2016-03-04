package com.jeffpeng.si.core.modintegration.decocraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.mia.props.Props;

public class DecoCraftDyeFix {
	public static void fix(){
		for(ItemStack redDye : OreDictionary.getOres("dyeRed")){
			Props.allowableResourceItems.add(redDye);
		}
		
		for(ItemStack greenDye : OreDictionary.getOres("dyeGreen")){
			Props.allowableResourceItems.add(greenDye);
		}
		
		for(ItemStack blueDye : OreDictionary.getOres("dyeBlue")){
			Props.allowableResourceItems.add(blueDye);
		}
		
		
		
	}
}
