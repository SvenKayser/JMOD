package com.jeffpeng.jmod.interfaces;

public interface IEventObject {
	public abstract void on(String trigger, Object callback);
	public abstract boolean fire (String trigger);
}
