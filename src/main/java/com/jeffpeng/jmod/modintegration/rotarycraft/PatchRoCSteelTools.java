package com.jeffpeng.jmod.modintegration.rotarycraft;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import Reika.RotaryCraft.Registry.ItemRegistry;

import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.util.Reflector;

public class PatchRoCSteelTools {
	public static void patchRoCSteelTools() {

		Object[] items = new Object[] { ItemRegistry.STEELPICK.getItemInstance(), ItemRegistry.STEELAXE.getItemInstance(),
				ItemRegistry.STEELSHOVEL.getItemInstance()};
		
		Object[] armors = new Object[] {ItemRegistry.STEELHELMET.getItemInstance(),ItemRegistry.STEELCHEST.getItemInstance(),
				ItemRegistry.STEELLEGS.getItemInstance(),ItemRegistry.STEELBOOTS.getItemInstance()};
		
		ToolMaterial toolmat = null;
		try{
			 toolmat = ToolMaterial.valueOf("HSLA");
		} catch(IllegalArgumentException e){
			JMOD.LOG.warn("Trying to set RotaryCraft tool data without HSLA being a defined TOOLMATERIAL will not work.");
			return;
		}
		
		
		for (int c = 0; c < items.length; c++) {
			Reflector itemreflector = new Reflector(items[c], ItemTool.class);
			
			itemreflector.set("field_77862_b", toolmat).set("field_77865_bY", toolmat.getDamageVsEntity() + 2F)
					.set("field_77864_a", toolmat.getEfficiencyOnProperMaterial());
			((ItemTool) items[c]).setMaxDamage(toolmat.getMaxUses());
		}
		
		
		Item theHoe = ItemRegistry.STEELHOE.getItemInstance();
		Reflector hoereflector = new Reflector(theHoe, ItemHoe.class);
		hoereflector.set("field_77843_a", toolmat);
		((ItemHoe)theHoe).setMaxDamage(toolmat.getMaxUses());
		
		Item theSword = ItemRegistry.STEELSWORD.getItemInstance();
		Reflector swordreflector = new Reflector(theSword, ItemSword.class);
		swordreflector.set("field_150933_b", toolmat).set("field_150934_a", toolmat.getDamageVsEntity() + 4F);
		((ItemSword)theSword).setMaxDamage(toolmat.getMaxUses());
		
		ArmorMaterial armormat = ArmorMaterial.valueOf("HSLA");
		
		for (int c = 0; c < armors.length; c++) {
			Reflector armorreflector = new Reflector(armors[c], ItemArmor.class);
			
			armorreflector.set("field_77879_b",armormat.getDamageReductionAmount(((ItemArmor)armors[c]).armorType));
			((ItemArmor) armors[c]).setMaxDamage(armormat.getDurability(((ItemArmor)armors[c]).armorType));
		}

		
		// throw new RuntimeException();
	}
}
