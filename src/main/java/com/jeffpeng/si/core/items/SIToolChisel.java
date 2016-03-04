package com.jeffpeng.si.core.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import team.chisel.client.render.item.ItemChiselRenderer;

import com.cricketcraft.chisel.api.IChiselItem;
import com.cricketcraft.chisel.api.carving.ICarvingVariation;
import com.jeffpeng.si.core.SI;
import com.jeffpeng.si.core.interfaces.ISITool;
import com.jeffpeng.si.core.util.descriptors.ToolDataDescriptor;


public class SIToolChisel extends SICoreItem implements IChiselItem, ISITool{
	
	
	private ToolMaterial toolmat;
	private boolean hasModes;
	private boolean unbreakable = false;
	private int durability = 0;

	public SIToolChisel(ToolDataDescriptor desc){
		
		if(desc == null)
		{
			throw new RuntimeException("[si.core] Cannot define a chisel without a proper tool material.");
		}
		
		this.toolmat = ToolMaterial.valueOf(desc.toolmat);
		this.hasModes = desc.hasmodes;
		this.unbreakable = desc.unbreakable || (toolmat.getMaxUses() == 0);  
		if(desc.durability != null) durability = desc.durability; else durability = toolmat.getMaxUses();
		
		
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public boolean canOpenGui(World world, EntityPlayer player, ItemStack chisel){
		return true;
	}
	
	public boolean onChisel(World world, ItemStack chisel, ICarvingVariation target){
		return true;
	}

	
	public boolean canChisel(World world, ItemStack chisel, ICarvingVariation target){
		return true;
	}

	
	public boolean canChiselBlock(World world, EntityPlayer player, int x, int y, int z, Block block, int metadata){
		return true;
	}

	public boolean hasModes(ItemStack chisel){
		return this.hasModes;
	}
	
	@Override
	public boolean isDamageable(){
		return !unbreakable;
	}
	
	@Override
	public int getMaxDamage(ItemStack stack){
		return this.durability;
	}
	
	public ItemStack getRepairItemStack(){
		return this.toolmat.getRepairItemStack();
	}
	
	@Override
	public boolean getIsRepairable(ItemStack damagedItem, ItemStack repairMaterial)
	{
		return repairMaterial.equals(this.toolmat.getRepairItemStack());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean held) {
		if(hasModes) list.add(StatCollector.translateToLocal("info.si.core.tooltops.chiselhasmodes"));
		else list.add(StatCollector.translateToLocal("info.si.core.tooltops.chiselnomodes")); 
	}
	
	@Override
	public void register(){
		super.register();
		if(!SI.isServer) MinecraftForgeClient.registerItemRenderer(this, new ItemChiselRenderer());
	}
	
	@Override
	public boolean isFull3D() {
		return true;
	}
	
}
