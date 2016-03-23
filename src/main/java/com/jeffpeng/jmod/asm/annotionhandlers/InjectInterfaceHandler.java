package com.jeffpeng.jmod.asm.annotionhandlers;

import java.util.Map;

import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import com.jeffpeng.jmod.annotations.InjectInterface;
import com.jeffpeng.jmod.asm.ASMUtil;
import com.jeffpeng.jmod.interfaces.IAnnotationHandler;

public class InjectInterfaceHandler implements IAnnotationHandler {

	@Override
	public String[] getAnnotationDescriptors() {
		return new String[]{ASMUtil.getDescriptor(InjectInterface.class)};
	}

	@Override
	public void handleAnnotation(Map<Object,Object> values, ClassNode cN) {
		
		
	}

}
