package com.jeffpeng.jmod.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JSFunctionWrapper {
	
	private Object context;
	private Object jsfunction;
	@SuppressWarnings("rawtypes")
	private static Class cls;
	private static Method m;
	private static boolean init = false;
	
	
	
	@SuppressWarnings("unchecked")
	public JSFunctionWrapper(Object jsfunction, Object context){
		if(!init){
			cls = jsfunction.getClass();
			try {
				m = cls.getMethod("call",new Class[]{Object.class,Object[].class});
			} catch (NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.jsfunction = jsfunction;
		this.context = context;
	}
	
	public Object invoke(Object[] params){
		try {
			return m.invoke(jsfunction,new Object[]{context,null});
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	

}
