package com.jeffpeng.si.core.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import Reika.RotaryCraft.API.RecipeInterface;
import Reika.RotaryCraft.Auxiliary.ItemStacks;
import Reika.RotaryCraft.Registry.ItemRegistry;

import com.jeffpeng.si.core.SI;
import com.jeffpeng.si.core.SILib;
import com.jeffpeng.si.core.util.descriptors.AlloyDescriptor;
import com.jeffpeng.si.core.util.descriptors.OreDictionaryDescriptor;
import com.jeffpeng.si.core.util.descriptors.ShapedRecipeDescriptor;
import com.jeffpeng.si.core.util.descriptors.ShapelessRecipeDescriptor;
import com.jeffpeng.si.core.worldgeneration.SIConfigurableOreGenerator;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class SIRegistry {
	public static SIBlockMaterialRegistry blockMaterialRegistry = new SIBlockMaterialRegistry();
	
	public CreativeTabs getCreativeTabByName(String name) {
		for (int i = 0; i < CreativeTabs.creativeTabArray.length; i++) {
			String tabname = CreativeTabs.creativeTabArray[i].getTabLabel();
			if (tabname.matches(name))
				return CreativeTabs.creativeTabArray[i];
		}
		return null;
	}

	public Material getBlockMaterial(String blockMaterialName) {
		return blockMaterialRegistry.get(blockMaterialName);
	}


	public void registerRecipesToGame() {
//		recipeloop:
//		for (ShapedRecipeDescriptor entry : SI.CONFIG.shapedrecipes) {
//			
//			Map<Integer, Character> shape = new HashMap<Integer, Character>();
//			Map<Integer, Object> ingredients = new HashMap<Integer, Object>();
//			Integer c = 0;
//			
//			if(entry.recipe == null){
//				FMLLog.warning("Recipe for " + entry.result + " has no ingredients."); continue recipeloop;
//			}
//			
//
//			
//			for(String[] row :entry.recipe)	{
//				for (int j = 0; j < 3; j++) {
//					if (row != null && row[j] != null) {
//						shape.put(c, c.toString().charAt(0));
//						String name = SILib.substituteItemStackName(row[j]);
//						if (name.contains(":")) {
//							ItemStack stack;
//							String[] splitname = name.split(":");
//							stack = GameRegistry.findItemStack(splitname[0], splitname[1], 0);
//							if(stack==null){
//								FMLLog.warning("Could not build Recipe for " + entry.result + " since " + name + " is missing. ");
//								continue recipeloop;
//							}
//							if (splitname.length == 3) {
//								stack.setItemDamage(Integer.parseInt(splitname[2]));
//							}
//							ingredients.put(c, stack);
//						} else {
//							ingredients.put(c, name);
//						}
//					} else {
//						shape.put(c, ' ');
//						ingredients.put(c, "blockStone");
//					}
//					c++;
//				}
//			}
//				
//			String line1 = shape.get(0).toString() + shape.get(1).toString() + shape.get(2).toString();
//			String line2 = shape.get(3).toString() + shape.get(4).toString() + shape.get(5).toString();
//			String line3 = shape.get(6).toString() + shape.get(7).toString() + shape.get(8).toString();
//			
//			int lines = 3;
//			
//			if(line1.equals("   ")){
//				lines--;
//				line1 = new String(line2);
//				//line2 = new String(line3);
////					if(line1.equals("   ")){
////						lines--;line1 = new String(line2); line2 = new String("  ");
////					}
//			}
//			
//			if(lines == 3 && line3.equals("   ")) lines = 2;
//			if(lines == 2 && line2.equals("   ")) lines = 1;
//			
//			while(line1.substring(line1.length()-1).equals(" ") && line2.substring(line2.length()-1).equals(" ") && line3.substring(line2.length()-1).equals(" "))
//			{
//				line1 = line1.substring(0, line1.length()-1);
//				line2 = line2.substring(0, line2.length()-1);
//				line3 = line3.substring(0, line3.length()-1);
//			}
//			
//			Object result = SILib.stringToItemStack(entry.result);
//			if (!(result instanceof ItemStack)){
//				FMLLog.warning("Cannot resolve "  + entry.result + " to an item. Hint: OreDict entries are not valid as craftig results.");
//				continue;
//			}
//			
//			if(lines == 3)
//			
//			GameRegistry.addRecipe(new ShapedOreRecipe((ItemStack) result, line1, line2, line3, '0', ingredients.get(0), '1', ingredients.get(1), '2',
//					ingredients.get(2), '3', ingredients.get(3), '4', ingredients.get(4), '5', ingredients.get(5), '6', ingredients.get(6), '7',
//					ingredients.get(7), '8', ingredients.get(8))); else
//						
//			if(lines == 2)
//				
//			GameRegistry.addRecipe(new ShapedOreRecipe((ItemStack) result, line1, line2, '0', ingredients.get(0), '1', ingredients.get(1), '2',
//					ingredients.get(2), '3', ingredients.get(3), '4', ingredients.get(4), '5', ingredients.get(5))); else
//										
//			if(lines == 1)
//									
//			GameRegistry.addRecipe(new ShapedOreRecipe((ItemStack) result, line1, '0', ingredients.get(0), '1', ingredients.get(1), '2', ingredients.get(2)));
//
//		} 
	
//	 	if(SI.CONFIG.shapelessrecipes.size() >0)
//		for (ShapelessRecipeDescriptor entry : SI.CONFIG.shapelessrecipes) {
//	
//				List<Object> ingredients = new ArrayList<Object>();
//
//			for (String ingrediententry : entry.ingredients) {
//				int amount = SILib.itemStackSizeFromString(ingrediententry);
//				
//				Object ingredient = SILib.stringToItemStack(ingrediententry);
//				
//				if(ingredient instanceof ItemStack){
//					ingredients.add(ingredient);
//				} else {
//					for (int c = 0; c < amount; c++) {
//						ingredients.add(ingredient);
//					}
//				}
//				
//				
//			}
//
//			Object result = SILib.stringToItemStack(entry.result);
//			if (!(result instanceof ItemStack)){
//				FMLLog.warning("Cannot resolve "  + entry.result + " to an item. Hint: OreDict entries are not valid as craftig results.");
//				continue;
//			}
//			ItemStack resultstack = ((ItemStack) result);
//			GameRegistry.addRecipe(new ShapelessOreRecipe(resultstack, ingredients.toArray()));
//
//
//		}

//		if (Loader.isModLoaded("RotaryCraft")) {
//			for (AlloyDescriptor entry : SI.CONFIG.alloymap) {
//				if (entry.two != null) {
//					ItemStack result = SILib.getFirstOreDictMatch(entry.result);
//					if(result==null) continue;
//					result.stackSize = entry.amount;
//					RecipeInterface.blastfurn.addAPIRecipe(result, 750, new ShapelessOreRecipe(result, entry.one, entry.two), 1, 0);
//				}
//			}
//			
//			RecipeInterface.grinder.addAPIRecipe(ItemRegistry.COMPACTS.getStackOfMetadata(4),	ItemRegistry.MODEXTRACTS.getStackOfMetadata(27));
//			RecipeInterface.grinder.addAPIRecipe(ItemRegistry.MODINGOTS.getStackOfMetadata(6),	ItemRegistry.MODEXTRACTS.getStackOfMetadata(27));
//
//			if (SI.CONFIG.rotaryCraftSteelmaking) {
//				ItemStack steel = OreDictionary.getOres("ingotSteel").get(0);
//				RecipeInterface.blastfurn.addAPIAlloying(new ItemStack(Items.coal, 1, 1), 100F, 4, null, 0F, 0, null, 0F, 0, new ItemStack(Items.iron_ingot),
//						steel, -1, false, 0, 550);
//				RecipeInterface.blastfurn.addAPIAlloying(new ItemStack(Items.coal), 100F, 2, null, 0F, 0, null, 0F, 0, new ItemStack(Items.iron_ingot), steel,
//						-1, false, 0, 550);
//				RecipeInterface.blastfurn.addAPIAlloying(ItemStacks.coke, 100F, 1, null, 0F, 0, null, 0F, 0, new ItemStack(Items.iron_ingot), steel, -1, false,
//						0, 550);
//				RecipeInterface.blastfurn.addAPIAlloying(ItemStacks.coke, 100F, 1, new ItemStack(Items.gunpowder), 3F, 1, null, 0F, 0, new ItemStack(
//						Items.iron_ingot), steel, -1, true, 0, 1200);
//			}
//		}
//		
//		if (SI.CONFIG.enderIOAlloys && Loader.isModLoaded("RotaryCraft") && Loader.isModLoaded("EnderIO")){
//			ItemStack electricalSteel = GameRegistry.findItemStack("EnderIO", "itemAlloy", 1);			electricalSteel.setItemDamage(0);
//			ItemStack energeticAlloy = GameRegistry.findItemStack("EnderIO", "itemAlloy", 1);			energeticAlloy.setItemDamage(1);
//			ItemStack vibrantAlloy = GameRegistry.findItemStack("EnderIO", "itemAlloy", 1);				vibrantAlloy.setItemDamage(2);
//			ItemStack redstoneAlloy = GameRegistry.findItemStack("EnderIO", "itemAlloy", 1);			redstoneAlloy.setItemDamage(3);
//			ItemStack conductiveIron = GameRegistry.findItemStack("EnderIO", "itemAlloy", 1);			conductiveIron.setItemDamage(4);
//			ItemStack pulsatingIron = GameRegistry.findItemStack("EnderIO", "itemAlloy", 1);			pulsatingIron.setItemDamage(5);
//			ItemStack darkSteel = GameRegistry.findItemStack("EnderIO", "itemAlloy", 1);				darkSteel.setItemDamage(6);
//			ItemStack soularium = GameRegistry.findItemStack("EnderIO", "itemAlloy", 1);				soularium.setItemDamage(7);
//			
//			ItemStack fusedQuartz = GameRegistry.findItemStack("EnderIO", "blockFusedQuartz",1);		fusedQuartz.setItemDamage(0);
//			ItemStack qcGlass = GameRegistry.findItemStack("EnderIO", "blockFusedQuartz",1);			qcGlass.setItemDamage(1);
//			ItemStack efQuartz = GameRegistry.findItemStack("EnderIO", "blockFusedQuartz",1);			efQuartz.setItemDamage(2);
//			ItemStack eqcGlass = GameRegistry.findItemStack("EnderIO", "blockFusedQuartz",1);			eqcGlass.setItemDamage(3);
//			ItemStack bfQuartz = GameRegistry.findItemStack("EnderIO", "blockFusedQuartz",1);			bfQuartz.setItemDamage(4);
//			ItemStack bqcGlass = GameRegistry.findItemStack("EnderIO", "blockFusedQuartz",1);			bqcGlass.setItemDamage(5);
//			
//			ItemStack eioSilicon = GameRegistry.findItemStack("EnderIO", "itemMaterial",1);				eioSilicon.setItemDamage(0);
//			
//			
//			
//			RecipeInterface.blastfurn.addAPIRecipe(electricalSteel,600, new ShapelessOreRecipe(electricalSteel, "ingotSteel", "itemSilicon" ),1,0);
//			RecipeInterface.blastfurn.addAPIRecipe(electricalSteel,600, new ShapelessOreRecipe(electricalSteel, "ingotHSLA", "itemSilicon" ),1,0);
//			RecipeInterface.blastfurn.addAPIRecipe(energeticAlloy,1000, new ShapelessOreRecipe(energeticAlloy, "dustRedstone", "ingotGold", "dustGlowstone" ),1,0);
//			RecipeInterface.blastfurn.addAPIRecipe(vibrantAlloy,1450, new ShapelessOreRecipe(vibrantAlloy, "ingotEnergeticAlloy", "pearlEnder"),1,0);
//			RecipeInterface.blastfurn.addAPIRecipe(conductiveIron,600, new ShapelessOreRecipe(conductiveIron, "dustRedstone", "ingotIron"),1,0);
//			RecipeInterface.blastfurn.addAPIRecipe(redstoneAlloy,600, new ShapelessOreRecipe(redstoneAlloy, "dustRedstone", "itemSilicon"),1,0);
//			RecipeInterface.blastfurn.addAPIRecipe(pulsatingIron,1000, new ShapelessOreRecipe(pulsatingIron, "pearlEnder", "ingotIron"),1,0);
//			RecipeInterface.blastfurn.addAPIRecipe(darkSteel,1200, new ShapelessOreRecipe(darkSteel, "ingotSteel", "blockObsidian"),1,0);
//			RecipeInterface.blastfurn.addAPIRecipe(darkSteel,1200, new ShapelessOreRecipe(darkSteel, "ingotHSLA", "blockObsidian"),1,0);
//			RecipeInterface.blastfurn.addAPIRecipe(soularium,1450, new ShapelessOreRecipe(soularium, "soulsand", "ingotGold"),1,0);
//			
//			RecipeInterface.blastfurn.addAPIAlloying(null, 0F, 0, null, 0F, 0,null, 0F, 0, new ItemStack(Items.quartz),fusedQuartz,4,false,1F,600);
//			RecipeInterface.blastfurn.addAPIAlloying(null, 0F, 0, null, 0F, 0,null, 0F, 0, new ItemStack(Blocks.sand),qcGlass,1,false,1F,600);
//			RecipeInterface.blastfurn.addAPIAlloying(new ItemStack(Items.glowstone_dust), 100F, 4, null, 0F, 0,null, 0F, 0, new ItemStack(Items.quartz),efQuartz,4,false,1F,600);
//			RecipeInterface.blastfurn.addAPIAlloying(new ItemStack(Items.glowstone_dust), 100F, 4, null, 0F, 0,null, 0F, 0, new ItemStack(Blocks.sand),eqcGlass,1,false,1F,600);
//			RecipeInterface.blastfurn.addAPIAlloying(new ItemStack(Items.dye), 100F, 4, null, 0F, 0,null, 0F, 0, new ItemStack(Items.quartz),bfQuartz,4,false,1F,600);
//			RecipeInterface.blastfurn.addAPIAlloying(new ItemStack(Items.dye), 100F, 4, null, 0F, 0,null, 0F, 0, new ItemStack(Blocks.sand),bqcGlass,1,false,1F,600);
//			
//			RecipeInterface.blastfurn.addAPIAlloying(ItemRegistry.MODEXTRACTS.getStackOfMetadata(27), 25F, 1, new ItemStack(Items.coal, 1, 1), 25F, 1, null, 0F,0, new ItemStack(Blocks.sand), eioSilicon,-1, false, 1F, 800);
//			RecipeInterface.blastfurn.addAPIAlloying(ItemRegistry.MODEXTRACTS.getStackOfMetadata(27), 25F, 1, new ItemStack(Items.coal), 25F, 1, null, 0F,0, new ItemStack(Blocks.sand), eioSilicon,-1, false, 1F, 800);
//			RecipeInterface.blastfurn.addAPIAlloying(ItemRegistry.MODEXTRACTS.getStackOfMetadata(27), 25F, 1, ItemStacks.coke, 25F, 1, null, 0F,0, new ItemStack(Blocks.sand), eioSilicon,-1, true, 1F, 800);
//			
//		}

	}

	public String blockyfy(String name) {
		String newname = "block";
		newname += ((Character) name.charAt(0)).toString().toUpperCase();
		newname += name.substring(1);
		return newname;
	}

	public String ingotyfy(String name) {
		String newname = "ingot";
		newname += ((Character) name.charAt(0)).toString().toUpperCase();
		newname += name.substring(1);
		return newname;
	}

	public void registerOreDict() {
		for (OreDictionaryDescriptor entry : SI.CONFIG.oredict) {
			FMLLog.info("Processing OreDictionary entry "+entry.itemstack);
			Object itemstack = SILib.stringToItemStack(entry.itemstack);
			if(itemstack instanceof ItemStack){
				OreDictionary.registerOre(entry.entry, (ItemStack)itemstack);
				FMLLog.info("Adding " + entry.itemstack + " to "+entry.entry);
				continue;
			} 
			
			if(itemstack instanceof String && !((String) itemstack).contains(":"))
			{
				FMLLog.info("Trying crossmatching of " + (String)itemstack + " to "+entry.entry);
				if(OreDictionary.getOres((String)itemstack).size() > 0){
					FMLLog.info("Crossmatching " + (String)itemstack + " to "+entry.entry);
					for(ItemStack listItem : OreDictionary.getOres((String)itemstack)){
						FMLLog.info("Registering " + listItem.getItem().getUnlocalizedName() + " to " + entry.entry + " because of it's membership to "+(String)itemstack);
						OreDictionary.registerOre(entry.entry, listItem);
					}
					continue;
				}
			}
			
			
			FMLLog.info("[registry] Trying to oredict " + entry.itemstack + ", but it is missing or not a proper itemstack/oredict descriptor.");
			
		}
	}

	public void registerOreGeneration() {
		boolean hasOregen = false;
		
		if(SI.CONFIG.oregeneration.size() > 0) hasOregen = true;
		
		if(hasOregen){
			SIConfigurableOreGenerator oreGenerator = new SIConfigurableOreGenerator();
			GameRegistry.registerWorldGenerator(oreGenerator, 0);
		}
		
	}
}
