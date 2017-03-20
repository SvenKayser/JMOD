package com.jeffpeng.jmod.descriptors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.script.Bindings;

import org.apache.commons.lang3.StringUtils;

import com.jeffpeng.jmod.Defines.UseOredict;
import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;
import com.jeffpeng.jmod.interfaces.ICreationAction;
import com.jeffpeng.jmod.primitives.OwnedObject;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

/**
 * This class aims to unify the string based handling of ItemStacks on JMODs
 * side. Up until now many functions in the in {@link Lib} have been used to
 * manage this. This class, however, should provide a more unified and easier to
 * work with approach.
 * <p>
 * Basically an ItemStackDescriptor is an alternative representation of what an
 * ItemStack is in MineCraft, without relying on any parts of the game being
 * already initialized. This allows JMOD to work with these (pseudo) ItemStacks
 * long before the Game engine is done creating them.
 * <p>
 * Additionally the ItemStackDescriptor is capable of transparently working
 * with OreDictionary entries as if they were ItemStack, returning either the
 * first match with toItemStack() or a list of all matching ItemStacks with
 * getItemStackList(). It is possible to return even single ItemStacks
 * as List<ItemStack> with this method. 
 * 
 * @author SvenKayser
 * @since 1.5.x
 * 
 */

public class ItemStackDescriptor extends OwnedObject {
	/**
	 * JMOD sets this once it reaches FMLInitialization. At this point all items
	 * are expected to be already known to the game.
	 */
	private static boolean ready = false;

	/**
	 * Marks that operations requiring the use of actual items and itemstacks
	 * from the game are now possible. Only {@link JMOD} should call this.
	 * 
	 * @return void
	 */
	public static void markReady() {
		ready = true;
	}

	private String domain;
	private String name;
	private int meta = 0;
	private int stacksize = 1;
	private NBTTagCompound nbt = null;
	private boolean possibleOreDictionaryEntry = false;
	private ItemStack cachedItemStack = null;
	private int metaTo = 0;
	private List<Integer> metalist = new ArrayList<>();

	/**
	 * @param owner
	 *            The owning JMODRepresentation
	 * @param domain
	 *            The namespace of the mod this item belongs to
	 * @param name
	 *            The internal name of the Ttem belonging to the ItemStack
	 */
	public ItemStackDescriptor(JMODRepresentation owner, String domain, String name) {
		super(owner);
		this.domain = domain;
		this.name = name;
	}

	public ItemStackDescriptor(JMODRepresentation owner, String domain, String name, int stacksize, int meta) {
		this(owner, domain, name);
		this.name = name;
		this.meta = meta;
		this.stacksize = stacksize;
	}

	public ItemStackDescriptor(JMODRepresentation owner, String domain, String name, int stacksize, int meta, Bindings nbt) {
		this(owner, domain, name, meta, stacksize);
		this.nbt = Lib.bindingsToNBTTagCompound(nbt);
	}

	public ItemStackDescriptor(JMODRepresentation owner, String name, int meta, int stacksize, boolean oreDict) {
		super(owner);
		this.possibleOreDictionaryEntry = oreDict;
		this.meta = meta;
		this.stacksize = stacksize;
		this.name = name;
	}

	public ItemStackDescriptor(JMODRepresentation owner, String inputstring) {
		super(owner);
		if (inputstring == null)
			return;
		if (inputstring.contains("@")) {
			String[] splits = inputstring.split("@");
			if (StringUtils.isNumeric(splits[1]))
				this.stacksize = Integer.parseInt(splits[1]);
			inputstring = splits[0];
		}

		if (name.contains(":")) {
			String[] splits = inputstring.split(":");
			if (splits.length >= 3) {
				this.domain = splits[0];
				this.name = splits[1];
				if (splits[2].equals("*"))
					this.meta = Short.MAX_VALUE;
				else if (StringUtils.isNumeric(splits[2]))
					this.meta = Integer.parseInt(splits[2]);
				possibleOreDictionaryEntry = false;
			} else {
				if (splits[1].equals("*") || StringUtils.isNumeric(splits[1])) {
					this.name = splits[0];
					this.meta = Integer.parseInt(splits[1]);
					if (hasOwner())
						this.domain = owner.getModId();
					else
						this.domain = JMOD.MODID;
				} else {
					this.name = splits[1];
					this.domain = splits[0];
				}
			}
			possibleOreDictionaryEntry = false;
		} else {
			if (hasOwner())
				this.domain = owner.getModId();
			else
				this.domain = JMOD.MODID;
			this.name = inputstring;
			possibleOreDictionaryEntry = true;
		}
	}
	
	public ItemStackDescriptor(JMODRepresentation owner, ItemStack is) {
		super(owner);
	}
	
	public ItemStackDescriptor(JMODRepresentation owner, ICreationAction ica){
		super(owner);
		this.name = ica.getName();
		this.domain = ica.getDomain();		
	}

	private void markChange() {
		cachedItemStack = null;
	}

	public void setName(String name) {
		markChange();
		this.name = name;
	}

	public void setDomain(String domain) {
		markChange();
		this.domain = domain;
	}

	public void setStackSize(int stacksize) {
		markChange();
		this.stacksize = stacksize;
	}

	public void setMeta(int meta) {
		cachedItemStack = null;
		this.meta = meta;
		this.metaTo = 0;
	}

	public void setMetaRange(int from, int to) {
		cachedItemStack = null;
		this.meta = from;
		this.metaTo = to;
	}

	public void setMetaList(List<Integer> list) {
		cachedItemStack = null;
		this.metalist = list;
	}

	public void setMetaList(Bindings list) {
		cachedItemStack = null;
		this.metalist = new ArrayList<>();
		list.forEach((k, v) -> {
			if (v instanceof Integer)
				metalist.add((Integer) v);
		});
	}

	public void addToMetaList(Integer i) {
		cachedItemStack = null;
		metalist.add(i);
	}

	public void emptyMetaList() {
		cachedItemStack = null;
		this.metalist = new ArrayList<>();
	}

	public void setNBT(NBTTagCompound nbt) {
		cachedItemStack = null;
		this.nbt = nbt;
	}

	public void setNBT(Bindings nbt) {
		cachedItemStack = null;
		this.nbt = Lib.bindingsToNBTTagCompound(nbt);
	}

	public String getName() {
		return name;
	}

	public String getDomain() {
		return domain;
	}

	public int getStackSize() {
		return stacksize;
	}

	public int getMeta() {
		return meta;
	}

	public ItemStack toItemStack() {
		return toItemStack(UseOredict.ALLOW);
	}

	public Optional<ItemStack> toMaybeItemStack() {
		return Optional.ofNullable(toItemStack(UseOredict.ALLOW));
	}

	public Optional<ItemStack> toMaybeItemStack(int preferOreDictMatch) {
		return Optional.ofNullable(toItemStack(preferOreDictMatch));
	}

	public List<ItemStack> getItemStackList() {
		return getItemStackList(UseOredict.PREFER);
	}

	public ItemStack toItemStack(int preferOreDictMatch) {
		if (!ready) {
			log.error("You cannot get real ItemStacks from the ItemStackDescriptor before FMLInitialization phase.");
			return null;
		}
		if (cachedItemStack != null)
			return cachedItemStack;
		if (name != null) {

			if (preferOreDictMatch >= UseOredict.PREFER && possibleOreDictionaryEntry && OreDictionary.doesOreNameExist(name)) {
				cachedItemStack = OreDictionary.getOres(name).get(0);
				return cachedItemStack;
			}

			if (preferOreDictMatch < UseOredict.EXCLUSIVE) {
				if (domain == null)
					domain = JMOD.MODID;
				Item returnItem = GameRegistry.findItem(domain, name);
				if (returnItem != null) {
					cachedItemStack = new ItemStack(returnItem, stacksize, meta);
					if (stacksize <= 1 && nbt != null)
						cachedItemStack.setTagCompound(nbt);
					return cachedItemStack;
				}
			}

			if (preferOreDictMatch == UseOredict.ALLOW && possibleOreDictionaryEntry && OreDictionary.doesOreNameExist(name)) {
				cachedItemStack = OreDictionary.getOres(name).get(0);
				return cachedItemStack;
			}
		}
		return null;
	}

	public List<ItemStack> getItemStackList(int preferOreDictMatch) {
		final List<ItemStack> returnList = new ArrayList<>();
		if (!ready) {
			log.error("You cannot get real ItemStacks from the ItemStackDescriptor before FMLInitialization phase.");
			return returnList;
		}
		if (name != null) {
			if (preferOreDictMatch >= UseOredict.PREFER && possibleOreDictionaryEntry && OreDictionary.doesOreNameExist(name)) {
				return OreDictionary.getOres(name);
			}

			if (preferOreDictMatch < UseOredict.EXCLUSIVE) {
				if (domain == null)
					domain = JMOD.MODID;
				Item returnItem = GameRegistry.findItem(domain, name);
				if (returnItem != null) {
					if (metalist != null) {
						metalist.forEach((v) -> returnList.add(new ItemStack(returnItem, stacksize, v)));
						return returnList;
					}

					if (metaTo > meta) {
						for (int x = meta; x <= metaTo; x++) {
							returnList.add(new ItemStack(returnItem, stacksize, x));
						}
					}

					returnList.add(new ItemStack(returnItem, stacksize, meta));
					return returnList;
				}
			}

			if (preferOreDictMatch == UseOredict.ALLOW && possibleOreDictionaryEntry && OreDictionary.doesOreNameExist(name)) {
				return OreDictionary.getOres(name);
			}

		}

		return returnList;
	}

	public Item toItem() {
		if (!ready) {
			log.error("You cannot get real ItemStacks from the ItemStackDescriptor before FMLInitialization phase.");
			return null;
		}
		if (name != null) {
			Item returnItem = GameRegistry.findItem(domain, name);
			if (returnItem != null) {
				return returnItem;
			}

			if (possibleOreDictionaryEntry && OreDictionary.doesOreNameExist(name)) {
				ItemStack tmpstack = OreDictionary.getOres(name).get(0);
				if (tmpstack == null)
					return null;
				return tmpstack.getItem();
			}
		}
		return null;
	}

	public boolean equals(ItemStackDescriptor compareItemStackDescriptor) {
		if (domain.equals(compareItemStackDescriptor.domain) && name.equals(compareItemStackDescriptor.name) && meta == compareItemStackDescriptor.meta
				&& stacksize == compareItemStackDescriptor.stacksize) {
			if (nbt == compareItemStackDescriptor.nbt) {
				return true;
			} else {
				return (nbt != null && compareItemStackDescriptor.nbt != null && nbt.equals(compareItemStackDescriptor.nbt));
			}
		}
		return false;
	}

	public boolean match(ItemStackDescriptor isd) {
		return match(isd, true);
	}

	public boolean match(ItemStackDescriptor compareItemStackDescriptor, boolean matchNBT) {
		if (domain.equals(compareItemStackDescriptor.domain) && name.equals(compareItemStackDescriptor.name)
				&& (meta == compareItemStackDescriptor.meta || meta == Short.MAX_VALUE || compareItemStackDescriptor.meta == Short.MAX_VALUE)) {
			if (!matchNBT || nbt == compareItemStackDescriptor.nbt) {
				return true;
			} else {
				return (nbt != null && compareItemStackDescriptor.nbt != null && nbt.equals(compareItemStackDescriptor.nbt));
			}
		}
		return false;

	}

	public boolean match(ItemStack compareItemStack) {
		return match(compareItemStack, true);
	}

	public boolean match(ItemStack compareItemStack, boolean matchNBT) {
		if (compareItemStack == null)
			return false;
		ItemStack is = toItemStack();
		if (is == null)
			return false;
		if (is.getItem() == compareItemStack.getItem()
				&& (is.getItemDamage() == compareItemStack.getItemDamage() || is.getItemDamage() == 32767 || compareItemStack.getItemDamage() == 32767)) {
			if (!matchNBT || (is.hasTagCompound() == compareItemStack.hasTagCompound())) {
				return true;
			} else {
				return (is.hasTagCompound() && compareItemStack.hasTagCompound() && compareItemStack.getTagCompound().equals(is.getTagCompound()));
			}
		}

		return false;
	}

	public boolean match(String matchIsString) {
		return match(matchIsString, true);
	}

	public boolean match(String matchItemStack, boolean matchNBT) {
		ItemStackDescriptor compareItemStackDescriptor = new ItemStackDescriptor(owner, matchItemStack);
		return match(compareItemStackDescriptor, matchNBT);
	}
	
	public String getAsOreDictEntry(){
		if(OreDictionary.doesOreNameExist(name)) return name;
		return null;
	}
	
	
	
	
	
	

	@Override
	public String toString() {
		if (domain != null && name != null)
			return domain + ":" + name + ":" + meta + "@" + stacksize;
		return "#invalid";
	}
	
	public String toItemStackString() {
		if (domain != null && name != null)
			return domain + ":" + name + ":" + meta + "@" + stacksize;
		return "#invalid";
	}

}
