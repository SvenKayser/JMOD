package com.jeffpeng.si.core;

public class SIServerProxy extends SIProxy{
	@Override
	public void determineIfServer(){
		SI.isServer = true;
	}
	
	
}
