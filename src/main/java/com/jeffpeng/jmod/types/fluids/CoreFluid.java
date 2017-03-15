package com.jeffpeng.jmod.types.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.types.blocks.CoreFluidBlock;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class CoreFluid extends Fluid {
	
	public static final int FREEZING = 273;
	public static final int BOILING = 473;
	public static final int SEARING = 773;

	private JMODRepresentation owner;
	private String fluidname;
	private int color;
	private CoreFluidBlock fluidBlock;
	private ItemBucket bucket = null; 
	private boolean poisonous;
	
	public CoreFluid(JMODRepresentation owner, String fluidname) {
		super(fluidname);
		this.fluidname = fluidname;
		this.owner = owner;
		this.canBePlacedInWorld();
	}

	public void setColor(int r, int g, int b) {
		color = (r << 16) + (g << 8) + b;
	}

	@Override
	public String getLocalizedName(FluidStack fs) {
		return fluidBlock.getLocalizedName();
	}
	
	@Override
	public String getLocalizedName() {
		return fluidBlock.getLocalizedName();
	}
	
	@Override
	public String getUnlocalizedName(FluidStack fs) {
		return fluidBlock.getUnlocalizedName();
	}
	
	@Override
	public String getUnlocalizedName() {
		return fluidBlock.getUnlocalizedName();
	}

	@Override
	public final int getColor() {
		return color;
	}

	public void register() {
		FluidRegistry.registerFluid(this);
		if(this.isGaseous) fluidBlock = new CoreFluidBlock(owner, new MaterialLiquid(MapColor.airColor), this); else
		if(this.temperature>SEARING) fluidBlock = new CoreFluidBlock(owner, Material.lava, this); else 
		fluidBlock = new CoreFluidBlock(owner, Material.water, this);
		
	}
	
	public void bucketize(){
		bucket = new ItemBucket(fluidBlock);
		bucket.setUnlocalizedName("bucket."+fluidname.toLowerCase()).setContainerItem(Items.bucket);
		bucket.setTextureName(owner.getModId()+":bucket."+fluidname.toLowerCase());
		
		JMOD.DEEPFORGE.registerItem(bucket, "bucket."+fluidname.toLowerCase(), owner);
		FluidContainerRegistry.registerFluidContainer(this, new ItemStack(bucket), new ItemStack(Items.bucket));
		MinecraftForge.EVENT_BUS.register(this);
	}

	public int getTickRate(){
		return viscosity/200;
	}
	
	
	@SubscribeEvent
	public void onBucketFill(FillBucketEvent event){
		World world = event.world;
		MovingObjectPosition position = event.target;
		
		Block block = world.getBlock(position.blockX, position.blockY, position.blockZ);
		
		if (block.equals(fluidBlock) && event.entityPlayer.getCurrentEquippedItem().getItem() == Items.bucket)
		{
			world.setBlockToAir(position.blockX, position.blockY, position.blockZ);
			event.result = new ItemStack(bucket);
			event.setResult(Result.ALLOW);
		}
	}
	
	public boolean isFreezing(){
		return this.temperature <= FREEZING;
	}
	
	public boolean isBoiling(){
		return this.temperature >= BOILING;
	}
	
	public boolean isSearing(){
		return this.temperature >= SEARING;
	}
	
	public void setPoisonous(boolean val){
		poisonous = val;
	}
	
	public boolean isPoisonous(){
		return poisonous;
	}
	
	
	
	
}
