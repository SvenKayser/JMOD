package com.jeffpeng.jmod.types.items;

import iguanaman.hungeroverhaul.config.IguanaConfig;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.oredict.OreDictionary;

import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.interfaces.IItem;


import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ItemWoodenBucket extends CoreItem implements IItem {
	private static ItemWoodenBucket emptyBucket;
	private static ItemWoodenBucket waterBucket;
	private static ItemWoodenBucket milkBucket;
	
	private String content;
	private String internalName;


	public ItemWoodenBucket(JMODRepresentation owner, String content) {
		super(owner);
		this.content = content;
	}
	
	public ItemWoodenBucket(JMODRepresentation owner) {
		super(owner);
		this.content = "empty";
		
		emptyBucket = this;
		waterBucket = new ItemWoodenBucket(owner,"water");
		milkBucket = new ItemWoodenBucket(owner,"milk");
		
		waterBucket.setContainerItem(emptyBucket).setMaxStackSize(1);
		milkBucket.setContainerItem(emptyBucket).setMaxStackSize(1);
		
		MinecraftForge.EVENT_BUS.register(this);
		
		
	}
	
	@SubscribeEvent
	public void milkCow(EntityInteractEvent event){
		EntityPlayer ep = event.entityPlayer;
		ItemStack is = event.entityPlayer.getCurrentEquippedItem();
		if(event.target instanceof EntityCow && is != null && is.getItem() == emptyBucket){
			
			
			
			if(Loader.isModLoaded("HungerOverhaul")){
				NBTTagCompound tags = event.target.getEntityData();
				if (tags.hasKey("Milked"))
				{
					event.setCanceled(true);
					if (!ep.worldObj.isRemote)
					event.target.playSound("mob.cow.hurt",  0.4F, 0.9F);
					return;
				} else
					tags.setInteger("Milked", IguanaConfig.milkedTimeout * 60);
			}
				
				
			ep.getCurrentEquippedItem().stackSize--;
			if (!ep.inventory.addItemStackToInventory(new ItemStack(ItemWoodenBucket.milkBucket)))
            {
                ep.dropPlayerItemWithRandomChoice(new ItemStack(ItemWoodenBucket.milkBucket, 1, 0), false);
            }
		}
	}
	
	@Override
	public Item setTextureName(String texname){
		super.setTextureName(texname);
		if(this.content=="empty"){
			waterBucket.setTextureName(texname+"Water");
			milkBucket.setTextureName(texname+"Milk");
		}
		return this;
	}
	
	@Override
    public String getUnlocalizedName(ItemStack is)
    {
        return "item." + getPrefix()+"."+internalName+"."+this.content;
    }
	
	@Override
	public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer ep)
    {
        boolean empty = this.content == "empty";
        MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, ep, empty);
        
        if(mop == null) return is;
        int cx = mop.blockX;
    	int cy = mop.blockY;
    	int cz = mop.blockZ;
        if(content == "empty"){
        	if(mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK){
            	if(world.getBlock(cx, cy, cz) == Blocks.water){
            		world.setBlockToAir(cx, cy, cz);
            		if(--is.stackSize <= 0){
            			return new ItemStack(waterBucket);
            		} else {
            			if (!ep.inventory.addItemStackToInventory(new ItemStack(waterBucket)))
                        {
                            ep.dropPlayerItemWithRandomChoice(new ItemStack(waterBucket, 1, 0), false);
                        }
            		}
            	} else{
            		TileEntity te = world.getTileEntity(cx,cy,cz);
            		if(te != null && te instanceof IFluidHandler){
            			
            		} 
            	}
            }
        } else if(content == "water"){
        	
        	
        	TileEntity te = world.getTileEntity(cx,cy,cz);
        	if(te != null && te instanceof IFluidHandler){
        		((IFluidHandler)te).canFill(ForgeDirection.getOrientation(mop.sideHit), FluidRegistry.WATER);
        	} else {
        		switch(mop.sideHit){
            		case 0: cy--; break;
            		case 1: cy++; break;
            		case 2: cz--; break;
            		case 3: cz++; break;
            		case 4: cx--; break;
            		case 5: cx++; break;
            	}
        		
        		if (world.provider.isHellWorld)
                {
                    world.playSoundEffect((double)((float)cx + 0.5F), (double)((float)cy + 0.5F), (double)((float)cz + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

                    for (int l = 0; l < 8; ++l)
                    {
                        world.spawnParticle("largesmoke", (double)cx + Math.random(), (double)cy+ Math.random(), (double)cz + Math.random(), 0.0D, 0.0D, 0.0D);
                    }
                }
        		
        		else if(true){
        			world.setBlock(cx,cy,cz,Blocks.flowing_water,0,3);
        		}
            	return new ItemStack(emptyBucket);
        	}
        }
   
        return is;
    }
	@Override
	public void setName(String name) {
		this.internalName = name;
		waterBucket.internalName = name;
		milkBucket.internalName = name;
	}

	@Override
	public void register() {
		JMOD.DEEPFORGE.registerItem(emptyBucket, this.internalName, owner);
		JMOD.DEEPFORGE.registerItem(waterBucket, this.internalName+"_water", owner);
		JMOD.DEEPFORGE.registerItem(milkBucket, this.internalName+"_milk", owner);
		OreDictionary.registerOre("listAllmilk", milkBucket);
		OreDictionary.registerOre("listAllwater", waterBucket);
		FluidContainerRegistry.registerFluidContainer(FluidRegistry.WATER, new ItemStack(waterBucket), new ItemStack (emptyBucket));
	}

}
