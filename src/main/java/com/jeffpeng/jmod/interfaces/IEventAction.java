package com.jeffpeng.jmod.interfaces;

public interface IEventAction {
	
	public abstract void on(String trigger);
	public abstract boolean fire (String trigger);

}
