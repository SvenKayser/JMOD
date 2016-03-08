package com.jeffpeng.jmod.util;

import java.lang.reflect.Field;

import net.minecraft.block.Block;

public class Reflector {
	
	

	@SuppressWarnings("unused")
	private static boolean deobfuscated;

	private Object reflectionobject;
	private Class<?> reflectionclass;
	private boolean initialized = false;
	private Field[] declaredFields; 

	private void init(){
		 deobfuscated = (Block.class.getCanonicalName() == "net.minecraft.block.Block");

	}
	
	private String reobfField(String fieldname) {
		return fieldname;
	}
	
	public Reflector(Object obj) {
		if(!initialized) init();
		this.reflectionobject = obj;
		this.reflectionclass = obj.getClass();
		this.declaredFields = reflectionclass.getDeclaredFields();


	}

	public Reflector(Object obj, Class<?> rclass) {
		if(!initialized) init();
		this.reflectionobject = obj;
		this.reflectionclass = rclass;
		this.declaredFields = reflectionclass.getDeclaredFields();

	}

	public Reflector(Object obj, String classname) {
		if(!initialized) init();
		this.reflectionobject = obj;
		try {
			this.reflectionclass = Class.forName(classname);
			this.declaredFields = reflectionclass.getDeclaredFields();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(
					"[Reflector] class not found in classname-based construction");
		}
	}
	
	public Reflector set(int index, Object value) {
		try {
			Field field = this.declaredFields[index];
			field.setAccessible(true);
			Class<?> fieldclass = field.getType();
			field.set(this.reflectionobject, fieldclass.cast(value));
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("[Reflector] cannot set " + index + " as Object; IllegalArgument");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException("[Reflector] cannot set " + index + " as Object; IllegalAccess");
		}
		return this;
	}

	public Reflector set(String fieldname, Object value) {
		try {
			Field field = this.reflectionclass
					.getDeclaredField(reobfField(fieldname));
			field.setAccessible(true);
			Class<?> fieldclass = field.getType();
			field.set(this.reflectionobject, fieldclass.cast(value));
		} catch (NoSuchFieldException e) {
			
			throw new RuntimeException("[Reflector] cannot set " + fieldname + " as Object; NoSuchField");
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("[Reflector] cannot set " + fieldname + " as Object; IllegalArgument");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException("[Reflector] cannot set " + fieldname + " as Object; IllegalAccess");
		}
		return this;
	}
	
	public Reflector set(int index, int value) {
		try {
			Field field = this.declaredFields[index];
			field.setAccessible(true);

			field.setInt(this.reflectionobject, value);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("[Reflector] cannot set " + index + " as int");
		}

		return this;
	}

	public Reflector set(String fieldname, int value) {
		try {
			Field field = this.reflectionclass.getDeclaredField(reobfField(fieldname));
			field.setAccessible(true);

			field.setInt(this.reflectionobject, value);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("[Reflector] cannot set " + fieldname + " as int");
		}

		return this;
	}
	
	public Reflector set(int index, float value) {
		try {
			Field field = this.declaredFields[index];
			field.setAccessible(true);
			field.setFloat(this.reflectionobject, value);
			
		} catch (Exception e) {
			
			throw new RuntimeException("[Reflector] cannot set " + index + " as float");
		}
		return this;
	}

	public Reflector set(String fieldname, float value) {
		try {
			Field field = this.reflectionclass.getDeclaredField(reobfField(fieldname));
			field.setAccessible(true);
			field.setFloat(this.reflectionobject, value);
			
		} catch (Exception e) {
			
			throw new RuntimeException("[Reflector] cannot set " + fieldname + " as float");
		}
		return this;
	}
	
	public Object get(int index) {
		try {
			Field field = this.declaredFields[index];
			field.setAccessible(true);
			return field.get(this.reflectionobject);
		} catch (Exception e) {
			throw new RuntimeException("[Reflector] cannot get " + index);
		}

	}

	public Object get(String fieldname) {
		try {
			Field field = this.reflectionclass.getDeclaredField(reobfField(fieldname));
			field.setAccessible(true);
			return field.get(this.reflectionobject);
		} catch (Exception e) {
			throw new RuntimeException("[Reflector] cannot get " + fieldname);
		}

	}
	
	public int getInt(String fieldname) {
		try {
			Field field = this.reflectionclass.getDeclaredField(reobfField(fieldname));
			field.setAccessible(true);
			return field.getInt(this.reflectionobject);
		} catch (Exception e) {
			throw new RuntimeException("[Reflector] cannot get " + fieldname);
		}

	}
	
	public Float getFloat(String fieldname) {
		try {
			Field field = this.reflectionclass.getDeclaredField(reobfField(fieldname));
			field.setAccessible(true);
			return field.getFloat(this.reflectionobject);
		} catch (Exception e) {
			throw new RuntimeException("[Reflector] cannot get " + fieldname);
		}
	}
	
	

}
