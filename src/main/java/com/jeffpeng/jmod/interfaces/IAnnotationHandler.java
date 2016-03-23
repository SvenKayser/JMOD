package com.jeffpeng.jmod.interfaces;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

public interface IAnnotationHandler {
	public static Map<String,IAnnotationHandler> annotationHandlers = new HashMap<>();
	
	public static void register(IAnnotationHandler handler){
		for(String ad : handler.getAnnotationDescriptors()){
			System.out.println("###1 put "+ad);
			annotationHandlers.put(ad, handler);
		}
		
	}
	
	public static IAnnotationHandler getHandler(AnnotationNode aN){
		System.out.println("###2 get "+aN.desc);
		return annotationHandlers.get(aN.desc);
	}
	
	public static void handle(AnnotationNode aN, ClassNode cN){
		IAnnotationHandler handler = getHandler(aN);
		if(handler!=null){
			Map<Object,Object> values = new HashMap<>();
			if(aN.values != null && aN.values.size() > 0) for(int c = 1; c < aN.values.size(); c+=2){
				values.put(aN.values.get(c), aN.values.get(c+1));
			}
			handler.handleAnnotation(values,cN);
		}
	}
	
	public void handleAnnotation(Map<Object,Object> values, ClassNode cN);
	
	public String[] getAnnotationDescriptors();
	
	
}
