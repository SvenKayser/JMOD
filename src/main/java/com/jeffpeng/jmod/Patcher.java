package com.jeffpeng.jmod;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.logging.log4j.Logger;

import com.jeffpeng.jmod.actions.AddArmorMaterial;
import com.jeffpeng.jmod.actions.AddToolMaterial;
import com.jeffpeng.jmod.util.Reflector;

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
		
		StreamSupport.stream(GameData.getItemRegistry().typeSafeIterable().spliterator(), true)
				.filter(this::isItemUnpatchable) 
				.filter(this::isItemToolOrLike) 
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
	private boolean isItemUnpatchable(Item item) {
		return !item.getClass().getCanonicalName().contains("Reika.RotaryCraft");
	}
				
	private boolean isItemToolOrLike(Item item) {
		return (item instanceof ItemTool || 
				item instanceof ItemHoe  || 
				item instanceof ItemSword ||
				item instanceof ItemAxe || 
				item instanceof ItemPickaxe ||
				item instanceof ItemSpade);
	}

				
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

		
		StreamSupport.stream(GameData.getItemRegistry().typeSafeIterable().spliterator(), true)
				.filter(this::isItemUnpatchable) 
				.filter(this::isItemArmorOrLike) 
				.peek(item -> LOG.debug("Patching Armor - Lookup ItemName: {}", item.getUnlocalizedName() ))
				.forEach(item -> {
					Optional<ArmorMaterial> optMat = lookUpArmorMaterial(item);
					optMat.ifPresent(mat -> {
						updateArmorWithMaterial(item, mat);
					});
				});
	}
	
	private boolean isItemArmorOrLike(Item item) {
		return item instanceof ItemArmor;
	}
	
	private Optional<ArmorMaterial> lookUpArmorMaterial(Item item) {
		Optional<ArmorMaterial> tmOpt = Optional.ofNullable(((ItemArmor)item).getArmorMaterial()).flatMap(armorMat -> {
			String name = armorMat.name();
			
			LOG.debug("Patching Armor - Lookup Item's ArmorMat name: {}", name);
			return Stream.of(armorMatFromJModMaterials(name), 
			  		 		 armorMatFromMinecraftEnum(name)
			 		)
				 .map(Supplier::get)
				 .filter(Optional::isPresent)
				 .map(Optional::get)
				 .peek(tm -> LOG.debug("Patching Armor - Lookup ArmorMat: {} ordinal: {}", tm.toString(), tm.ordinal()))
				 .findFirst();
			
		});
				
		return tmOpt;	
	}
	
	private Supplier<Optional<ArmorMaterial>> armorMatFromJModMaterials(String name) {
		return () -> Optional.ofNullable(AddArmorMaterial.get(name)).map(add -> add.armormat);
	}
	
	private Supplier<Optional<ArmorMaterial>> armorMatFromMinecraftEnum(String name) {
		return () -> Stream.of(ArmorMaterial.values())
				 		   .filter(mat -> mat.toString() == name)
				 		   .findFirst();
	}
	
	private void updateArmorWithMaterial(Item item, ArmorMaterial mat) {
		ItemArmor itemarmor = (ItemArmor) item;
		
		Reflector itemreflector = new Reflector(itemarmor, ItemArmor.class);
		itemreflector.set(5,mat.getDamageReductionAmount(itemarmor.armorType));
		itemarmor.setMaxDamage(mat.getDurability(itemarmor.armorType));
		
		String itemname = itemarmor.getUnlocalizedName();
		JMOD.LOG.info("[armor patcher] " + itemname + " is an armor (" + itemarmor.getClass().getName() + ")");
	}
}
