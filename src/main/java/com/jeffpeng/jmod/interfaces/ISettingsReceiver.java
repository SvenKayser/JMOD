package com.jeffpeng.jmod.interfaces;

import java.util.Map;

import javax.script.Bindings;



import com.jeffpeng.jmod.primitives.BasicAction;

public interface ISettingsReceiver {
	public BasicAction set(Bindings o);
	public BasicAction set(String name, Object o);
	public Map<String,Object> getSettingObjects();
}
