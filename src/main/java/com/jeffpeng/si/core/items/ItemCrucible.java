package com.jeffpeng.si.core.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import codechicken.nei.api.API;

import com.jeffpeng.si.core.SI;
import com.jeffpeng.si.core.util.descriptors.AlloyDescriptor;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCrucible extends SICoreItem {

	Map<String, Integer> ingredientmap = new HashMap<String, Integer>();
	Map<Integer, String> ingredientbackmap = new HashMap<Integer, String>();
	Map<String, Integer> alloymap = new HashMap<String, Integer>();
	Map<Integer, String> alloybackmap = new HashMap<Integer, String>();
	Map<Integer, String> cruciblestates = new HashMap<Integer, String>();

	public static Item thisUnfired;
	public static Item thisFired = new ItemCrucible(1);
	public static Item thisFilled = new ItemCrucible(2);
	public static Item thisCooked = new ItemCrucible(3);
	private int innerState = 0;

	public ItemCrucible() {
		thisUnfired = this;
		
		this.setHasSubtypes(innerState >= 2);
		setMaps();
		MinecraftForge.EVENT_BUS.register(this);

	}
	
	@Override
	public void register(){
		super.register();
		GameRegistry.registerItem(thisFired, "technical.itemCrucible.fired");
		GameRegistry.registerItem(thisFilled, "technical.itemCrucible.filled");
		GameRegistry.registerItem(thisCooked, "technical.itemCrucible.cooked");
		if(Loader.isModLoaded("NotEnoughItems")){
			API.hideItem(new ItemStack(thisFired,1));
			API.hideItem(new ItemStack(thisFilled,1));
			API.hideItem(new ItemStack(thisCooked,1));
		}
		
	}

	public ItemCrucible(int state) {
		this.innerState = state;
		this.setHasSubtypes(state >= 2);
	}

	public int getState() {
		return innerState;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(Item item, CreativeTabs creativetab, List itemlist) {
		itemlist.add(new ItemStack(this, 1, 0));
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon[] icons;
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		icons = new IIcon[4];
		this.icons[0] = reg.registerIcon("si.core:itemCrucible_unfired");
		this.icons[1] = reg.registerIcon("si.core:itemCrucible_fired");
		this.icons[2] = reg.registerIcon("si.core:itemCrucible_filled");
		this.icons[3] = reg.registerIcon("si.core:itemCrucible_cooked");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return this.icons[innerState];
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String name = "item.si.core.itemCrucible.";
		switch (innerState) {
			case 0:
				name += "unfired";
				break;
			case 1:
				name += "fired";
				break;
			case 2:
				name += "filled";
				break;
			case 3:
				name += "cooked";
				break;
		}
		return name;
	}

	@Override
	public boolean hasContainerItem() {
		return innerState == 3;
	}

	@Override
	public Item getContainerItem() {
		return ItemCrucible.thisFired;
	}

	@SubscribeEvent
	public void hoverContents(ItemTooltipEvent event) {
		Item stackitem = event.itemStack.getItem();
		if (stackitem instanceof ItemCrucible && event.itemStack.getItemDamage() > 0) {
			ItemCrucible crucible = (ItemCrucible) stackitem;
			if (crucible.getState() == 2) {
				Integer type = event.itemStack.getItemDamage();
				Integer first = getFirstIngredient(type);
				Integer second = getSecondIngredient(type);
				String tip = "";

				tip += StatCollector.translateToLocal("info.si.core.itemCrucible.contains") + " "
						+ StatCollector.translateToLocal("info.si.core.itemCrucible.ingredient." + ingredientbackmap.get(first));
				if (second > 0)
					tip += " " + StatCollector.translateToLocal("info.si.core.itemCrucible.and") + " "
							+ StatCollector.translateToLocal("info.si.core.itemCrucible.ingredient." + ingredientbackmap.get(second));
				event.toolTip.add(tip);
			} else

			if (crucible.getState() == 3) {
				Integer type = event.itemStack.getItemDamage();
				Integer first = getFirstIngredient(type);
				Integer second = getSecondIngredient(type);
				String tip = "";

				tip += StatCollector.translateToLocal("info.si.core.itemCrucible.contains") + " "
						+ StatCollector.translateToLocal("info.si.core.itemCrucible.alloy." + alloybackmap.get(first)) + " "
						+ StatCollector.translateToLocal("info.si.core.itemCrucible.for") + " " + second + " "
						+ StatCollector.translateToLocal("info.si.core.itemCrucible.ingots");
				event.toolTip.add(tip);
			}
		}
	}

	public void setMaps() {
		Integer c = 1;
		Integer d = 1;
		for (AlloyDescriptor entry : SI.CONFIG.alloymap) {
			if (entry.one != null && !ingredientmap.containsKey(entry.one)) {
				ingredientmap.put(entry.one, c);
				ingredientbackmap.put(c, entry.one);
				c++;
			}

			if (entry.two != null && !ingredientmap.containsKey(entry.two)) {
				ingredientmap.put(entry.two, c);
				ingredientbackmap.put(c, entry.two);
				c++;
			}

			if (entry.result != null && !alloymap.containsKey(entry.result)) {
				alloymap.put(entry.result, d);
				alloybackmap.put(d, entry.result);
				d++;
			}
		}
	}

	public void setRecipes() {

		ItemStack mold = new ItemStack(ItemIngotMold.instance);
		ItemStack unfired = new ItemStack(this, 1);
		ItemStack fired = new ItemStack(thisFired, 1);
		GameRegistry.addSmelting(unfired, fired, 1F);
		for (AlloyDescriptor entry : SI.CONFIG.alloymap) {
			ArrayList<ItemStack> oredictentry = OreDictionary.getOres(entry.result);
			if (oredictentry.size() > 0) {
				ItemStack result = oredictentry.get(0);
				result.stackSize = entry.amount;
				ItemStack cooked;
				ItemStack filled;
				if (entry.two != null) {
					filled = new ItemStack(thisFilled, 1, ingredientmap.get(entry.one) * 1 + ingredientmap.get(entry.two) * 1 * 128);
					cooked = new ItemStack(thisCooked, 1, alloymap.get(entry.result) * 1 + entry.amount * 1 * 128);
					GameRegistry.addRecipe(new ShapelessOreRecipe(filled, entry.one, fired, entry.two));
				} else {
					filled = new ItemStack(thisFilled, 1, ingredientmap.get(entry.one) * 1);
					cooked = new ItemStack(thisCooked, 1, alloymap.get(entry.result) * 1 + entry.amount * 1 * 128);
					GameRegistry.addRecipe(new ShapelessOreRecipe(filled, entry.one, fired));
				}
				GameRegistry.addSmelting(filled, cooked, 2F);
				GameRegistry.addRecipe(new ShapelessOreRecipe(result, cooked, mold));
			}
		}
	}

	public Integer getMeta(int type, String one, String two) {
		return type + ingredientmap.get(one) * 1 + ingredientmap.get(two) * 1 * 128;
	}

	public Integer getMeta(int type, String one) {
		return type + ingredientmap.get(one) * 1;
	}

	public Integer getMeta(int type) {
		return type;
	}

	public Integer getFirstIngredient(int meta) {
		return (meta & 127) / 1;
	}

	public Integer getSecondIngredient(int meta) {
		return (meta & 16256) / (1 * 128);
	}
}
