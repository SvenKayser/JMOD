package com.jeffpeng.jmod.actions.rotarycraft;

import net.minecraft.item.ItemStack;
import Reika.RotaryCraft.API.RecipeInterface;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;


import cpw.mods.fml.common.event.FMLPostInitializationEvent;

public class AddBlastFurnaceAlloying extends BasicAction {
	
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
	
	private ItemStack[] in1;
	private ItemStack[] in2;
	private ItemStack[] in3;
	private ItemStack isout;
	private ItemStack[] ismain;
	
	
	public AddBlastFurnaceAlloying(JMODRepresentation owner, String out, String main, int temp){
		super(owner);
		this.out = out;
		this.main = main;
		this.temp = temp;
		
		this.req = -1;
		this.bonus = false;
		this.xp = 0F;
		
		
		
	}

	@Override
	public boolean on(FMLPostInitializationEvent event){
		if(!owner.testForMod("RotaryCraft")) return false;
		in1 = lib.stringToItemStackArray(in[0]);
		in2 = lib.stringToItemStackArray(in[1]);
		in3 = lib.stringToItemStackArray(in[2]);
		isout = lib.stringToItemStackOrFirstOreDict(out);
		ismain = lib.stringToItemStackArray(main);
		
		
		if(isout == null || ismain[0] == null){
			valid = false;
			return false;
		}
		valid = true;
		execute();
		return valid;
	}
	
	public AddBlastFurnaceAlloying input(int number, String in, int chance, int decrease){
		if(number > 3 || number < 1){
			log.warn("Blast Furnace inputs are only valid for a range of 1 to 3. Omitting input #" + number + " " + in + " for recipe for " + out);
			return this;
		}
		
		this.in[number - 1] = in;
		this.chance[number - 1] = chance;
		this.decrease[number - 1] = decrease;
		
		return this;
	}
	
	public AddBlastFurnaceAlloying required(int req){
		this.req = req;
		return this;
	}
	
	public AddBlastFurnaceAlloying addBonus(){
		this.bonus = true;
		return this;
	}
	
	public AddBlastFurnaceAlloying setXP(float xp){
		this.xp = xp;
		return this;		
	}
	
	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return valid;
	}
	
	@Override
	public void execute(){
		for(ItemStack iin1 : in1){
			for(ItemStack iin2 : in2){
				for(ItemStack iin3 : in3){
					for(ItemStack iismain : ismain){
						RecipeInterface.blastfurn.addAPIAlloying(iin1, chance[0], decrease[0], iin2, chance[1], decrease[1], iin3, chance[2], decrease[1], iismain, isout, req, bonus, xp, temp);
					}
				}
			}
		}
	}
}
