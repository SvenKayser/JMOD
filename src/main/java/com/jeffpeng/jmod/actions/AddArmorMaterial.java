package com.jeffpeng.jmod.actions;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.descriptors.ItemStackDescriptor;
import com.jeffpeng.jmod.primitives.BasicAction;
import com.jeffpeng.jmod.util.Reflector;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class AddArmorMaterial extends BasicAction {
	
	private static Map<String,AddArmorMaterial> list = new HashMap<>();
	
	public static AddArmorMaterial get(String name){
		return list.get(name);
	}
	
	public String name;
	public int enchantability;
	public int reductionbase;
	public int helmetfactor;
	public int chestfactor;
	public int legginsfactor;
	public int bootsfactor;
	public ItemStackDescriptor repairmaterial;
	public ItemStack repairstack;
	public ArmorMaterial armormat;
	private int[] factors;
	
	public AddArmorMaterial(JMODRepresentation owner, String name, int reductionbase, int helmetfactor, int chestfactor,int legginsfactor,int bootsfactor,int enchantability,String repairmaterial){
		this(owner,name,reductionbase,helmetfactor,chestfactor,legginsfactor,bootsfactor,enchantability,new ItemStackDescriptor(owner, repairmaterial));
	}
	
	public AddArmorMaterial(JMODRepresentation owner, String name, int reductionbase, int helmetfactor, int chestfactor,int legginsfactor,int bootsfactor,int enchantability,ItemStackDescriptor repairmaterial){
		super(owner);
		this.name = name;
		this.reductionbase = reductionbase;
		this.helmetfactor = helmetfactor;
		this.chestfactor = chestfactor;
		this.legginsfactor = legginsfactor;
		this.bootsfactor = bootsfactor;
		this.repairmaterial = repairmaterial;
		this.valid = true;
		list.put(name,this);
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public boolean on(FMLPreInitializationEvent event){
		factors = new int[]{helmetfactor,chestfactor,legginsfactor,bootsfactor};
		armormat = EnumHelper.addArmorMaterial(name, reductionbase, factors, enchantability);
		((Map<String,AddArmorMaterial>) config.get("armormaterials")).put(name,this);
		return true;
	}
	
	@Override
	public boolean on(FMLPostInitializationEvent event){

		log.info("[armor material patcher] Patching ArmorMaterial " + armormat.name());
		new Reflector(armormat).set(5, reductionbase).set(6, factors).set(7, enchantability);
		repairstack = repairmaterial.toItemStack();
		if(repairstack == null)	log.warn("[armor material patcher] the repairmaterial " + repairmaterial + " is unknown. " + armormat.name() + " will not be repairable.");
		return true;
	}
	
	@Override
	public int priority()
	{
		return Priorities.AddArmorMaterial;
	}
}
