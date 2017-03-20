package com.jeffpeng.jmod.actions;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.descriptors.ItemStackDescriptor;
import com.jeffpeng.jmod.primitives.BasicAction;

public class SetItemProperties extends BasicAction {

	private ItemStackDescriptor itemdesc;

	public SetItemProperties(JMODRepresentation owner, ItemStackDescriptor itemdesc) {
		super(owner);
		this.itemdesc = itemdesc;
		this.valid = true;
	}

	private ItemStackDescriptor container;
	private Integer stackSize;
	private Integer durability;

	public SetItemProperties stackSize(int stackSize) {
		this.stackSize = stackSize;
		return this;
	}

	public SetItemProperties container(String container) {
		this.container = new ItemStackDescriptor(owner,container);
		return this;
	}

	public SetItemProperties container(ItemStackDescriptor container) {
		this.container = container;
		return this;
	}

	public SetItemProperties durability(int durability) {
		this.durability = durability;
		return this;
	}

	public void execute() {
		List<ItemStack> isl = itemdesc.getItemStackList();
		if (isl.size() > 0) {
			for (ItemStack is : isl) {
				Item item = is.getItem();

				if (stackSize != null)
					item.setMaxStackSize(this.stackSize);
				if (container != null) {
					ItemStack cis = container.toItemStack();
					if (cis != null)
						item.setContainerItem(cis.getItem());
				}
				if (durability != null)
					item.setMaxDamage(durability);

			}

		} else {
			log.warn("Cannot patch item properties for " + itemdesc + " as it is not in the game. Omitting.");
		}

	}

}
