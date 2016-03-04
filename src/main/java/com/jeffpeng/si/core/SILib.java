package com.jeffpeng.si.core;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;
import team.chisel.init.ChiselItems;
import team.chisel.item.chisel.ItemChisel;

import com.cricketcraft.chisel.api.IChiselItem;
import com.jeffpeng.si.core.items.SIToolChisel;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import fi.dy.masa.enderutilities.item.tool.ItemEnderSword;
import fi.dy.masa.enderutilities.item.tool.ItemEnderTool;
import fi.dy.masa.enderutilities.item.tool.ItemEnderTool.ToolType;

public class SILib {
	
	public static boolean chance(int percentage){
		int rand = ThreadLocalRandom.current().nextInt(0,100);
		return rand < percentage;
	}
	
	public static void checkDependencies(){
		for(Map.Entry<String,String> entry : SI.CONFIG.moddependencies.entrySet()){
			if(!Loader.isModLoaded(entry.getKey())){
				String message = "The mod " + entry.getValue() + " is missing!\n\nSurvival Industry is not supposed to run without it. We strongly recommend installing it.\n\nIf you do not, these possible problems are all yours to keep:\n\n- Broken progression\n- Missing blocks and items\n- Ruined game experience\n- Horrible crashes of doom\n- Feeling miserable\n\nWe will not care, and will not help, but keep bugging you with this window.";
				if(!SI.isServer){
					JFrame frame = new JFrame();
					JOptionPane.showMessageDialog(frame, message,"The mod " + entry.getValue() + " is missing!",JOptionPane.WARNING_MESSAGE);
				}
				
				FMLLog.bigWarning(message);
				
			}
		}
	}
	
	public static int itemStackSizeFromString(String inputstring){
		if(inputstring.contains("@")) {
			String[] splits = inputstring.split("@");
			return Integer.parseInt(splits[1]);
		} else return 1;
	}
	
	public static ItemStack stringToItemStackNoOreDict(String inputstring){
		if(inputstring == null) return null;
		Object is = stringToItemStack(inputstring);
		if(is instanceof ItemStack) return (ItemStack)is;
		else return null;
	}
	
	public static Object stringToItemStack(String inputstring) {
		if(inputstring == null) return null;
		int amount = 1;
		if(inputstring.contains("@")) {
			String[] splits = inputstring.split("@");
			amount = Integer.parseInt(splits[1]);
			inputstring = splits[0];
		}
		
		String name = substituteItemStackName(inputstring);
		
		if (name.contains(":")) {
			String[] splits = name.split(":");
			ItemStack stack = GameRegistry.findItemStack(splits[0], splits[1], amount);
			if(stack == null){
				SI.LOG.warn("[Missing ItemStack] Could not find " + name + ".");
				return name;
			}
			if (splits.length == 3) {
				if(splits[2].equals("*")) stack.setItemDamage(32767); else
				stack.setItemDamage(Integer.parseInt(splits[2]));
			}
			return stack;
		} else {
			return name;
		}
	}
	
	public static String substituteItemStackName(String name) {
		boolean changed = true;
		String toSub = name;
		int iterations = 0;
		int i;
		if(SI.CONFIG.itemstacksubstitutes.size() > 0) while(changed){
			changed = false;
			for(i = 0;i<SI.CONFIG.itemstacksubstitutes.size();i++){
				
				if(SI.CONFIG.itemstacksubstitutes.get(i).source.equals(toSub)){
					toSub = SI.CONFIG.itemstacksubstitutes.get(i).target;
					changed = true;
				}
				
			}
			iterations++;
			if(iterations>SI.CONFIG.itemstacksubstitutes.size()){
				FMLLog.bigWarning("The substituteable itemStack " + name + " undergoes a circular chain of substitutions. This doesn't work, hence the original itemStack was returned. Please check your config!");
				return name;
			}
		}
		return toSub;
	}
	
	public static boolean belongToSameOreDictEntry(ItemStack one, ItemStack two){
		if(one == null || two == null) return false;
		for(Integer idOne : OreDictionary.getOreIDs(one)){
			for(Integer idTwo : OreDictionary.getOreIDs(two)){
				if(idTwo.equals(idOne))	return true;
			}
		}
		return false;
	}
	
	public static ItemStack getRepairItemStack(Item item){
		ItemStack returnstack = null;
		
		if(item instanceof ItemTool) 	returnstack = ToolMaterial.valueOf(((ItemTool)item).getToolMaterialName()).getRepairItemStack(); else
			if(item instanceof ItemHoe) 	returnstack = ToolMaterial.valueOf(((ItemHoe)item).getToolMaterialName()).getRepairItemStack(); else 
			if(item instanceof ItemSword) returnstack = ToolMaterial.valueOf(((ItemSword)item).getToolMaterialName()).getRepairItemStack(); else
			if(item instanceof IChiselItem) {
				if(Loader.isModLoaded("chisel") && item instanceof ItemChisel){
					if(item.equals(ChiselItems.chisel)) returnstack = new ItemStack(Items.iron_ingot);
					if(item.equals(ChiselItems.diamondChisel)) returnstack = new ItemStack(Items.diamond);
					if(item.equals(ChiselItems.obsidianChisel)) returnstack = new ItemStack(Blocks.obsidian);
				} else {
					if(item instanceof SIToolChisel){
						returnstack = ((SIToolChisel)item).getRepairItemStack();
					}
				}
			}
		return returnstack;
	}
	
	public static Float getRepairAmount(Item item){
		Float repairamount = 0F;
		if(!Loader.isModLoaded("enderutilities") || !((item instanceof ItemEnderTool) || (item instanceof ItemEnderSword))){
			if(item instanceof ItemPickaxe || item instanceof ItemAxe) repairamount = 1F/3F; else
			if(item instanceof ItemHoe || item instanceof ItemSword) repairamount = 1F/2F; else
			if(item instanceof ItemSpade || item instanceof IChiselItem) repairamount = 1F;
		} else {
			if(item instanceof ItemEnderSword) repairamount = 1F/2F; else {
				ItemEnderTool enderTool = ((ItemEnderTool)item);
				ToolType enderToolType = enderTool.getToolType(new ItemStack(item));
				if(enderToolType == ToolType.PICKAXE || enderToolType == ToolType.AXE) repairamount = 1F/3F; else
				if(enderToolType == ToolType.HOE) repairamount = 1F/2F; else
				if(enderToolType == ToolType.SHOVEL) repairamount = 1F;
			}
		}
		return repairamount;
	}
	
	public static boolean matchItemStacks(ItemStack is1, ItemStack is2){
		return	(	is1 != null && 
					is2 != null && 
					(is1.getItem() == is2.getItem()) &&
					(is1.getItemDamage() == 32767 || is2.getItemDamage() == 32767 || is1.getItemDamage() == is2.getItemDamage())
				);
	}
	
	public static boolean matchItemStacksOreDict(Object is1, Object is2){
		if(is1 == null){
			if(is2==null) return true; else return false;
		}
		
		if(is1 instanceof String){
			if(is2 instanceof ItemStack) return matchItemStacksOreDict(is2,is1);
			if(is2 instanceof String) return is1.equals(is2);
		}
		if(is1 instanceof ItemStack){
			if(is2 instanceof ItemStack) return matchItemStacks((ItemStack)is1,(ItemStack)is2);
			if(is2 instanceof String){
				FMLLog.info("matching IS to string");
				FMLLog.info(is1 + " " + is2);
				
				if(((String) is2).contains(":")){
					Object is2o = SILib.stringToItemStack((String)is2);
					if(is2o instanceof ItemStack) return matchItemStacks((ItemStack)is1,(ItemStack)is2o);
					else return false;
				}
				
				int[] oreids = OreDictionary.getOreIDs((ItemStack)is1);
				if(oreids.length < 1) return false;
				int ore2id = OreDictionary.getOreID((String)is2);
				boolean match = false;
				for(int oreid : oreids){
					match |= (oreid == ore2id);
				}
				return match;
			}
		}
		
		return false;
	}
	
	public static ItemStack getFirstOreDictMatch(String oredictentry){
		ItemStack is = null;
		if(OreDictionary.doesOreNameExist(oredictentry) && OreDictionary.getOres(oredictentry).size() > 0){
			is = OreDictionary.getOres(oredictentry).get(0);
		} else FMLLog.warning("Failed to find an OreDict entry for " + oredictentry);
		
		return is;
		
		
	}
	
	public static  <T> T[] convertArray(Object o, Class<T[]> type){
		Class<?> som;
		try {
			som = o.getClass();
			Object vals = som.getMethod("values").invoke(o);
			Collection<?> col = (Collection<?>)vals;
			Object[] oa = col.toArray(); 
			return Arrays.copyOf(oa, oa.length,type);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("nooo");
		}
	}
	
	public static  List<String[]> convertPattern(Object o){
		List<String[]> rows = new ArrayList<>();
		Class<?> som;
		Class<?> somsub;

		try {
			som = o.getClass();
			Object vals = som.getMethod("values").invoke(o);
			Collection<?> col = (Collection<?>)vals;
			Object[] oa = col.toArray(); 
			for(int x = 0;x < oa.length;x++){
				somsub = oa[x].getClass();
				Object valssub = somsub.getMethod("values").invoke(oa[x]);
				Collection<?> colsub = (Collection<?>)valssub;
				Object[] ob = colsub.toArray();
				rows.add(Arrays.copyOf(ob,ob.length,String[].class));
			}
			
			return rows;
			
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("nooo");
		}
	}
	
}
