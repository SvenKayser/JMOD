package com.jeffpeng.si.core.modintegration.harvestcraft;

//import java.lang.reflect.Field;
//
//import net.minecraft.item.ItemStack;
//
////import com.pam.harvestcraft.ItemRegistry;
////import com.pam.harvestcraft.PresserRecipes;
////import com.pam.harvestcraft.TileEntityPamPresser;
//
//import cpw.mods.fml.common.FMLLog;

//public class TileEntityPamPresserSI extends TileEntityPamPresser {
//	public ItemStack[] presserStacks;
//	
//	public TileEntityPamPresserSI(){
//		super();
//		
//		
//		
//		Field presserStacksfield;
//		try {
//			presserStacksfield = getClass().getSuperclass().getDeclaredField("combPresserItemStacks");
//			presserStacksfield.setAccessible(true);
//			presserStacks = (ItemStack[]) presserStacksfield.get(this);
//		} catch (NoSuchFieldException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//		
//		FMLLog.info("SI PAM PRESSER");
//	}
//	
//	public String getInvName() {
//         return "Presser";
//    }
//	
//	public boolean processingSoy;
//	
//	@Override
//	public void updateEntity() {
//		processingSoy = (presserStacks[0] != null && presserStacks[0].getItem() == ItemRegistry.soybeanItem);
//		FMLLog.info("update");
//		if(presserStacks[0] != null) FMLLog.info(presserStacks[0].getItem().getUnlocalizedName()); else FMLLog.info("no input");
//		if(processingSoy) FMLLog.info("it's soy!");
//		boolean needsUpdate = false;
//
//		if (!super.worldObj.isRemote) {
//
//			if (this.canRun()) {
//				++this.combPresserCookTime;
//				if (this.combPresserCookTime >= 125) {
//					this.combPresserCookTime = 0;
//					this.pressComb();
//					needsUpdate = true;
//				}
//			} else {
//				this.combPresserCookTime = 0;
//			}
//
//			if (needsUpdate != this.combPresserCookTime > 0) {
//				needsUpdate = true;
//			}
//		}
//
//		if (needsUpdate) {
//			this.markDirty();
//			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
//		}
//
//	}
//	
//	
//	private boolean canRun()
//    {
//		FMLLog.info("can run");
//        if (presserStacks[0] == null)
//        {
//            return false;
//        }
//        else
//        {
//            ItemStack[] results = PresserRecipes.pressing().getPressingResult(presserStacks[0]);
//            if (results == null) return false;
//            if (presserStacks[1] != null) {
//                if (!presserStacks[1].isItemEqual(results[0])) return false;
//                if (presserStacks[1].stackSize + results[0].stackSize >= presserStacks[1].getMaxStackSize()) return false; 
//            }
//            if (results[1] != null) {
//                if (presserStacks[2] != null) {
//                    if (!presserStacks[2].isItemEqual(results[1])) return false;
//                    if (presserStacks[2].stackSize + results[1].stackSize >= presserStacks[2].getMaxStackSize()) return false; 
//                }
//            }
//            return true;
//        }
//    }
//	
//	@Override
//	public void pressComb()
//    {
//        if (this.canRun())
//        {
//            ItemStack[] results = PresserRecipes.pressing().getPressingResult(presserStacks[0]);
//
//            if (presserStacks[1] == null)
//            {
//                presserStacks[1] = results[0].copy();
//            }
//            else if (presserStacks[1].getItem() == results[0].getItem())
//            {
//            	presserStacks[1].stackSize += results[0].stackSize; // Forge BugFix: Results may have multiple items
//            }
//
//            if (results[1] != null) {
//                if (presserStacks[2] == null)
//                {
//                	presserStacks[2] = results[1].copy();
//                }
//                else if (presserStacks[2].isItemEqual(results[1]))
//                {
//                	presserStacks[2].stackSize += results[1].stackSize;
//                }
//            }
//            
//            if(!processingSoy)	--this.presserStacks[0].stackSize; else this.presserStacks[0].stackSize -= 2;
//
//            if (presserStacks[0].stackSize <= 0)
//            {
//            	presserStacks[0] = null;
//            }
//        }
//    }
//	
//}
