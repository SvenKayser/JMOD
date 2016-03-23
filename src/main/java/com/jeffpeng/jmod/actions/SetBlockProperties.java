package com.jeffpeng.jmod.actions;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;

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

	public Float hardness;
	public Float blastresistance;
	public Map<Integer,Integer> harvestlevel = new HashMap<>();
	public String tool;
	public Integer opacity;
	public String sound;
	public Float slipperiness;
	
	
	public SetBlockProperties hardness(Float hardness){
		this.hardness = hardness;
		return this;
	}
	
	public SetBlockProperties slipperiness(Float slipperiness){
		this.slipperiness = slipperiness;
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
		this.tool = tool;
		return this;
	}
	
	public SetBlockProperties sound(String sound){
		this.sound = sound;
		return this;
		
	}
	
	public SetBlockProperties opacity(int opacity){
		this.opacity = opacity;
		return this;
		
	}
	
	public SetBlockProperties blastresistance(float blastresistance){
		this.blastresistance = blastresistance;
		return this;
	}
	
	public SetBlockProperties br(float br){
		return blastresistance(br);
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

			} else {
				log.warn("Cannot patch block properties for " + blockstring + " as it is not in the game. Omitting.");
			}
		
	}
	
}
