package com.jeffpeng.si.core.asm;

import java.util.Iterator;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class SIClassTransformer implements IClassTransformer{

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if(name.equalsIgnoreCase(SIObfuscationHelper.get("net.minecraft.inventory.ContainerRepair$2"))) return patchContainerRepair2(basicClass);
		//if(false && name.equalsIgnoreCase("com.pam.harvestcraft.BlockPamPresser")) return patchPamPresser(basicClass);
		//if(false && name.equalsIgnoreCase("com.pam.harvestcraft.harvestcraft")) return patchPamHarvestcraft(basicClass);
		if(name.equalsIgnoreCase("sync.common.Sync")) return patchSync(basicClass);
		return basicClass;
	}
	
//	private byte[] patchPamHarvestcraft(byte[] basicClass){
//		String method = "onPreInit";
//		ClassNode cN = new ClassNode();
//		ClassReader cR = new ClassReader(basicClass);
//		cR.accept(cN, 0);
//		AbstractInsnNode iNode;
//		Iterator<MethodNode> methods = cN.methods.iterator();
//
//		while(methods.hasNext()){
//			
//			MethodNode mN = methods.next();
//			System.out.println(mN.name);
//			if(mN.name.equals(method)){
//				
//				Iterator<AbstractInsnNode> iIterator = mN.instructions.iterator();
//				while(iIterator.hasNext()){
//					iNode = iIterator.next();
//					if(iNode.getOpcode() == Opcodes.LDC){
//						LdcInsnNode lDCNode = (LdcInsnNode) iNode;
//						System.out.println("ttti");
//						if(lDCNode.cst instanceof Type){
//							System.out.println(((Type)lDCNode.cst).getInternalName());
//							if(((Type)lDCNode.cst).getInternalName().equals("com/pam/harvestcraft/TileEntityPamPresser")){
//								lDCNode.cst = Type.getObjectType("com/jeffpeng/si/core/modintegration/harvestcraft/TileEntityPamPresserSI");
//								System.out.println("ttto");
//							}
//						}
//						
//					}
//				}
//			}
//		}
//		ClassWriter cW = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
//		cN.accept(cW);
//		System.out.println("[si.core.plugin] [SIClassTransformer] com.pam.harvestcraft.harvestcraft");
//		System.out.println("[si.core.plugin] [SIClassTransformer] This forces harvestcraft to use SI's modified presser TE");
//		
//		return cW.toByteArray();
//	}
//	
//	private byte[] patchPamPresser(byte[] basicClass){
//		String method = "createNewTileEntity";
//		ClassNode cN = new ClassNode();
//		ClassReader cR = new ClassReader(basicClass);
//		cR.accept(cN, 0);
//		AbstractInsnNode iNode;
//		Iterator<MethodNode> methods = cN.methods.iterator();
//
//		while(methods.hasNext()){
//			
//			MethodNode mN = methods.next();
//			System.out.println(mN.name);
//			if(mN.name.equals(method)){
//				
//				Iterator<AbstractInsnNode> iIterator = mN.instructions.iterator();
//				while(iIterator.hasNext()){
//					iNode = iIterator.next();
//					if(iNode.getOpcode() == Opcodes.NEW){
//						TypeInsnNode tINode = (TypeInsnNode) iNode;
//						tINode.desc = "com/jeffpeng/si/core/modintegration/harvestcraft/TileEntityPamPresserSI";
//					}
//					
//					if(iNode.getOpcode() == Opcodes.INVOKESPECIAL){
//						MethodInsnNode mINode = (MethodInsnNode) iNode;
//						mINode.owner = "com/jeffpeng/si/core/modintegration/harvestcraft/TileEntityPamPresserSI";
//					}
//				}
//			}
//		}
//		ClassWriter cW = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
//		cN.accept(cW);
//		System.out.println("[si.core.plugin] [SIClassTransformer] com.pam.harvestcraft.BlockPamPresser");
//		System.out.println("[si.core.plugin] [SIClassTransformer] This forces harvestcraft to use SI's modified presser TE");
//		
//		return cW.toByteArray();
//	}
	
	private byte[] patchSync(byte[] basicClass){
		/*
		 * This patches the Anvil to allow repairs that need 0 levels to complete.
		 */
		String method = "mapHardmodeRecipe";
		ClassNode cN = new ClassNode();
		ClassReader cR = new ClassReader(basicClass);
		cR.accept(cN, 0);
		Iterator<MethodNode> methods = cN.methods.iterator();
		while(methods.hasNext()){
			MethodNode mN = methods.next();
			if(mN.name.equals(method)){
				AbstractInsnNode aIN = mN.instructions.getFirst();
				InsnList inject = new InsnList();
				inject.add(new org.objectweb.asm.tree.InsnNode(Opcodes.RETURN));
				mN.instructions.insertBefore(aIN, inject);
				
			}
		}
		ClassWriter cW = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		cN.accept(cW);
		System.out.println("[si.core.plugin] [SIClassTransformer] sync.common.Sync");
		System.out.println("[si.core.plugin] [SIClassTransformer] This prevents Sync from adding the recipe for Sync:Sync_ItemPlaceholder on server start and bypassing SI.core recipe purger");
		
		return cW.toByteArray();		
	}
	
	private byte[] patchContainerRepair2(byte[] basicClass){
		/*
		 * This patches the Anvil to allow repairs that need 0 levels to complete.
		 */
		
		System.out.println("aa11");
		
		ClassNode cN = new ClassNode();
		ClassReader cR = new ClassReader(basicClass);
		cR.accept(cN, 0);
		AbstractInsnNode iNode;
		MethodNode mN = cN.methods.get(2);
		Iterator<AbstractInsnNode> iIterator = mN.instructions.iterator();
		while(iIterator.hasNext()){
			iNode = iIterator.next();
			if(iNode.getOpcode() == Opcodes.IFLE){
				JumpInsnNode jINode = (JumpInsnNode) iNode;
				jINode.setOpcode(Opcodes.IFLT);
			}
		}
		ClassWriter cW = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		cN.accept(cW);
		System.out.println("[si.core.plugin] [SIClassTransformer] Patched net.minecraft.inventory.ContainerRepair$2");
		System.out.println("[si.core.plugin] [SIClassTransformer] This allows the Anvil to process repairs that require 0 levels");
		
		return cW.toByteArray();		
	}
}
