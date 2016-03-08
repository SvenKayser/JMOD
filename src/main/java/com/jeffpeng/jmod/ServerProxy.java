package com.jeffpeng.jmod;

public class ServerProxy extends Proxy{
	@Override
	public void determineIfServer(){
		JMOD.setIsServer(true);
	}
	
	
}
