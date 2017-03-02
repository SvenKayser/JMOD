package com.jeffpeng.jmod.crafting;

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
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.oredict.OreDictionary;

import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;
import com.jeffpeng.jmod.actions.AddArmorMaterial;
import com.jeffpeng.jmod.primitives.OwnedObject;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fi.dy.masa.enderutilities.item.tool.ItemEnderSword;
import fi.dy.masa.enderutilities.item.tool.ItemEnderTool;
import fi.dy.masa.enderutilities.item.tool.ItemEnderTool.ToolType;

public class AnvilHandler extends OwnedObject {

	public float DISCOUNT;

	public AnvilHandler(JMODRepresentation jmod) {
		super(jmod);
		DISCOUNT = config.anvilRepairModifier;
	}

	@SubscribeEvent
	public void AnvilRepairHandler(AnvilUpdateEvent event) {

		if (event.left != null && event.right != null) {
			Item left = event.left.getItem();

			int durability = 0;
			int damage = 0;

			float effectivityPerItem = 0;
			int repairPerItem = 0;
			int maxItemsNeeded = 0;

			if (left instanceof ItemTool || left instanceof ItemHoe || left instanceof ItemSword || left instanceof ItemArmor) {
				ToolMaterial toolmat = null;
				ArmorMaterial armormat = null;
				ItemStack repairmat;

				if (left instanceof ItemTool)
					toolmat = ToolMaterial.valueOf(((ItemTool) left).getToolMaterialName());
				else if (left instanceof ItemHoe)
					toolmat = ToolMaterial.valueOf(((ItemHoe) left).getToolMaterialName());
				else if (left instanceof ItemSword)
					toolmat = ToolMaterial.valueOf(((ItemSword) left).getToolMaterialName());
				else if (left instanceof ItemArmor)
					armormat = ArmorMaterial.valueOf(((ItemArmor) left).getArmorMaterial().name());

				if (left instanceof ItemArmor) {
					AddArmorMaterial aarmor = config.armormaterials.get(armormat.name());
					if(aarmor != null && aarmor.repairstack != null) repairmat = aarmor.repairstack;
					else repairmat = new ItemStack(armormat.func_151685_b());
					
					if (repairmat.getItem() == null) return;
				} else {
					repairmat = toolmat.getRepairItemStack();
					if (repairmat == null||repairmat.getItem() == null) return;
					
				}

				if (!lib.matchItemStacksOreDict(event.right, repairmat)) return;
				
				durability = left.getMaxDamage();
				damage = event.left.getItemDamage();
				if (!Loader.isModLoaded("enderutilities") || !((left instanceof ItemEnderTool) || (left instanceof ItemEnderSword))) {
					if (left instanceof ItemPickaxe || left instanceof ItemAxe)
						effectivityPerItem = (1F / 3F) * DISCOUNT;
					else if (left instanceof ItemSpade)
						effectivityPerItem = 1F;
					else if (left instanceof ItemSword || left instanceof ItemHoe)
						effectivityPerItem = (1F / 2F) * DISCOUNT;
					else if (left instanceof ItemArmor) {
						int armortype = ((ItemArmor) left).armorType;
						if (armortype == 0)
							effectivityPerItem = (1F / 5F) * DISCOUNT;
						else if (armortype == 1)
							effectivityPerItem = (1F / 8F) * DISCOUNT;
						else if (armortype == 2)
							effectivityPerItem = (1F / 7F) * DISCOUNT;
						else if (armortype == 3)
							effectivityPerItem = (1F / 4F) * DISCOUNT;
					} else {
						return;
					}
				} else {
					if (left instanceof ItemEnderSword)
						effectivityPerItem = 1F / 2F;
					else {
						ItemEnderTool enderTool = ((ItemEnderTool) left);
						ToolType enderToolType = enderTool.getToolType(event.left);
						if (enderToolType == ToolType.PICKAXE || enderToolType == ToolType.AXE)
							effectivityPerItem = 1F / 3F;
						else if (enderToolType == ToolType.HOE)
							effectivityPerItem = 1F / 2F;
						else if (enderToolType == ToolType.SHOVEL)
							effectivityPerItem = 1F;
					}
				}

				if (effectivityPerItem == 0)
					return;

				repairPerItem = (int) (effectivityPerItem * durability);
				maxItemsNeeded = (int) (damage / repairPerItem) + 1;
				event.output = event.left.copy();
				if (event.right.stackSize > maxItemsNeeded) {
					event.materialCost = maxItemsNeeded;

					event.output.setItemDamage(0);

				} else {
					event.materialCost = event.right.stackSize;
					event.output.setItemDamage(damage - (int) (event.right.stackSize * effectivityPerItem * durability));

				}

				event.cost = 0;

				if (event.name != null && event.name != "") {
					event.output.setStackDisplayName(event.name);
					event.cost = 5;
				}

			}

		}

	}
}
