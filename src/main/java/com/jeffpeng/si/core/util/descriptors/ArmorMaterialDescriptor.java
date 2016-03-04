package com.jeffpeng.si.core.util.descriptors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;

import com.jeffpeng.si.core.interfaces.ISIExecutableObject;
import com.jeffpeng.si.core.util.Reflector;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ArmorMaterialDescriptor implements ISIExecutableObject {
	
	private static Map<String,ArmorMaterialDescriptor> list = new HashMap<>();
	
	public static ArmorMaterialDescriptor get(String name){
		return list.get(name);
	}
	
	public String name;
	public int enchantability;
	public int reductionbase;
	public int helmetfactor;
	public int chestfactor;
	public int legginsfactor;
	public int bootsfactor;
	public String repairmaterial;
	private ArmorMaterial armormat;
	private int[] factors;
	
	public ArmorMaterialDescriptor(String name, int reductionbase, int helmetfactor, int chestfactor,int legginsfactor,int bootsfactor,int enchantability,String repairmaterial){
		list.put(name,this);
		registerAsStaged();
		this.name = name;
		this.reductionbase = reductionbase;
		this.helmetfactor = helmetfactor;
		this.chestfactor = chestfactor;
		this.legginsfactor = legginsfactor;
		this.bootsfactor = bootsfactor;
		this.repairmaterial = repairmaterial;
	}

	@Override
	public boolean isValid() {
		return true;
	}
	
	@Override
	public boolean on(FMLPreInitializationEvent event){
		factors = new int[]{helmetfactor,chestfactor,legginsfactor,bootsfactor};
		armormat = EnumHelper.addArmorMaterial(name, reductionbase, factors, enchantability);
		return true;
	}

	@Override
	public void execute() {
		FMLLog.info("[armor material patcher] Patching ArmorMaterial " + armormat.name());
		new Reflector(armormat).set(5, reductionbase).set(6, factors).set(7, enchantability);
		ArrayList<ItemStack> entryoredictstacks = OreDictionary.getOres(repairmaterial);
		if(entryoredictstacks.size() > 0) 
			armormat.customCraftingMaterial = entryoredictstacks.get(0).getItem();
		else FMLLog.warning("[armor material patcher] the repairmaterial " + repairmaterial + " is unknown. " + armormat.name() + " will not be repairable.");
	}
}
