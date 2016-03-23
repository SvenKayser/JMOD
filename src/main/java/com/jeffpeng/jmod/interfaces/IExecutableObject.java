package com.jeffpeng.jmod.interfaces;

import java.util.List;

import com.jeffpeng.jmod.JMOD;

public interface IExecutableObject extends IStagedObject {
	public void execute();
	
	public static void execute(@SuppressWarnings("rawtypes") Class toexecute,List<IStagedObject> list){
		for(IStagedObject object : list){
			if(object instanceof IExecutableObject){
				if(toexecute.isInstance(object)){
					if( ((IExecutableObject) object).isValid() )  {
						if(JMOD.isDevVersion()) JMOD.LOG.info("Executing " + object.getClass().getName());
						
						((IExecutableObject) object).execute();
					} else {
						if(JMOD.isDevVersion()) JMOD.LOG.info("Not executing " + object.getClass().getName() + " since invalid.");
					}
				}
			}
		}
	}
	
}
