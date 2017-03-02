package com.jeffpeng.jmod.interfaces;

import java.util.Map;

import javax.script.Bindings;



import com.jeffpeng.jmod.primitives.BasicAction;

public interface ISettingsReceiver {
	public BasicAction set(Bindings o);
	public BasicAction set(String name, Object o);
	public Object getObject(String name);
	public String getString(String name);
	public Float getFloat(String name);
	public Double getDouble(String name);
	public Integer getInt(String name);
	public Boolean getBoolean(String name);
	public boolean hasSetting(String name);
	public Map<String,Object> getSettingObjects();
}
