package com.jeffpeng.jmod.asm;

public class ASMUtil {
	public static String getDescriptor(Class<?> clazz){
		return "L"+clazz.getName().replace(".", "/")+";";
	}

}
