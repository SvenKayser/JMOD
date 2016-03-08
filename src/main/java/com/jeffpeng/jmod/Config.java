package com.jeffpeng.jmod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jeffpeng.jmod.util.actions.AddArmorMaterial;
import com.jeffpeng.jmod.util.actions.AddBlockDrop;
import com.jeffpeng.jmod.util.descriptors.AlloyDescriptor;
import com.jeffpeng.jmod.util.descriptors.ColorDescriptor;
import com.jeffpeng.jmod.util.descriptors.ItemStackSubstituteDescriptor;
import com.jeffpeng.jmod.util.descriptors.TooltipDescriptor;

public class Config{

	public Map<String,String> moddependencies 							= new HashMap<>();
	public Map<String,AddArmorMaterial> armormaterials 					= new HashMap<>();
	public List<String> metalblocks										= new ArrayList<>();
	public List<String> metalingots										= new ArrayList<>();
	public List<ItemStackSubstituteDescriptor> itemstacksubstitutes		= new ArrayList<>();
	public List<AlloyDescriptor> alloymap								= new ArrayList<>();
	public Map<String,ColorDescriptor> colors							= new HashMap<>();
	public List<TooltipDescriptor> tooltips								= new ArrayList<>();
	public List<AddBlockDrop> blockDrops								= new ArrayList<>();
	
	public boolean patchRotarycraftSteelTools = false;
	public boolean enhancedAnvilRepair = false;
	public boolean craftingGridToolRepair = false;
	public boolean showToolHarvestLevels = false;
	public boolean showArmorValues = false;
	public boolean showBlockHarvestLevels = false;
	
	public float anvilRepairModifier = 1.1F;
	public float craftingGridRepairModifier = 0.9F;
	
}
