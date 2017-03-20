package com.jeffpeng.jmod.asm.annotionhandlers;

import java.util.Iterator;
import java.util.Map;

import org.objectweb.asm.tree.ClassNode;

import com.jeffpeng.jmod.annotations.StripMissingInterfaces;
import com.jeffpeng.jmod.asm.JAH;
import com.jeffpeng.jmod.interfaces.IAnnotationHandler;

public class StripMissingInterfacesHandler implements IAnnotationHandler {

	@Override
	public void handleAnnotation(Map<Object, Object> values, ClassNode cN) {
		Iterator<String> interfaces = cN.interfaces.iterator();
		while(interfaces.hasNext()){
			try {
				Class.forName(interfaces.next().replace("/", "."));
			} catch(ClassNotFoundException e){
				interfaces.remove();
			}
		}
	}

	@Override
	public String[] getAnnotationDescriptors() {
		return new String[]{JAH.getDescriptor(StripMissingInterfaces.class)};
	}
}
