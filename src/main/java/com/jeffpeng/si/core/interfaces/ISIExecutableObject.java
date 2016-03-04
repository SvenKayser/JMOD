package com.jeffpeng.si.core.interfaces;

public interface ISIExecutableObject extends ISIStagedObject {
	public void execute();
	
	public static void execute(@SuppressWarnings("rawtypes") Class toexecute){
		for(ISIStagedObject object : ISIStagedObject.list){
			if(object instanceof ISIExecutableObject){
				if(toexecute.isInstance(object)){
					if( ((ISIExecutableObject) object).isValid() )  ((ISIExecutableObject) object).execute();
				}
			}
		}
	}
	
}
