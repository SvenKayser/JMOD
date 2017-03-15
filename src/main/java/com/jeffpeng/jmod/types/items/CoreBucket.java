package com.jeffpeng.jmod.types.items;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.Lib;
import com.jeffpeng.jmod.forgeevents.JMODRegisterFilledCoreBucketEvent;
import com.jeffpeng.jmod.interfaces.IItem;
import com.jeffpeng.jmod.primitives.BasicAction;

import cpw.mods.fml.common.event.FMLEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class CoreBucket extends ItemBucket implements IItem{
	
	private JMODRepresentation owner;
	private String internalName;
	private int burnTime = 0;
	private boolean fluidListMode = false;
	private Map<String,String> fluidList;
	protected Map<Integer,String> containingFluids = new HashMap<>();
	protected Map<String, Integer> containingFluidsRev = new HashMap<>();
	protected Map<Integer, CoreBucket> children = new HashMap<>();
	private int fluidsCounter = 0;
	public IIcon[] icons;
	BasicAction settings;
	private boolean fluidsAssembled = false;
	private int bucketsize;
	private CoreBucket refBucket;
	private int fluidid = 0;
	private boolean isChild = false;
	private static boolean eventInitialized = false;
	
	public CoreBucket(JMODRepresentation owner) {
		super(Blocks.air);
		this.owner = owner;
		this.refBucket = this;
		this.bucketsize = 1000;
		if(!eventInitialized){
			MinecraftForge.EVENT_BUS.register(new FluidHandler());
			eventInitialized = true;
		}
	}
	
	public CoreBucket(JMODRepresentation owner,CoreBucket parentBucket, Block fluidblock, int fluidid){
		super(fluidblock);
		this.isChild = true;
		this.owner = owner;
		this.refBucket = parentBucket;
		this.setContainerItem(parentBucket);
		this.setMaxStackSize(1);
		this.fluidid = fluidid;
		this.bucketsize = parentBucket.bucketsize;

	}
	
	@Override
	public JMODRepresentation getOwner() {
		return owner;
	}

	@Override
	public void setName(String name) {
		internalName = name;
		
	}
	
	@Override
	public String getName(){
		return this.internalName;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void processSettings(BasicAction settings) {
		this.settings = settings;
		if(settings.hasSetting("burntime"))			this.burnTime	 	= settings.getInt("burntime");
		if(settings.hasSetting("fluidlist"))    	this.fluidList 		= (Map<String, String>) settings.getObject("fluidlist");
		if(settings.hasSetting("fluidlistmode"))    this.fluidListMode 	= settings.getBoolean("fluidlistmode");
		if(settings.hasSetting("size"))				this.bucketsize		= settings.getInt("size");
		if(settings.hasSetting("remainsincraftinggrid")) this.containerItemSticksInCraftingGrid = settings.getBoolean("remainsincraftinggrid");
	}
	
	private boolean containerItemSticksInCraftingGrid = false;
	
	@Override
	public boolean doesContainerItemLeaveCraftingGrid(ItemStack is)
    {
        return !containerItemSticksInCraftingGrid;
    }
	
	@Override
	public void on(FMLEvent event){
		if(event instanceof FMLInitializationEvent) assembleFluids();
		if(event instanceof FMLLoadCompleteEvent) setCTabs();
	}
	
	private void setCTabs(){
		if(!JMOD.isServer() && settings.hasSetting("tab") && settings.getString("tab") != null){
			CreativeTabs tabInstance = Lib.getCreativeTabByName(settings.getString("tab"));
			if(tabInstance != null)	for(Map.Entry<Integer, CoreBucket> entry : children.entrySet()) entry.getValue().setCreativeTab(tabInstance);
		}
	}
	
	private void assembleFluids(){
		if(fluidsAssembled) return;
		
		if(this.fluidList == null) this.fluidList = new HashMap<>();
		for(Map.Entry<String, Fluid> entry : FluidRegistry.getRegisteredFluids().entrySet()){
			if(fluidList.containsValue(entry.getKey()) ^ this.fluidListMode){
				containingFluids.put(++fluidsCounter, entry.getKey());
				containingFluidsRev.put(entry.getKey(), fluidsCounter);
				CoreBucket newbucket = new CoreBucket(owner,this,entry.getValue().getBlock(),fluidsCounter);
				newbucket.setName(this.getName()+"."+( entry.getKey().replace(" ", "_") ));
				newbucket.setTextureName(getPrefix() + ":" + getName() + "_"+( entry.getKey().replace(" ", "_") ));
				newbucket.register();
				children.put(fluidsCounter,newbucket);
				MinecraftForge.EVENT_BUS.post(new JMODRegisterFilledCoreBucketEvent(this,newbucket,entry.getValue(),this.bucketsize));
			}
		}
		
		for(Map.Entry<String, String> entry : this.fluidList.entrySet()){
			if(!FluidRegistry.getRegisteredFluids().containsKey(entry.getValue())){
				containingFluids.put(++fluidsCounter, entry.getValue());
				containingFluidsRev.put(entry.getValue(), fluidsCounter);
				CoreBucket newbucket = new CoreBucket(owner,this,null,fluidsCounter);
				newbucket.setName(this.getName()+"."+( entry.getValue().replace(" ", "_") ));
				newbucket.setTextureName(getPrefix() + ":" + getName() + "_"+( entry.getValue().replace(" ", "_") ));
				newbucket.register();
				
				children.put(fluidsCounter,newbucket);
			}
		}
		
		fluidsAssembled = true;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "item." + getPrefix() + "." + getName();
	}
	
	@Override 
	public int getBurnTime(){
		return this.burnTime;
	}
	
	@Override
	public void setBurnTime(int bt){
		this.burnTime = bt;
	}
	
	private boolean isChild(){
		return isChild;
	}
	
	private ItemStack givePlayerBucketOf(String fluidname,EntityPlayer ep, ItemStack is){
		if (--is.stackSize <= 0) return getBucketOf(fluidname);
        else
        {
        	ItemStack bucketstack = getBucketOf(fluidname);
            if (!ep.inventory.addItemStackToInventory(bucketstack)) ep.dropPlayerItemWithRandomChoice(bucketstack, false);
            return is;
        }
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer ep)
    {
		if(this.bucketsize != 1000) return is;
        boolean isEmpty = !isChild();
        MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, ep, isEmpty);
        if (mop == null) return is;
        else{
            FillBucketEvent event = new FillBucketEvent(ep, is, world, mop);
            if (MinecraftForge.EVENT_BUS.post(event))	return is;
            if (event.getResult() == Event.Result.ALLOW){
                if (ep.capabilities.isCreativeMode) return is;
                if (--is.stackSize <= 0) return event.result;
                if (!ep.inventory.addItemStackToInventory(event.result))	ep.dropPlayerItemWithRandomChoice(event.result, false);
                return is;
            }
            if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK){
                int bx = mop.blockX;
                int by = mop.blockY;
                int bz = mop.blockZ;

                if (!world.canMineBlock(ep, bx, by, bz)) return is;

                if (isEmpty){
                    if (!ep.canPlayerEdit(bx, by, bz, mop.sideHit, is)) return is;
                    Block block = world.getBlock(bx,by,bz);
                    if(block == Blocks.flowing_water) block = Blocks.water;
                	if(block == Blocks.flowing_lava) block = Blocks.lava;
                    if(world.getBlockMetadata(bx, by, bz) == 0) for(Map.Entry<String, Fluid> entry : FluidRegistry.getRegisteredFluids().entrySet()){
                    	if(refBucket.containingFluidsRev.containsKey(entry.getKey())){
                    		Fluid fluid = entry.getValue();
                        	Block fluidblock = fluid.getBlock();
                        	if(fluidblock != null && fluidblock == block){
                        		world.setBlockToAir(bx, by, bz);
                        		if (ep.capabilities.isCreativeMode) return is;
                                else return givePlayerBucketOf(entry.getKey(),ep,is);
                        	}
                    	}
                    }
                }
                else{
                    if (mop.sideHit == 0) --by;
                    if (mop.sideHit == 1) ++by;
                    if (mop.sideHit == 2) --bz;
                    if (mop.sideHit == 3) ++bz;
                    if (mop.sideHit == 4) --bx;
                    if (mop.sideHit == 5) ++bx;

                    if (!ep.canPlayerEdit(bx, by, bz, mop.sideHit, is)) return is;
                    if (this.tryPlaceContainedLiquid(world, bx, by, bz) && !ep.capabilities.isCreativeMode) return new ItemStack(refBucket,1,0);
                }
            }
            return is;
        }
    }
	
	public String getFluidName(){
		if(fluidid >0) return refBucket.containingFluids.get(fluidid);
		return "empty";
	}
	
	public ItemStack getBucketOf(String fluidname){
		Integer fluidid = refBucket.containingFluidsRev.get(fluidname);
		return getBucketOf(fluidid);
	}
	
	public ItemStack getBucketOf(Integer fluidid){
		if(fluidid != null) return new ItemStack(refBucket.children.get(fluidid));
		return null;
	}
	
	public boolean hasBucketsOf(String fluidname){
		return refBucket.containingFluidsRev.containsKey(fluidname);
	}
	
	@Override
	public boolean tryPlaceContainedLiquid(World world, int x, int y, int z)
    {
		if(!isChild()) return false;
		Fluid fluid = FluidRegistry.getRegisteredFluids().get(refBucket.containingFluids.get(this.fluidid));
        if (fluid == null || fluidid == 0) return false;
        else
        {
            Material material = world.getBlock(x, y, z).getMaterial();
            boolean isSolid = !material.isSolid();

            if (!world.isAirBlock(x, y, z) && !isSolid) return false;
            else {
            	Block block = fluid.getBlock();
            	if(block == Blocks.water) block = Blocks.flowing_water;
            	if(block == Blocks.lava) block = Blocks.flowing_lava;
            	
                if (world.provider.isHellWorld &&  block == Blocks.water ) {
                    world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
                    for (int l = 0; l < 8; ++l) world.spawnParticle("largesmoke", (double)x + Math.random(), (double)y + Math.random(), (double)z + Math.random(), 0.0D, 0.0D, 0.0D);
                    
                } else {
                    if (!world.isRemote && isSolid && !material.isLiquid()) world.func_147480_a(x, y, z, true);
                    world.setBlock(x, y, z, block, 0, 3);
                }

                return true;
            }
        }
    }
	
	public class FluidHandler {
	
		@SubscribeEvent(priority=EventPriority.HIGH)
		public void preventGUI(PlayerOpenContainerEvent e)
		{
		}
		
		@SubscribeEvent(priority=EventPriority.HIGH)
		public void handleIFluidEntity(PlayerInteractEvent e){
			
			
			
			if(e.action != Action.RIGHT_CLICK_BLOCK) return;
			if(e.world.isRemote && e.entityPlayer.getCurrentEquippedItem() != null && e.entityPlayer.getCurrentEquippedItem().getItem() instanceof CoreBucket){
				EntityPlayer ep = e.entityPlayer;
				ItemStack is = ep.getCurrentEquippedItem();
				if(is == null) return;
				//e.setCanceled(true);
				//Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(e.x, e.y, e.z, e.face, is, 0,0,0));
				return;
			}
			EntityPlayer ep = e.entityPlayer;
			ItemStack is = ep.getCurrentEquippedItem();
			if(is == null) return;
			if(!(is.getItem() instanceof CoreBucket)) return;
			TileEntity te = e.world.getTileEntity(e.x, e.y, e.z);
			if(te == null || !(te instanceof IFluidHandler)) return;
			CoreBucket bucket = (CoreBucket)is.getItem();
			ForgeDirection side = Lib.getForgeDirectionFromMOPSide(e.face);
			if(side == null) return;
			IFluidHandler fh = (IFluidHandler)te;
			if(!bucket.isChild()){
				FluidStack testDrain = fh.drain(side, bucket.bucketsize, false);
				if(testDrain == null) return;
				Fluid testFluid = testDrain.getFluid();
				if(testFluid == null) return;
				String fluidname = testFluid.getName();
				if(testDrain.amount == bucket.bucketsize && bucket.refBucket.hasBucketsOf(fluidname)){
					fh.drain(side,bucket.bucketsize,true);
					ep.setCurrentItemOrArmor(0, bucket.givePlayerBucketOf(fluidname,ep,is));
					e.setCanceled(true);
				}
			} else {
				Fluid myFluid =  FluidRegistry.getFluid(bucket.refBucket.containingFluids.get(bucket.fluidid));
				if(myFluid == null) return;
				if(!fh.canFill(side, myFluid)) return;
				FluidStack refStack = new FluidStack(myFluid,bucket.bucketsize);
				int testFill = fh.fill(side, refStack, false);
				if(testFill == bucket.bucketsize){
					fh.fill(side, refStack, true);
					ep.setCurrentItemOrArmor(0, new ItemStack(bucket.refBucket,1,0));
					e.setCanceled(true);
				}
			}
			return;
		}
	}
}
