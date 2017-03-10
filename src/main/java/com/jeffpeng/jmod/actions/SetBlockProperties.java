package com.jeffpeng.jmod.actions;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.registry.GameRegistry;

public class SetBlockProperties extends BasicAction {
	
	private String blockstring;
	
	public SetBlockProperties(JMODRepresentation owner, String blockstring) {
		super(owner);
		this.blockstring = blockstring;
		this.valid = true;
	}

	private Float hardness;
	private Float blastresistance;
	private Map<Integer,Integer> harvestlevel = new HashMap<>();
	private Boolean opaque;
	private Float lightlevel;
	private int burnTime;
	
	
	public SetBlockProperties hardness(Float hardness){
		this.hardness = hardness;
		return this;
	}
	
	public SetBlockProperties slipperiness(Float slipperiness){
		return this;
	}
	
	public SetBlockProperties harvestlevel(int meta, int harvestlevel){
		this.harvestlevel.put(meta, harvestlevel);
		return this;
	}
	
	public SetBlockProperties hl(int meta, int hl){
		return harvestlevel(meta,hl);
	}
	
	public SetBlockProperties harvestlevel(int harvestlevel){
		this.harvestlevel.put(0, harvestlevel);
		return this;
	}
	
	public SetBlockProperties hl(int hl){
		return harvestlevel(hl);
	}
	
	public SetBlockProperties tool(String tool){
		return this;
	}
	
	public SetBlockProperties sound(String sound){
		return this;
		
	}
	
	public SetBlockProperties opaque(boolean op){
		this.opaque = op;
		return this;
	}
	
	public SetBlockProperties furnaceBurnTime(int burnTime){
		this.burnTime = 0;
		return this;
	}
	
	public SetBlockProperties blastresistance(float blastresistance){
		this.blastresistance = blastresistance;
		return this;
	}
	
	public SetBlockProperties br(float br){
		return blastresistance(br);
	}
	
	public SetBlockProperties lightlevel(float ll){
		this.lightlevel = ll;
		return this;
	}
	
	public void execute(){
		
			String[] splitname = blockstring.split(":");
			Block block = GameRegistry.findBlock(splitname[0], splitname[1]);
			if (block != null) {
				
				if (harvestlevel.size() > 0) {
					for (Map.Entry<Integer, Integer> harventry : harvestlevel.entrySet()) {
						String tool = block.getHarvestTool(harventry.getKey());
						block.setHarvestLevel(tool, harventry.getValue(), harventry.getKey());
					}
				}
				if (blastresistance != null) {
					block.setResistance(blastresistance);
				}
				
				if (hardness != null){
					block.setHardness(hardness);
				}
				
				if (opaque != null){
					block.opaque = opaque;
					block.setLightOpacity(opaque ? 255 : 0);
				}
				
				if (lightlevel != null)
				{
					block.setLightLevel(lightlevel);
				}
				
				if (burnTime != 0){
					owner.fuelHandler.setBurnTime(new ItemStack(block), burnTime);
				}

			} else {
				log.warn("Cannot patch block properties for " + blockstring + " as it is not in the game. Omitting.");
			}
		
	}
	
}
