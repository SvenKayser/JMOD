package com.jeffpeng.jmod.asm;

import java.util.Iterator;

import org.objectweb.asm.tree.LabelNode;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class JMODClassTransformer implements IClassTransformer{

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if(name.equalsIgnoreCase(JMODObfuscationHelper.get("net.minecraft.inventory.ContainerRepair$2"))) return patchContainerRepair2(basicClass);
		if(name.equalsIgnoreCase("sync.common.Sync")) return patchSync(basicClass);
		return basicClass;
	}
	
	private byte[] patchSync(byte[] basicClass){
		/*
		 * This patches Sync so it does not add recipes on server startup
		 */
		String method = "mapHardmodeRecipe";
		ClassNode cN = new ClassNode();
		ClassReader cR = new ClassReader(basicClass);
		cR.accept(cN, 0);
		Iterator<MethodNode> methods = cN.methods.iterator();
		while(methods.hasNext()){
			MethodNode mN = methods.next();
			if(mN.name.equals(method)){
				LabelNode lblnde = new LabelNode();
				AbstractInsnNode aIN = mN.instructions.getFirst();
				InsnList inject = new InsnList();
				inject.add(new MethodInsnNode(Opcodes.INVOKESTATIC,"com/jeffpeng/jmod/JMOD","getGlobalConfig","()Lcom/jeffpeng/jmod/GlobalConfig;",false));
				inject.add(new FieldInsnNode(Opcodes.GETFIELD,"com/jeffpeng/jmod/GlobalConfig","preventSyncRecipeReload","Z"));
				inject.add(new JumpInsnNode(Opcodes.IFEQ,  lblnde));
				inject.add(new InsnNode(Opcodes.RETURN));
				inject.add(lblnde);
				mN.instructions.insertBefore(aIN, inject);
				
			}
		}
		ClassWriter cW = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		cN.accept(cW);
		System.out.println("[jmod.plugin] [JMODClassTransformer] sync.common.Sync");
		System.out.println("[jmod.plugin] [JMODClassTransformer] This prevents Sync from adding the recipe for Sync:Sync_ItemPlaceholder on server start.");
		
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
		System.out.println("[jmod.plugin] [JMODClassTransformer] Patched net.minecraft.inventory.ContainerRepair$2");
		System.out.println("[jmod.plugin] [JMODClassTransformer] This allows the Anvil to process repairs that require 0 levels");
		
		return cW.toByteArray();		
	}
}
