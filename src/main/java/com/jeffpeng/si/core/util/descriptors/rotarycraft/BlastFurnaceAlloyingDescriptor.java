package com.jeffpeng.si.core.util.descriptors.rotarycraft;

import net.minecraft.item.ItemStack;
import Reika.RotaryCraft.API.RecipeInterface;

import com.jeffpeng.si.core.SILib;
import com.jeffpeng.si.core.interfaces.ISIStagedObject;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

public class BlastFurnaceAlloyingDescriptor implements ISIStagedObject {
	
	private boolean valid;
	private int temp;
	private float xp;
	private boolean bonus;
	private int req;
	private String out;
	private String main;
	private String[] in = new String[3];
	private int[] chance = new int[3];
	private int[] decrease = new int[3];
	
	public BlastFurnaceAlloyingDescriptor(String out, String main, int temp){
		this.out = out;
		this.main = main;
		this.temp = temp;
		
		this.req = -1;
		this.bonus = false;
		this.xp = 0F;
		
		
		
	}

	@Override
	public boolean on(FMLPostInitializationEvent event){
		ItemStack in1 = SILib.stringToItemStackNoOreDict(in[0]);
		ItemStack in2 = SILib.stringToItemStackNoOreDict(in[1]);
		ItemStack in3 = SILib.stringToItemStackNoOreDict(in[2]);
		ItemStack isout = SILib.stringToItemStackNoOreDict(out);
		ItemStack ismain = SILib.stringToItemStackNoOreDict(main);
		
		
		if(isout == null || ismain == null){
			valid = false;
			return false;
		}
		
		valid = true;
		
		RecipeInterface.blastfurn.addAPIAlloying(in1, chance[0], decrease[0], in2, chance[1], decrease[1], in3, chance[2], decrease[1], ismain, isout, req, bonus, xp, temp);
		
		return valid;
	}
	
	public BlastFurnaceAlloyingDescriptor input(int number, String in, int chance, int decrease){
		if(number > 3 || number < 1){
			FMLLog.warning("Blast Furnace inputs are only valid for a range of 1 to 3. Omitting input #" + number + " " + in + " for recipe for " + out);
			return this;
		}
		
		this.in[number - 1] = in;
		this.chance[number - 1] = chance;
		this.decrease[number - 1] = decrease;
		
		return this;
	}
	
	public BlastFurnaceAlloyingDescriptor required(int req){
		this.req = req;
		return this;
	}
	
	public BlastFurnaceAlloyingDescriptor addBonus(){
		this.bonus = true;
		return this;
	}
	
	public BlastFurnaceAlloyingDescriptor setXP(float xp){
		this.xp = xp;
		return this;		
	}
	
	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
