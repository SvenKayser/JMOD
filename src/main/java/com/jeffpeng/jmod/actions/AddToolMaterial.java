package com.jeffpeng.jmod.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;
import com.jeffpeng.jmod.util.Reflector;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class AddToolMaterial extends BasicAction{
	
	private static Map<String,AddToolMaterial> list = new HashMap<>();
	
	public static AddToolMaterial get(String name){
		return list.get(name);
	}
	
	public String name;
	public int harvestLevel;
	public int durability;
	public float efficiency;
	public float damage;
	public int enchantability;
	public String repairmaterial;
	public ToolMaterial toolmat;
	
	
	
	public AddToolMaterial(JMODRepresentation owner, String name, int harvestLevel, int durability, float efficiency, float damage, int enchantability, String repairmaterial){
		super(owner);
		this.name = name;
		this.harvestLevel = harvestLevel;
		this.durability = durability;
		this.efficiency = efficiency;
		this.damage = damage;
		this.enchantability = enchantability;
		this.repairmaterial = repairmaterial;
		list.put(name,this);
	}

	@Override
	public boolean isValid() {
		return true;
	}
	
	@Override
	public boolean on(FMLPreInitializationEvent event){
		log.info("[tool material] injecting new tool material: " + name);
		this.toolmat = EnumHelper.addToolMaterial(name, harvestLevel, durability, efficiency, damage, enchantability);
		return true;
	}
	
	@Override
	public boolean on(FMLPostInitializationEvent event){
		new Thread(new Runnable(){

			@Override
			public void run() {
				log.info("[tool material patcher] Patching ToolMaterial " + toolmat.name());
				new Reflector(toolmat).set(5, harvestLevel).set(6, durability).set(7, efficiency)
						.set(8, damage).set(9, enchantability);
				ArrayList<ItemStack> entryoredictstacks = OreDictionary.getOres(repairmaterial);
				if(entryoredictstacks.size() > 0) 
					toolmat.setRepairItem(entryoredictstacks.get(0));
				else log.warn("[tool material patcher] the repairmaterial " + repairmaterial + " is unknown. " + toolmat.name() + " will not be repairable.");
			}
		}).start();
		return true;
	}

	@Override
	public int priority()
	{
		return 250;
	}
}
