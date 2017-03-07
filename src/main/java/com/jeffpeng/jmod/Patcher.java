package com.jeffpeng.jmod;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.jeffpeng.jmod.actions.AddToolMaterial;
import com.jeffpeng.jmod.util.Reflector;

import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

public class Patcher {
	
	private static Patcher instance = new Patcher();

	private Patcher(){}

	public static Patcher getInstance(){
	    return instance;
	}
	
	
	/**
	 * Patches tools to use any added ToolMaterials
	 */
	public void patchTools() {
		FMLControlledNamespacedRegistry<Item> gamereg = GameData.getItemRegistry();

		@SuppressWarnings("unchecked")
		Set<String> allItems = GameData.getItemRegistry().getKeys();

		allItems.stream()
				.map(itemName -> gamereg.getObject(itemName))
				.filter(isItemInstanceOf)
				.forEach(item -> {
					Optional<ToolMaterial> toolMat = lookUpToolMaterial.apply(item);
					toolMat.ifPresent(mat -> {
						updateToolMaterial.accept(item, mat);
					});
				});
		
	}
	
	/**
	 * Filter for Items we can not patch
	 */
	private Predicate<Item> isItemInstanceOf = item -> 
			  !item.getClass().getCanonicalName().contains("Reika.RotaryCraft") ||
													   item instanceof ItemTool || 
													   item instanceof ItemHoe  || 
													   item instanceof ItemSword;

    /**
     * Finds the ToolMaterial to Patch our Tool with
     * 
     * Actions:
     *  1. Gets tool's Material Name
     *  2. first looks for it in list of added Tool Materials
     *  3. If not found in added Tool Materials look in Minecraft's ToolMaterial enum
     *  4. If tool material is still not found return empty, this will be skipped
     */
	private Function<Item, Optional<ToolMaterial>> lookUpToolMaterial = item -> {
		Optional<String> toolmatname = Optional.empty();
		
		if (item instanceof ItemTool)
			toolmatname = Optional.ofNullable( ((ItemTool) item).getToolMaterialName());
		else if (item instanceof ItemHoe)
			toolmatname = Optional.ofNullable( ((ItemHoe) item).getToolMaterialName());
		else if (item instanceof ItemSword)
			toolmatname = Optional.ofNullable( ((ItemSword) item).getToolMaterialName());

		Optional<ToolMaterial> toolmat = toolmatname.flatMap(name -> 
											Optional.ofNullable(AddToolMaterial.get(name)).map(add -> add.toolmat)
													);
		
		if (!toolmat.isPresent()) {
			try {
				toolmat = toolmatname.flatMap(name -> Optional.ofNullable(ToolMaterial.valueOf(name)));
			} catch (IllegalArgumentException e) {
				toolmat = Optional.empty();
			}
		}
		return toolmat;
	};
	
	/**
	 * Updates the Item to use a Tool Material.
	 */
	private BiConsumer<Item, ToolMaterial> updateToolMaterial = (item, toolmat) -> {
		if(JMODPlugin.updateToolMaterialCycle(item,toolmat)) return;
		if (item instanceof ItemTool) {
			item.setMaxDamage(toolmat.getMaxUses());
			Reflector itemreflector = new Reflector(item, ItemTool.class);
			Float damagemodifier = 0F;
			if (item instanceof ItemAxe)
				damagemodifier = 3F;
			if (item instanceof ItemPickaxe) {
				damagemodifier = 2F;
				item.setHarvestLevel("pickaxe", toolmat.getHarvestLevel());
			}
			if (item instanceof ItemSpade) {
				damagemodifier = 1F;
				item.setHarvestLevel("shovel", toolmat.getHarvestLevel());
			}
			
			itemreflector.set(3, toolmat).set(2, toolmat.getDamageVsEntity() + damagemodifier)
					.set(1, toolmat.getEfficiencyOnProperMaterial());
		}
		if (item instanceof ItemHoe) {
			new Reflector(item, ItemHoe.class).set(0, toolmat);
		}
		if (item instanceof ItemSword) {
			new Reflector(item, ItemSword.class).set(1, toolmat).set(0, toolmat.getDamageVsEntity() + 4F);
		}

		if (item.getMaxDamage() > 0)
			item.setMaxDamage(toolmat.getMaxUses());
		
		String itemname = item.getUnlocalizedName();
		
		if (item instanceof ItemTool)
			JMOD.LOG.info("[tool patcher] " + itemname + " is a " + ((ItemTool) item).getToolMaterialName() + " tool (" + item.getClass().getName() + ")");
		if (item instanceof ItemHoe)
			JMOD.LOG.info("[tool patcher] " + itemname + " is a " + ((ItemHoe) item).getToolMaterialName() + " hoe (" + item.getClass().getName() + ")");
		if (item instanceof ItemSword)
			JMOD.LOG.info("[tool patcher] " + itemname + " is a " + ((ToolMaterial) new Reflector(item, ItemSword.class).get(1)).name() + " weapon (basically a sword) (" + item.getClass().getName() + ")");
	};
	
	
	/**
	 * Patches Armor to use any added ArmorMaterial
	 */
	public void patchArmor() {

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

				JMOD.LOG.info("[armor patcher] " + itemname + " is an armor (" + itemarmor.getClass().getName() + ")");
				
			}
		}
	}
}
