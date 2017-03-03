package com.jeffpeng.jmod.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jeffpeng.jmod.primitives.ModScriptObject;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JMODBinding {
	public String[] binding();
	public Class<? extends ModScriptObject> scriptObject();
}


