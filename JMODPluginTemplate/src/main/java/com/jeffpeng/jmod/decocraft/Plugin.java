package com.jeffpeng.jmod.decocraft;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.jeffpeng.jmod.JMODPlugin;
import com.jeffpeng.jmod.JMODPluginContainer;
import com.mia.props.Props;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;

public class Plugin extends JMODPlugin{

	public Plugin(JMODPluginContainer container) {
		super(container);
	}
	
	public void on(FMLLoadCompleteEvent event){
		if(Loader.isModLoaded("props")){
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

}
