package com.jeffpeng.si.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jeffpeng.si.core.util.descriptors.AlloyDescriptor;
import com.jeffpeng.si.core.util.descriptors.ArmorMaterialDescriptor;
import com.jeffpeng.si.core.util.descriptors.BlockDescriptor;
import com.jeffpeng.si.core.util.descriptors.BlockPropertyDescriptor;
import com.jeffpeng.si.core.util.descriptors.ChestLootDescriptor;
import com.jeffpeng.si.core.util.descriptors.ColorDescriptor;
import com.jeffpeng.si.core.util.descriptors.BlockDropDescriptor;
import com.jeffpeng.si.core.util.descriptors.ItemDescriptor;
import com.jeffpeng.si.core.util.descriptors.ItemStackSubstituteDescriptor;
import com.jeffpeng.si.core.util.descriptors.OreDictionaryDescriptor;
import com.jeffpeng.si.core.util.descriptors.OreGenerationDescriptor;
import com.jeffpeng.si.core.util.descriptors.ShapedRecipeDescriptor;
import com.jeffpeng.si.core.util.descriptors.ShapelessRecipeDescriptor;
import com.jeffpeng.si.core.util.descriptors.TooltipDescriptor;
import com.jeffpeng.si.core.util.descriptors.rotarycraft.GrinderRecipeDescriptor;

public class SIConfig{

	public Map<String,String> moddependencies 							= new HashMap<>();

	public Map<String,ArmorMaterialDescriptor> armormaterials 			= new HashMap<>();
	public Map<String,BlockDescriptor> blocks							= new HashMap<>();
	public Map<String,ItemDescriptor> items								= new HashMap<>();
	public List<String> metalblocks										= new ArrayList<>();
	public List<String> metalingots										= new ArrayList<>();
	public List<ItemStackSubstituteDescriptor> itemstacksubstitutes		= new ArrayList<>();
	
	public List<ShapelessRecipeDescriptor> shapelessrecipes				= new ArrayList<>();
	public List<ShapedRecipeDescriptor> shapedrecipes					= new ArrayList<>();
	public Map<String,String> smeltingrecipes							= new HashMap<>();
	public List<ShapelessRecipeDescriptor> purgeshapelessrecipes		= new ArrayList<>();
	public List<String> purgesmeltingrecipes							= new ArrayList<>();
	
	public List<OreDictionaryDescriptor> oredict						= new ArrayList<>();
	public List<AlloyDescriptor> alloymap								= new ArrayList<>();
	public Map<String,BlockPropertyDescriptor> blockproperties			= new HashMap<>();
	public Map<Integer,String> harvestlevelcolors						= new HashMap<>();
	public List<OreGenerationDescriptor> oregeneration					= new ArrayList<>();
	public Map<String,ColorDescriptor> colors							= new HashMap<>();
	public List<String> purgerecipes									= new ArrayList<>();
	
	public List<String> hideitemsfromnei								= new ArrayList<>();
	public List<TooltipDescriptor> tooltips								= new ArrayList<>();
	public Map<String,String[]> removeChestLoot							= new HashMap<>();
	public Map<ChestLootDescriptor,String[]> addChestLoot				= new HashMap<>();
	public List<OreDictionaryDescriptor> removeOredict					= new ArrayList<>();
	
	public List<BlockDropDescriptor> blockDrops							= new ArrayList<>();
	
	
	public boolean libraryMode = false;
	public boolean addItems = true;
	public boolean addBlocks = true;
	public boolean updateToolMaterials = true;
	public boolean setToolMaterials = true;
	public boolean setArmorMaterials = true;
	public boolean updateArmorMaterials = true;
	public boolean updateBlockProperties = true;
	public boolean updateTools = true;
	public boolean updateArmor = true;
	public boolean runWorldGeneration = true;
	public boolean addOredictEntries = true;
	public boolean addRecipes = true;
	public boolean rotaryCraftSteelmaking = true;
	public boolean patchRotarycraftSteelTools = true;
	public boolean enderIOAlloys = true;
	public boolean purgeRecipes = true;
	public boolean useBuffedAnvilRepair = true;
	public boolean craftingGridToolRepair = true;
	public boolean preventToolBreaking = true;
	public boolean alterBlockDrops = true;
	public boolean removeOreDictEntries = true;
	public boolean precessChestLoot = true;
	
	public float anvilRepairModifier = 1.1F;
	public float craftingGridRepairModifier = 0.9F;
	
	public RotaryCraft rotaryCraft = new RotaryCraft();
	
	public class RotaryCraft {
		public List<GrinderRecipeDescriptor> grinderRecipes = new ArrayList<>(); 
	}
}
