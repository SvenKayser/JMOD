package com.jeffpeng.jmod.asm.annotionhandlers;

import java.util.Iterator;
import java.util.Map;

import org.objectweb.asm.tree.ClassNode;

import com.jeffpeng.jmod.JMOD;
import com.jeffpeng.jmod.annotations.JMODBinding;
import com.jeffpeng.jmod.asm.ASMUtil;
import com.jeffpeng.jmod.interfaces.IAnnotationHandler;

public class JMODBindingHandler implements IAnnotationHandler {

	@Override
	public void handleAnnotation(Map<Object, Object> values, ClassNode cN) {
		for(Map.Entry<Object,Object> entry : values.entrySet()){
			JMOD.LOG.info("###JBH "+entry.getKey()+" "+entry.getValue()+" ");
		}
	}

	@Override
	public String[] getAnnotationDescriptors() {
		System.out.println("###JBH "+ASMUtil.getDescriptor(JMODBinding.class));
		
		return new String[]{ASMUtil.getDescriptor(JMODBinding.class)};
	}
}