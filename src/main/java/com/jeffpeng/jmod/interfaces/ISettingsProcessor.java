package com.jeffpeng.jmod.interfaces;

import com.jeffpeng.jmod.primitives.BasicAction;

public interface ISettingsProcessor {
	
	default public void processSettings(BasicAction settings ){
	};
	//public abstract void processSetting(String k, Object v);

}
