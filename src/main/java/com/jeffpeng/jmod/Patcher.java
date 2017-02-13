package com.jeffpeng.jmod;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.logging.log4j.Logger;

import com.jeffpeng.jmod.actions.AddToolMaterial;
import com.jeffpeng.jmod.util.Reflector;

import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import cpw.mods.fml.common.registry.GameData;
import fi.dy.masa.enderutilities.item.tool.ItemEnderTool;
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
	
	private static Patcher instance;
	public final Logger LOG;
	private Patcher(){
		this.LOG = JMOD.LOG;
	}

	public static Patcher getInstance() {
		if ( instance == null) {
			instance = new Patcher();
		}
		
	    return instance;
	}
	
	
	/**
	 * Patches tools to use any added ToolMaterials
	 */
	public void patchTools() {
		LOG.debug("Patching Tools - Begaining");
		
		long itemCount = StreamSupport.stream(GameData.getItemRegistry().typeSafeIterable().spliterator(), true)
					 .filter(isItemToolOrLike)
					 .peek(item -> LOG.debug("Patching Tools - a Tool - {}", 
							 item.getUnlocalizedName()))
					 .count();
		
		LOG.debug("Patching Tools - Count: {}", itemCount);
		
//		LOG.debug("Patching Tools - Contains minecraft:iron_shovel? {}, iron_pickaxe? {}",
//		GameData.getItemRegistry().containsKey("minecraft:iron_shovel"),
//		GameData.getItemRegistry().containsKey("minecraft:iron_pickaxe")
//		);
		
		// FMLControlledNamespacedRegistry<Item> gamereg = GameData.getItemRegistry();

//		@SuppressWarnings("unchecked")
//		Set<String> allItems = GameData.getItemRegistry().typeSafeIterable().spliterator();
//		Set<String> allItems = GameData.getItemRegistry().getKeys();

		StreamSupport.stream(GameData.getItemRegistry().typeSafeIterable().spliterator(), true)
//		allItems.stream()
				// .map(itemName -> gamereg.getObject(itemName))
				.filter(isItemUnpatchable) 
				.filter(isItemToolOrLike) 
				.peek(item -> LOG.debug("Patching Tools - Lookup ItemName: {}", item.getUnlocalizedName() ))
				.forEach(item -> {
					Optional<ToolMaterial> toolMat = lookUpToolMaterial(item);
					toolMat.ifPresent(mat -> {
						updateToolMaterial(item, mat);
					});
				});
	}
	
	/**
	 * Filter for Items we can not patch
	 */
	private Predicate<Item> isItemUnpatchable = item -> 
			  !item.getClass().getCanonicalName().contains("Reika.RotaryCraft");

	private Predicate<Item> isItemToolOrLike = item -> 
				(item instanceof ItemTool || 
				 item instanceof ItemHoe  || 
				 item instanceof ItemSword ||
				 item instanceof ItemAxe || 
				 item instanceof ItemPickaxe ||
				 item instanceof ItemSpade 
				 );
				
    /**
     * Finds the ToolMaterial to Patch our Tool with
     * 
     * Actions:
     *  1. Gets tool's Material Name
     *  2. first looks for it in list of added Tool Materials
     *  3. If not found in added Tool Materials look in Minecraft's ToolMaterial enum
     *  4. If tool material is still not found return empty, this will be skipped
     */
	private Optional<ToolMaterial> lookUpToolMaterial(Item item) {
		Optional<ToolMaterial> tmOpt = getItem_ToolMaterialName(item).flatMap(name -> {
			LOG.debug("Patching Tools - Lookup Item's ToolMat name: {}", name);
			return Stream.of(toolMaterialFromJModMaterials(name), 
					  		 toolMaterialFromMinecraftEnum(name)  
					 		)
						 .map(Supplier::get)
						 .filter(Optional::isPresent)
						 .map(Optional::get)
						 .peek(tm -> LOG.debug("Patching Tools - Lookup ToolMaterial: {} Harvestlvl: {}", tm.toString(), tm.getHarvestLevel()))
						 .findFirst();
		});
		
		
//		Optional<ToolMaterial> tmOpt = getItem_ToolMaterialName(item).flatMap(name -> {
//			return Stream.of(this.toolMaterialFromJModMaterials(name), // lazy lookup
//					  		 this.toolMaterialFromMinecraftEnum(name)  // lazy lookup
//					 		)
//						 .map(Supplier::get)
//						 .filter(Optional::isPresent)
//						 .map(Optional::get)
//						 .peek(tm -> LOG.debug("Pactching Tools - Lookup ToolMaterial: {} Harvestlvl: {}", tm.toString(), tm.getHarvestLevel()))
//						 .findFirst();
//		});
		
		return tmOpt;
	}
	
	private Optional<String> getItem_ToolMaterialName(Item item) {
		if (item instanceof ItemTool) 
			return Optional.ofNullable(((ItemTool)item).getToolMaterialName());
		if (item instanceof ItemHoe) 
			return Optional.ofNullable(((ItemHoe)item).getToolMaterialName());
		if (item instanceof ItemSword) 
			return Optional.ofNullable(((ItemSword)item).getToolMaterialName());
		
		return Optional.empty();
	}
	
//	private Optional<ToolMaterial> toolMaterialFromJModMaterialsOpt(String name) {
//		LOG.debug("Patching Tools - Tool Mat Lookup - from JMod Name: {}", name);
//		return Optional.ofNullable(AddToolMaterial.get(name))
//					   .map(add -> add.toolmat);
//	}
//		
//	private Optional<ToolMaterial> toolMaterialFromMinecraftEnumOpt(String name) {
//		LOG.debug("Patching Tools - Tool Mat Lookup - from Minecraft Name: {}", name);
//		return Stream.of(ToolMaterial.values())
//					 .filter(mat -> mat.toString() == name)
//					 .findFirst();
//	}

	
	private Supplier<Optional<ToolMaterial>> toolMaterialFromJModMaterials(String name) {
		return () -> {
			return Optional.ofNullable(AddToolMaterial.get(name))
						   .map(add -> add.toolmat);
		};
	}
	
	private Supplier<Optional<ToolMaterial>> toolMaterialFromMinecraftEnum(String name) {
		return () -> {
			return Stream.of(ToolMaterial.values())
						 .filter(mat -> mat.toString() == name)
						 .findFirst();
		};
	}
	
	/**
	 * Updates the Item to use a Tool Material.
	 */
	private void updateToolMaterial(Item item, ToolMaterial toolmat) {
		// Update the tool material
		if (item instanceof ItemTool) {
			if(item.getClass().getCanonicalName().contains("fi.dy.masa.enderutilities")){
				Reflector endertoolreflector = new Reflector(item, ItemEnderTool.class);
				endertoolreflector.set("material", toolmat).set("field_77865_bY",toolmat.getDamageVsEntity()+2F).set("field_77864_a", toolmat.getEfficiencyOnProperMaterial());
				
			} else {
				item.setMaxDamage(toolmat.getMaxUses());
				
				Reflector itemreflector = new Reflector(item, ItemTool.class);
				Float damagemodifier = 0F;
				if (item instanceof ItemAxe)
					damagemodifier = 3F;
					item.setHarvestLevel("axe", toolmat.getHarvestLevel());
				if (item instanceof ItemPickaxe) {
					damagemodifier = 2F;
					item.setHarvestLevel("pickaxe", toolmat.getHarvestLevel());
				}
				if (item instanceof ItemSpade) {
					damagemodifier = 1F;
					item.setHarvestLevel("shovel", toolmat.getHarvestLevel());
				}
				
				itemreflector.set(3, toolmat)
							 .set(2, toolmat.getDamageVsEntity() + damagemodifier)
							 .set(1, toolmat.getEfficiencyOnProperMaterial());
				
			}

		}
		if (item instanceof ItemHoe) {
			new Reflector(item, ItemHoe.class).set(0, toolmat);
		}
		if (item instanceof ItemSword) {
			new Reflector(item, ItemSword.class).set(1, toolmat).set(0, toolmat.getDamageVsEntity() + 4F);
		}
		
		// if (item instanceof ItemShears) {
		// }

		// Update the max damage
		if (item.getMaxDamage() > 0)
			item.setMaxDamage(toolmat.getMaxUses());
		
		String itemname = item.getUnlocalizedName();
		
		if (item instanceof ItemTool)
			JMOD.LOG.info("[tool patcher] " + itemname + " is a " + ((ItemTool) item).getToolMaterialName() + " tool (" + item.getClass().getName() + ")");
		if (item instanceof ItemHoe)
			JMOD.LOG.info("[tool patcher] " + itemname + " is a " + ((ItemHoe) item).getToolMaterialName() + " hoe (" + item.getClass().getName() + ")");
		if (item instanceof ItemSword)
			JMOD.LOG.info("[tool patcher] " + itemname + " is a " + ((ToolMaterial) new Reflector(item, ItemSword.class).get(1)).name() + " weapon (basically a sword) (" + item.getClass().getName() + ")");
	}
	
	
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
