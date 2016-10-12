package com.jeffpeng.jmod.types.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidClassic;

import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;
import com.jeffpeng.jmod.interfaces.IBlock;
import com.jeffpeng.jmod.registry.BuffRegistry;
import com.jeffpeng.jmod.types.blocks.placers.CoreBlockPlacer;
import com.jeffpeng.jmod.types.fluids.CoreFluid;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CoreFluidBlock extends BlockFluidClassic implements IBlock {

	private CoreFluid attachedFluid;
	@SideOnly(Side.CLIENT)
	private IIcon[] icons = new IIcon[2];
	private String internalName;
	private JMODRepresentation owner;
	private int setFireChance = 0;
	private int freezeChance = 0;
	
	private boolean searing;
	private boolean boiling;
	private boolean freezing;
	private boolean cold;
	private boolean hypothermic;
	private boolean supercold;
	private boolean poisonous;
	private int temperature;
	
	private int corrosiveness;
	
	// caching status effects
	private static final DamageSource coldDamage = new DamageSource("cold").setDamageBypassesArmor();
	private static final DamageSource acidDamage = new DamageSource("acid").setDamageBypassesArmor();
	

	

	public CoreFluidBlock(JMODRepresentation owner, Material mat, CoreFluid attachedFluid) {
		super(attachedFluid, mat);
		if(attachedFluid.getDensity()<0) this.densityDir = 1;
		this.owner = owner;
		this.attachedFluid = attachedFluid;
		this.internalName = "fluid." + attachedFluid.getName();
		this.setBlockName(owner.getModId() + "." + internalName);
		this.setBlockTextureName(owner.getModId() + ":" + internalName);
		JMOD.LOG.info("cfb tex "+this.textureName);
		searing = attachedFluid.isSearing();
		boiling = attachedFluid.isBoiling();
		freezing = attachedFluid.isFreezing();
		cold = attachedFluid.getTemperature() <= (10+273);
		hypothermic = attachedFluid.getTemperature() <= (-50+273);
		supercold = attachedFluid.getTemperature() <= (-100+273);
		temperature = attachedFluid.getTemperature();
		if(searing)
			if(attachedFluid.getTemperature() > CoreFluid.SEARING+1000) setFireChance = 100;
			else setFireChance = (CoreFluid.SEARING+1000 - attachedFluid.getTemperature()) / 10;
		
		if(freezing)
			if(attachedFluid.getTemperature() < CoreFluid.FREEZING-100) freezeChance = 100;
			else freezeChance = (attachedFluid.getTemperature() - CoreFluid.FREEZING-100);
		
		poisonous = attachedFluid.isPoisonous();
		
		register();

	}

	@Override
	public IIcon getIcon(int side, int meta) {
		if (side == 1)
			return this.icons[0];
		else
			return this.icons[1];
	}

	public IIcon getStillIcon() {
		return this.icons[0];
	}

	public IIcon getFlowingIcon() {
		return this.icons[1];
	}

	@Override
	public void registerBlockIcons(IIconRegister iconregister) {
		this.icons[0] = iconregister.registerIcon(textureName + "-still");
		this.icons[1] = iconregister.registerIcon(textureName + "-flowing");
		attachedFluid.setIcons(this.icons[0], this.icons[1]);

	}

	@Override
	public String getUnlocalizedName() {
		return "fluid." + owner.getModId() + "." + this.attachedFluid.getName();
	}

	@Override
	public String getLocalizedName() {
		return StatCollector.translateToLocal(getUnlocalizedName());

	}

	@Override
	public String getName() {
		return internalName;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {

		super.updateTick(world, x, y, z, rand);
		if (!world.isRemote) {
			if (setFireChance > 0) {
				int face = 0;
				ForgeDirection direction = ForgeDirection.getOrientation(face = rand.nextInt(6));
				Block targetBlock = world.getBlock(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);

				if (targetBlock != Blocks.air && targetBlock.isFlammable(world, x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ, direction)) {
					if(Lib.chance(setFireChance)) world.setBlock(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ, Blocks.fire, 0,3);
				}
			}
		}
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, @SuppressWarnings("rawtypes") List p_149666_3_){}

	@Override
	public void register() {
		owner.registerBlock(this, new CoreBlockPlacer(owner, this));
	}

	@Override
	public JMODRepresentation getOwner() {
		return owner;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		if(world.isRemote) return;
		if(searing){
			entity.setFire(10);
		} else if(boiling && entity instanceof EntityLivingBase){
			entity.attackEntityFrom(DamageSource.lava, 1F);
		} else if(supercold && entity instanceof EntityLivingBase){
			((EntityLivingBase)entity).addPotionEffect(new PotionEffect(BuffRegistry.getBuff("moveslowdown").id,60*20,4));
			if(Lib.chance(25)) entity.attackEntityFrom(coldDamage, 2F);
		} else if(hypothermic && entity instanceof EntityLivingBase){
			((EntityLivingBase)entity).addPotionEffect(new PotionEffect(BuffRegistry.getBuff("moveslowdown").id,20*20,2));
			if(Lib.chance(15)) entity.attackEntityFrom(coldDamage, 1F);
		} else if(freezing && entity instanceof EntityLivingBase){
			((EntityLivingBase)entity).addPotionEffect(new PotionEffect(BuffRegistry.getBuff("moveslowdown").id,5*20,1));
		} else if(cold && entity instanceof EntityLivingBase) {
			((EntityLivingBase)entity).addPotionEffect(new PotionEffect(BuffRegistry.getBuff("moveslowdown").id,1*20,0));
		}
		
		if(corrosiveness>0){
			if(Lib.chance(corrosiveness)) entity.attackEntityFrom(acidDamage, 1F);
		}
		
		if(poisonous && entity instanceof EntityLivingBase){
			((EntityLivingBase)entity).addPotionEffect(new PotionEffect(BuffRegistry.getBuff("poison").id,10*20,1));
		}
	}
}
