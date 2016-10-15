package com.jeffpeng.jmod.actions.rotarycraft;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import Reika.RotaryCraft.Auxiliary.RecipeManagers.RecipeHandler.RecipeLevel;
import Reika.RotaryCraft.Auxiliary.RecipeManagers.RecipesCentrifuge;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;

public class AddCentrifugeRecipe extends BasicAction {

	private static Method addCentrifugeRecipe = null;
	private static Class<?> FluidOut = null;
	private static Constructor<?> FluidOutConstructor = null;

	private String inputitemString;

	private String outputfluidString = null;
	private Float outputfluidChance = 0F;

	private List<CentrifugeOutput> centrifugeOutputs = new ArrayList<>();

	private List<Object> outputitems = new ArrayList<>();
	private Object[] outputitemsarray;

	private ItemStack[] in;
	private FluidStack outFluid;
	private List<Item> outStacks = new ArrayList<>();

	@SuppressWarnings("rawtypes")
	private static void initReflection() {
		// TODO: This whole atrocity is only here because of a bug with the
		// Centrifuge API in v10x, remove when v12 hits and works
		if (addCentrifugeRecipe == null) {
			try {
				FluidOut = Class.forName("Reika.RotaryCraft.Auxiliary.RecipeManagers.RecipesCentrifuge$FluidOut");

				Class[] cArg1 = new Class[2];
				cArg1[0] = FluidStack.class;
				cArg1[1] = float.class;

				FluidOutConstructor = FluidOut.getDeclaredConstructor(cArg1);
				FluidOutConstructor.setAccessible(true);

				Class[] cArg2 = new Class[4];
				cArg2[0] = ItemStack.class;
				cArg2[1] = FluidOut;
				cArg2[2] = RecipeLevel.class;
				cArg2[3] = Object[].class;

				addCentrifugeRecipe = RecipesCentrifuge.class.getDeclaredMethod("addRecipe", cArg2);
				addCentrifugeRecipe.setAccessible(true);

			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("error acquiring access to the centrifuge function");
			}
		}
	}

	public AddCentrifugeRecipe(JMODRepresentation owner, String inputitem) {
		super(owner);
		this.inputitemString = inputitem;
	}

	public AddCentrifugeRecipe addFluidOutput(String fluidstring, float fluidchance) {
		this.outputfluidString = fluidstring;
		this.outputfluidChance = fluidchance;
		return this;
	}

	public AddCentrifugeRecipe addOutput(String string, float chance) {
		centrifugeOutputs.add(new CentrifugeOutput(string, chance));
		return this;
	}

	@Override
	public boolean on(FMLLoadCompleteEvent event) {
		initReflection();
		try {
			this.in = lib.stringToItemStackArray(inputitemString);
			this.outFluid = null;
			if (this.outputfluidString != null) {
				this.outFluid = lib.stringToFluidStack(outputfluidString);
			}
			for (CentrifugeOutput co : centrifugeOutputs) {
				ItemStack os = lib.stringToItemStackOrFirstOreDict(co.outputString);
				if (os != null) {
					outputitems.add(os);
					outputitems.add(co.outputChance);
				}
			}
			outputitemsarray = outputitems.toArray(new Object[outputitems.size()]);
			execute();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return valid = true;
	}

	@Override
	public void execute() {
		try {
			for (ItemStack ins : in) {
				if (this.outFluid == null) {
					if (ins != null)
						addCentrifugeRecipe.invoke(RecipesCentrifuge.getRecipes(), ins, null, RecipeLevel.API, outputitemsarray);
				} else {
					if (ins != null)
						addCentrifugeRecipe.invoke(RecipesCentrifuge.getRecipes(), ins, FluidOutConstructor.newInstance(this.outFluid, this.outputfluidChance),
								RecipeLevel.API, outputitemsarray);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class CentrifugeOutput {
		private String outputString;
		private float outputChance;

		private CentrifugeOutput(String outputString, float outputChance) {
			this.outputChance = outputChance;
			this.outputString = outputString;
		}
	}

}
