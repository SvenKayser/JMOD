package com.jeffpeng.jmod.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.interfaces.IAnnotationHandler;

import net.minecraft.launchwrapper.IClassTransformer;

public class JMODAnnotationParser implements IClassTransformer {

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if(!(name.startsWith("com.jeffpeng.jmod") || name.startsWith("jmodplugins")))
			return basicClass;
		ClassNode cN = new ClassNode();
		
		ClassReader cR = new ClassReader(basicClass);
		cR.accept(cN, 0);
		
		if(cN.visibleAnnotations != null){
			
			for(AnnotationNode aN : cN.visibleAnnotations){
				IAnnotationHandler.handle(aN, cN);
			}
		}
		
		ClassWriter cW = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		cN.accept(cW);
		cN.check(cN.version);
		return cW.toByteArray();
	}

}
