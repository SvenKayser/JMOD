package com.jeffpeng.jmod;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import javax.script.Bindings;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.jeffpeng.jmod.API.forgeevents.JMODGetRepairAmountEvent;
import com.jeffpeng.jmod.API.forgeevents.JMODGetRepairItemStackEvent;
import com.jeffpeng.jmod.API.forgeevents.JMODHideItemStackEvent;
import com.jeffpeng.jmod.API.forgeevents.JMODPatchToolEvent;
import com.jeffpeng.jmod.primitives.OwnedObject;
import com.jeffpeng.jmod.registry.BlockMaterialRegistry;
import com.jeffpeng.jmod.util.Reflector;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;


public class Lib extends OwnedObject {
	
	public Lib(JMODRepresentation jmod){
		super(jmod);
	}
	
	public static CreativeTabs getCreativeTabByName(String name) {
		for (int i = 0; i < CreativeTabs.creativeTabArray.length; i++) {
			String tabname = CreativeTabs.creativeTabArray[i].getTabLabel();
			if (tabname.matches(name))
				return CreativeTabs.creativeTabArray[i];
		}
		return null;
	}
	
	public static BlockMaterialRegistry blockMaterialRegistry;
	
	public static Material getBlockMaterial(String blockMaterialName) {
		return blockMaterialRegistry.get(blockMaterialName);
	}
	
	public static boolean chance(int percentage){
		int rand = ThreadLocalRandom.current().nextInt(0,100);
		return rand <= percentage;
	}
	
	public void displayWarningMessage(String title, String message){
		if(!JMOD.isServer()){
			JFrame frame = new JFrame();
			JOptionPane.showMessageDialog(frame, message,title,JOptionPane.WARNING_MESSAGE);
			
		}
		log.warn(message);
	}
	
	public void displayErrorMessage(String title, String message){
		if(!JMOD.isServer()){
			JFrame frame = new JFrame();
			JOptionPane.showMessageDialog(frame, message,title,JOptionPane.ERROR_MESSAGE);
		}
		log.error(message);
		throw new RuntimeException(message);
	}
	
	@SuppressWarnings("unchecked")
	public void checkDependencies(){
		for(Map.Entry<String,String> entry : ((Map<String,String>) config.get("moddependencies")).entrySet()){
			if(!Loader.isModLoaded(entry.getKey())){
				String message = "The mod " + entry.getValue() + " is missing!\n\n" + owner.getModName() + " is not supposed to run without it. We strongly recommend installing it.\n\nIf you do not, these possible problems are all yours to keep:\n\n- Broken progression\n- Missing blocks and items\n- Ruined game experience\n- Horrible crashes of doom\n- Feeling miserable\n\nWe will not care, and will not help, but keep bugging you with this window.";
				String title = "The mod " + entry.getValue() + " is missing!";
				displayWarningMessage(title,message);
			}
		}
	}
	
	@Deprecated
	public static int itemStackSizeFromString(String inputstring){
		if(inputstring.contains("@")) {
			String[] splits = inputstring.split("@");
			return Integer.parseInt(splits[1]);
		} else return 1;
	}
	
	public static boolean itemStackIsBlock(ItemStack input){
		if(input == null || input.getItem() == null || Block.getBlockFromItem(input.getItem()) == Blocks.air) return false;
		return true;
	}
	
	public static Block getBlockFromItemStack(ItemStack input){
		if(input == null || input.getItem() == null) return null;
		Block ret = Block.getBlockFromItem(input.getItem());
		if(ret == Blocks.air) return null;
		return ret;
	}
	
	@Deprecated
	public ItemStack[] stringToItemStackArray(String inputstring){
		Object is = stringToItemStack(inputstring);
		if(is instanceof ItemStack){
			ItemStack[] retstack = new ItemStack[1];
			retstack[0] = (ItemStack)is;
			return retstack;
		}
		
		if(is instanceof String){
			if(OreDictionary.doesOreNameExist((String)is)){
				List<ItemStack> orelist = OreDictionary.getOres((String) is);
				
				if(orelist.size()>0){
					ItemStack[] retstack = new ItemStack[orelist.size()];
					for(int c = 0; c < orelist.size(); c++){
						retstack[c] = orelist.get(c);
					}
					return retstack;
				}
			} 
		}
		ItemStack[] retstack = new ItemStack[1];
		retstack[0] = null;
		return retstack;
	}
	
	@Deprecated
	public static ItemStack stringToItemStackOrFirstOreDict(String inputstring){
		if(inputstring == null) return null;
		Object is = stringToItemStack(inputstring);
		if(is instanceof ItemStack) return (ItemStack)is;
		else {
			int amount = 1;
			if(inputstring.contains("@")) {
				String[] splits = inputstring.split("@");
				amount = Integer.parseInt(splits[1]);
				inputstring = splits[0];
			}
			
			
			if(OreDictionary.doesOreNameExist((String)is)){
				List<ItemStack> orelist = OreDictionary.getOres((String) is);
				if(orelist.size() > 0){
					ItemStack returnstack = orelist.get(0); 
					returnstack.stackSize = amount;
					return returnstack;
				}
			}
		}
		return null;
	}
	
	@Deprecated
	public static ItemStack stringToItemStackNoOreDict(String inputstring){
		if(inputstring == null) return null;
		Object is = stringToItemStack(inputstring);
		if(is instanceof ItemStack) return (ItemStack)is;
		else return null;
	}
	
	@Deprecated
	public static Object stringToItemStack(String inputstring) {
		if(inputstring == null){
			//jmod.getLogger().warn("[ItemStackString parser] Received a null string");
			return null;
		}
		int amount = 1;
		if(inputstring.contains("@")) {
			String[] splits = inputstring.split("@");
			amount = Integer.parseInt(splits[1]);
			inputstring = splits[0];
		}
		
		String name = inputstring;
		
		if (name.contains(":")) {
			String[] splits = name.split(":");
			ItemStack stack = GameRegistry.findItemStack(splits[0], splits[1], amount);
			if(stack == null){
				JMOD.LOG.warn("[ItemStackString parser] Could not find " + name + ".");
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
	

	/**
	 * Looks for inputstring ItemStack
	 * <p>
	 * Should be used to find ItemStacks no OreDic lookup is done
	 * </p>
	 * @param inputstring the Item to lookup
	 * @return an Optional<ItemStack> if the inputstring is a valid Item. 
	 */
	@Deprecated
	public Optional<ItemStack> stringToMaybeItemStackNoOreDic(String inputstring) {
		return Optional.ofNullable(stringToItemStackNoOreDict(inputstring));
	}
	
	/**
	 * Does lookup on inputstring. inputstring could be an OreDic String.
	 * @param inputstring the Item to lookup
	 * @return an Optional<Object> is an ItemStack or String
	 */
	@Deprecated
	public Optional<Object> stringToMaybeItemStack(String inputstring) {
		return Optional.ofNullable(stringToItemStack(inputstring));
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
		JMODGetRepairItemStackEvent event = new JMODGetRepairItemStackEvent(item);
		ItemStack returnstack = null;
		if(JMOD.BUS.post(event)){
			returnstack = event.getRepairItemStack();
			if(returnstack != null) return event.getRepairItemStack();
		}
		if(item instanceof ItemTool) 	returnstack = ToolMaterial.valueOf(((ItemTool)item).getToolMaterialName()).getRepairItemStack(); else
		if(item instanceof ItemHoe) 	returnstack = ToolMaterial.valueOf(((ItemHoe)item).getToolMaterialName()).getRepairItemStack(); else 
		if(item instanceof ItemSword) returnstack = ToolMaterial.valueOf(((ItemSword)item).getToolMaterialName()).getRepairItemStack(); 
		return returnstack;
	}
	
	public static Float getRepairAmount(Item item){
		JMODGetRepairAmountEvent event = new JMODGetRepairAmountEvent(item);
		JMOD.BUS.post(event);
		Float fa = event.getRepairAmount();
		if(fa!=0F) return fa;
		
		if(item instanceof ItemPickaxe || item instanceof ItemAxe) return 1F/3F; else
		if(item instanceof ItemHoe || item instanceof ItemSword) return 1F/2F; else
		if(item instanceof ItemSpade) return 1F;
		if(item instanceof ItemArmor) {
			int armortype = ((ItemArmor) item).armorType;
			if (armortype == 0)	return 1F / 5F;
			if (armortype == 1) return 1F / 8F;
			if (armortype == 2) return 1F / 7F;
			if (armortype == 3) return 1F / 4F;
		}
		
		return 0F;
	}
	
	public static boolean matchItemStacks(ItemStack is1, ItemStack is2){
		return	(	is1 != null && 
					is2 != null && 
					(is1.getItem() == is2.getItem()) &&
					(is1.getItemDamage() == 32767 || is2.getItemDamage() == 32767 || is1.getItemDamage() == is2.getItemDamage())
				);
	}
	
	public boolean matchItemStacksOreDict(Object is1, Object is2){
		if(is1 == null){
			if(is2==null) return true; else return false;
		}
		
		if(is1 instanceof String){
			if(is2 instanceof ItemStack) return matchItemStacksOreDict(is2,is1);
			if(is2 instanceof String) return is1.equals(is2);
		}
		if(is1 instanceof ItemStack){
			if(is2 instanceof ItemStack) return belongToSameOreDictEntry((ItemStack)is1,(ItemStack)is2);
			if(is2 instanceof String){
				
				if(((String) is2).contains(":")){
					Object is2o = stringToItemStack((String)is2);
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
	
	public FluidStack stringToFluidStack(String input){
		if(input == null){
			owner.getLogger().warn("[FluidString parser] Received a null string");
			return null;
		}
		FluidStack fs = null;
		if(input.contains("@")){
			String[] splits = input.split("@");
			if(FluidRegistry.isFluidRegistered(splits[0])){
				fs = FluidRegistry.getFluidStack(splits[0], Integer.parseInt(splits[1]));
			}
		}
		
		if(fs == null) owner.getLogger().warn("[FluidString parser] " + input + " does not translate to a valid fluid"); 
		return fs;
	}
	
	public static ItemStack getFirstOreDictMatch(String oredictentry){
		ItemStack is = null;
		if(OreDictionary.doesOreNameExist(oredictentry) && OreDictionary.getOres(oredictentry).size() > 0){
			is = OreDictionary.getOres(oredictentry).get(0);
		} else JMOD.LOG.warn("Failed to find an OreDict entry for " + oredictentry);
		
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
	
	public ShapedOreRecipe convertPatterToShapedOreRecipe(ItemStack result, Object o){
		List<String[]> pattern = convertPattern(o);
		
		Character[] letters = new Character[]{'a','b','c','d','e','f','g','h','i'};
		
		List<Object> olist = new ArrayList<>();
		Character[] slist = new Character[9];
		List<Object> rlist = new ArrayList<>();
		
		for(int y = 0; y < 3; y++){
			for(int x = 0; x < 3; x++){
				int c = x+y*3;
				if(pattern.size() > y && pattern.get(y)[x] != null){
					slist[c] = letters[c];
					
				} else {
					slist[c] = ' ';
				}
				olist.add(pattern.get(y)[x]);
			}
		}
		
		rlist.add(slist[0].toString()+slist[1].toString()+slist[2].toString());
		rlist.add(slist[3].toString()+slist[4].toString()+slist[5].toString());
		rlist.add(slist[6].toString()+slist[7].toString()+slist[8].toString());
						
		for(int x = 0; x < 9; x++){
			if(olist.get(x) != null){
				rlist.add(slist[x]);
				if(olist.get(x) instanceof String)
					rlist.add(stringToItemStack((String)olist.get(x)));
				else 
					rlist.add(olist.get(x));
			} 
		}
		
		return new ShapedOreRecipe(result,rlist.toArray());
		
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
			e.printStackTrace();
			throw new RuntimeException("Something went wrong converting a recipe pattern.");
		}
	}
	
	public static String blockyfy(String name) {
		String newname = "block";
		newname += ((Character) name.charAt(0)).toString().toUpperCase();
		newname += name.substring(1);
		return newname;
	}

	public static String ingotyfy(String name) {
		String newname = "ingot";
		newname += ((Character) name.charAt(0)).toString().toUpperCase();
		newname += name.substring(1);
		return newname;
	}
	
	public static void patchArmor() {

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
	
	public static void hideItemStack(ItemStack is){
		JMOD.BUS.post(new JMODHideItemStackEvent(is));
	}


	public static void patchTools() {

		FMLControlledNamespacedRegistry<Item> gamereg = GameData.getItemRegistry();

		@SuppressWarnings("unchecked")
		Set<String> allItems = GameData.getItemRegistry().getKeys();

		
		
		for (String itemname : allItems) {
			Item item = gamereg.getObject(itemname);
			if(JMOD.BUS.post(new JMODPatchToolEvent(item,itemname))) continue;

			
			if (item instanceof ItemTool || item instanceof ItemHoe || item instanceof ItemSword) {

				if (item.getClass().getCanonicalName().contains("Reika.RotaryCraft")) {
					continue;
				}

				ToolMaterial toolmat = null;
				String toolmatname = null;
				if (item instanceof ItemTool)
					toolmatname = ((ItemTool) item).getToolMaterialName();
				else if (item instanceof ItemHoe)
					toolmatname = ((ItemHoe) item).getToolMaterialName();
				else if (item instanceof ItemSword)
					toolmatname = ((ItemSword) item).getToolMaterialName();

				if (toolmatname == null) {
				} else {
					try {
						toolmat = ToolMaterial.valueOf(toolmatname);
					} catch (IllegalArgumentException e) {
						toolmat = null;
					}
				}

				if (toolmat == null) {
				} else {
					// Update the tool material
					if (item instanceof ItemTool) {
						
						Reflector itemreflector = new Reflector(item, ItemTool.class);
						Float damagemodifier = 0F;
						if (item instanceof ItemAxe)
							damagemodifier = 3F;
						if (item instanceof ItemPickaxe)
							damagemodifier = 2F;
						if (item instanceof ItemSpade)
							damagemodifier = 1F;

						itemreflector.set(3, toolmat).set(2, toolmat.getDamageVsEntity() + damagemodifier)
								.set(1, toolmat.getEfficiencyOnProperMaterial());
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

					if (item instanceof ItemTool)
						JMOD.LOG.info("[tool patcher] " + itemname + " is a " + ((ItemTool) item).getToolMaterialName() + " tool (" + item.getClass().getName() + ")");
					if (item instanceof ItemHoe)
						JMOD.LOG.info("[tool patcher] " + itemname + " is a " + ((ItemHoe) item).getToolMaterialName() + " hoe (" + item.getClass().getName() + ")");
					if (item instanceof ItemSword)
						JMOD.LOG.info("[tool patcher] " + itemname + " is a " + ((ToolMaterial) new Reflector(item, ItemSword.class).get(1)).name() + " weapon (basically a sword) (" + item.getClass().getName() + ")");
				}
			}
		}
	}
	
	public static NBTTagCompound bindingsToNBTTagCompound(Bindings b){
		System.out.println("###btnbt " + b.getClass());
		NBTTagCompound nbt = new NBTTagCompound();
		b.forEach((key,v) -> {
			
			System.out.println(key + " " + v);
			if(v instanceof String)		nbt.setString	(key,	(String)v); 	else
			if(v instanceof Integer) 	nbt.setInteger	(key, 	(Integer)v); 	else
			if(v instanceof Long) 		nbt.setLong		(key, 	(Long)v); 		else
			if(v instanceof Float)		nbt.setFloat	(key,	(Float)v);		else
			if(v instanceof Short)		nbt.setShort	(key,	(Short)v);		else
			if(v instanceof Byte)		nbt.setByte 	(key,	(Byte)v);		else
			if(v instanceof Bindings){
				System.out.println(v.toString());
				if(v.toString().equals("[object Array]")){
					System.out.println("isarray");
					nbt.setTag		(key,	bindingsToNBTTagList((Bindings)v));
				} else {
					System.out.println("isnotarray");
					nbt.setTag		(key,	bindingsToNBTTagCompound((Bindings)v));
				}
			}
		});
		return nbt;
	}
	
	private static NBTTagList bindingsToNBTTagList(Bindings b){
		try{
			NBTTagList nbt = new NBTTagList();
			
			b.forEach((key,v) -> {
				System.out.println(key + " " + v);
				System.out.println(v.toString());
				if(v.toString().equals("[object Array]")){
					System.out.println("isarray");
					nbt.appendTag(bindingsToNBTTagList((Bindings)v));
				} else {
					System.out.println("isnotarray");
					nbt.appendTag(bindingsToNBTTagCompound((Bindings)v));
				}
			});
			return nbt;
		} catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException("Nope");
		}
		
	}
	
	
	
	
	
	
	public static ForgeDirection getForgeDirectionFromMOPSide(int side){
		if(side == 0) return ForgeDirection.DOWN;
		if(side == 1) return ForgeDirection.UP;
		if(side == 2) return ForgeDirection.EAST;
		if(side == 3) return ForgeDirection.WEST;
		if(side == 4) return ForgeDirection.NORTH;
		if(side == 5) return ForgeDirection.SOUTH;
		return null;
	}
	
	

	// TODO: There must be a more elegant solution to this.
	// Side constants
	
	
	
	
	
	
	
	
	
}
