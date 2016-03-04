package com.jeffpeng.si.core.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import Reika.RotaryCraft.Registry.ItemRegistry;

import com.jeffpeng.si.core.SI;
import com.jeffpeng.si.core.SILib;
import com.jeffpeng.si.core.util.descriptors.BlockPropertyDescriptor;
import com.jeffpeng.si.core.util.descriptors.ChestLootDescriptor;
import com.jeffpeng.si.core.util.descriptors.OreDictionaryDescriptor;
import com.jeffpeng.si.core.util.descriptors.ShapelessRecipeDescriptor;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import fi.dy.masa.enderutilities.item.tool.ItemEnderTool;

public class Patcher {
	
	public static void patchArmor() {

		FMLControlledNamespacedRegistry<Item> gamereg = GameData.getItemRegistry();

		@SuppressWarnings("unchecked")
		Set<String> allItems = GameData.getItemRegistry().getKeys();

		for (String itemname : allItems) {

			Item item = gamereg.getObject(itemname);
			if (item instanceof ItemArmor) {
				
				ItemArmor itemarmor = (ItemArmor) item;
				
				if (itemarmor.getClass().getCanonicalName().contains("Reika.RotaryCraft")) {
					continue;
				}

				ArmorMaterial armormat = null;
				String armormatname = itemarmor.getArmorMaterial().name();
				

				if (armormatname == null) continue;
				else armormat = ArmorMaterial.valueOf(armormatname);

				
				// Update the tool material
				
				Reflector itemreflector = new Reflector(itemarmor, ItemArmor.class);
				itemreflector.set(5,armormat.getDamageReductionAmount(itemarmor.armorType));
				itemarmor.setMaxDamage(armormat.getDurability(itemarmor.armorType));

				FMLLog.info("[armor patcher] " + itemname + " is an armor (" + itemarmor.getClass().getName() + ")");
				
			}
		}
	}


	public static void patchTools() {

		FMLControlledNamespacedRegistry<Item> gamereg = GameData.getItemRegistry();

		@SuppressWarnings("unchecked")
		Set<String> allItems = GameData.getItemRegistry().getKeys();

		for (String itemname : allItems) {

			Item item = gamereg.getObject(itemname);
			if (item instanceof ItemTool || item instanceof ItemHoe || item instanceof ItemSword) {

				

				if (item.getClass().getCanonicalName().contains("Reika.RotaryCraft")) {
					continue;
				}

				ToolMaterial toolmat = null;
				String toolmatname = null;
				if (item instanceof ItemTool)
					toolmatname = ((ItemTool) item).getToolMaterialName();
				else if (item instanceof ItemHoe)
					toolmatname = ((ItemHoe) item).getToolMaterialName();
				else if (item instanceof ItemSword)
					toolmatname = ((ItemSword) item).getToolMaterialName();

				if (toolmatname == null) {
				} else {
					try {
						toolmat = ToolMaterial.valueOf(toolmatname);
					} catch (IllegalArgumentException e) {
						toolmat = null;
					}
				}

				if (toolmat == null) {
				} else {
					// Update the tool material
					if (item instanceof ItemTool) {
						if(item.getClass().getCanonicalName().contains("fi.dy.masa.enderutilities")){
							Reflector endertoolreflector = new Reflector(item, ItemEnderTool.class);
							endertoolreflector.set("material", toolmat).set("field_77865_bY",toolmat.getDamageVsEntity()+2F).set("field_77864_a", toolmat.getEfficiencyOnProperMaterial());
							
						} else {
							Reflector itemreflector = new Reflector(item, ItemTool.class);
							Float damagemodifier = 0F;
							if (item instanceof ItemAxe)
								damagemodifier = 3F;
							if (item instanceof ItemPickaxe)
								damagemodifier = 2F;
							if (item instanceof ItemSpade)
								damagemodifier = 1F;

							itemreflector.set(3, toolmat).set(2, toolmat.getDamageVsEntity() + damagemodifier)
									.set(1, toolmat.getEfficiencyOnProperMaterial());
						}
						

					}
					if (item instanceof ItemHoe) {
						new Reflector(item, ItemHoe.class).set(0, toolmat);
					}
					if (item instanceof ItemSword) {
						new Reflector(item, ItemSword.class).set(1, toolmat).set(0, toolmat.getDamageVsEntity() + 4F);
					}

					// Update the max damage
					if (item.getMaxDamage() > 0)
						item.setMaxDamage(toolmat.getMaxUses());

					if (item instanceof ItemTool)
						FMLLog.info("[tool patcher] " + itemname + " is a " + ((ItemTool) item).getToolMaterialName() + " tool (" + item.getClass().getName() + ")");
					if (item instanceof ItemHoe)
						FMLLog.info("[tool patcher] " + itemname + " is a " + ((ItemHoe) item).getToolMaterialName() + " hoe (" + item.getClass().getName() + ")");
					if (item instanceof ItemSword)
						FMLLog.info("[tool patcher] " + itemname + " is a " + ((ToolMaterial) new Reflector(item, ItemSword.class).get(1)).name() + " weapon (basically a sword) (" + item.getClass().getName() + ")");
				}
			}
		}
	}

	// TODO: There must be a more elegant solution to this.
	

	public static void patchBlockProperties() {
		for (Map.Entry<String, BlockPropertyDescriptor> entry : SI.CONFIG.blockproperties.entrySet()) {
			String[] splitname = entry.getKey().split(":");
			Block block = GameRegistry.findBlock(splitname[0], splitname[1]);
			if (block != null) {
				BlockPropertyDescriptor desc = entry.getValue();
				if (desc.harvestlevel.size() > 0) {
					for (Map.Entry<Integer, Integer> harventry : desc.harvestlevel.entrySet()) {
						String tool = block.getHarvestTool(harventry.getKey());
						block.setHarvestLevel(tool, harventry.getValue(), harventry.getKey());
					}
				}
				if (desc.blastresistance != null) {
					block.setResistance(desc.blastresistance);
				}

			} else {
				FMLLog.warning("[patcher] Cannot patch " + entry.getKey() + " as it is not in the game. Omitting.");
			}
		}
	}

	public static void patchRoCSteelTools() {

		Object[] items = new Object[] { ItemRegistry.STEELPICK.getItemInstance(), ItemRegistry.STEELAXE.getItemInstance(),
				ItemRegistry.STEELSHOVEL.getItemInstance()};
		
		Object[] armors = new Object[] {ItemRegistry.STEELHELMET.getItemInstance(),ItemRegistry.STEELCHEST.getItemInstance(),
				ItemRegistry.STEELLEGS.getItemInstance(),ItemRegistry.STEELBOOTS.getItemInstance()};
		
		ToolMaterial toolmat = null;
		try{
			 toolmat = ToolMaterial.valueOf("HSLA");
		} catch(IllegalArgumentException e){
			FMLLog.bigWarning("Trying to set RotaryCraft tool data without HSLA being a defined TOOLMATERIAL will not work.");
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
	
	@SuppressWarnings("rawtypes")
	public static void purgeRecipes(){
		
		// purge all
		
		for(String entry : SI.CONFIG.purgerecipes){
			
			Object is = SILib.stringToItemStack(entry);
			if(is instanceof ItemStack){
				Iterator it = CraftingManager.getInstance().getRecipeList().iterator();
				while(it.hasNext()){
					IRecipe recipe = ((IRecipe)it.next());
					ItemStack output = recipe.getRecipeOutput();
					if(SILib.matchItemStacks(output, (ItemStack)is)) it.remove();
				}
			}
		
		}
		
		for(ShapelessRecipeDescriptor entry : SI.CONFIG.purgeshapelessrecipes){
		
			List<Object> ingredients = new ArrayList<Object>();

			for (String ingrediententry : entry.ingredients) {
				int amount = SILib.itemStackSizeFromString(ingrediententry);
				
				Object ingredient = SILib.stringToItemStack(ingrediententry);
				
				if(ingredient instanceof ItemStack){
					ingredients.add(ingredient);
				} else {
					for (int c = 0; c < amount; c++) {
						ingredients.add(ingredient);
					}
				}
			}

			Object result = SILib.stringToItemStack(entry.result);
			if (!(result instanceof ItemStack)){
				continue;
			}
			ItemStack resultstack = ((ItemStack) result);
			IRecipe targetRecipe = new ShapelessOreRecipe(resultstack, ingredients.toArray());
			
			Iterator it = CraftingManager.getInstance().getRecipeList().iterator();
			while(it.hasNext()){
				IRecipe recipe = ((IRecipe)it.next());
				if(recipe.equals(targetRecipe))	it.remove();
			}
		}
		
		for(String entry : SI.CONFIG.purgesmeltingrecipes){
			Object is = SILib.stringToItemStack(entry);
			if(is instanceof ItemStack){
				@SuppressWarnings("unchecked")
				Iterator<Entry> it = FurnaceRecipes.smelting().getSmeltingList().entrySet().iterator();
				while(it.hasNext()){
					Entry mapentry = it.next();
					ItemStack output = (ItemStack)mapentry.getKey();
					if(SILib.matchItemStacks(output, (ItemStack)is)) it.remove();
				}
			}
		}
		
		
		
		
	}
	
	public static void processChestLoot(){
		Reflector categoriesReflector = new Reflector(null,ChestGenHooks.class);
		@SuppressWarnings("unchecked")
		Map<String,ChestGenHooks> categories = (Map<String,ChestGenHooks>)categoriesReflector.get("chestInfo");
		
		for(Map.Entry<String, String[]> entry: SI.CONFIG.removeChestLoot.entrySet()){
			Object is = SILib.stringToItemStack(entry.getKey());
			if(is instanceof ItemStack)
				if(entry.getValue() == null)
					for(Map.Entry<String,ChestGenHooks> category : categories.entrySet())	category.getValue().removeItem((ItemStack)is);
					
				else {
					for(String categoryname : entry.getValue()){
						ChestGenHooks category =  categories.get(categoryname);
						if(category != null) category.removeItem((ItemStack)is);
					}
				}
		}
		
		for(Map.Entry<ChestLootDescriptor, String[]> entry: SI.CONFIG.addChestLoot.entrySet()){
			Object is = SILib.stringToItemStack(entry.getKey().itemstack);
			if(is instanceof ItemStack)
			if(entry.getValue() == null){
				for(Map.Entry<String,ChestGenHooks> category : categories.entrySet()){
					category.getValue().addItem(entry.getKey().getWeightedRandomChestContent());
				}
			} else {
				for(String categoryname : entry.getValue()){
					ChestGenHooks category =  categories.get(categoryname);
					if(category != null) category.addItem(entry.getKey().getWeightedRandomChestContent());
				}
			}
		}
	}
	
	public static void removeOreDictEntries(){
		
		@SuppressWarnings("unchecked")
		List<ArrayList<ItemStack>> idToStack = (List<ArrayList<ItemStack>>) new Reflector(null,OreDictionary.class).get("idToStack");
		
		for(OreDictionaryDescriptor desc : SI.CONFIG.removeOredict){
			if(desc.entry != null){
				int id = OreDictionary.getOreID(desc.entry);
				Object is = SILib.stringToItemStack(desc.itemstack);
				if(is instanceof ItemStack){
					Iterator<ItemStack> it = idToStack.get(id).iterator();
					while(it.hasNext()){
						if(SILib.matchItemStacks(it.next(), (ItemStack)is)) it.remove();
					}
				}
			} else 
			
			for(int n = 0; n < idToStack.size(); n++){
				Object is = SILib.stringToItemStack(desc.itemstack);
				if(is instanceof ItemStack){
					Iterator<ItemStack> it = idToStack.get(n).iterator();
					while(it.hasNext()){
						if(SILib.matchItemStacks(it.next(), (ItemStack)is)) it.remove();
					}
				}
			} 
		}
		//OreDictionary.rebakeMap();
	}
}
