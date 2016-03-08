package com.jeffpeng.jmod.primitives;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.interfaces.IExecutableObject;
import com.jeffpeng.jmod.util.actions.SharedRoutines;

public class BasicAction extends OwnedObject implements IExecutableObject {
	
	protected boolean valid = false;
	protected SharedRoutines routines;
	
	public BasicAction(JMODRepresentation owner){
		super(owner);
		routines = new SharedRoutines(owner);
		registerAsStaged();
	}
	
	

	@Override
	public boolean isValid() {
		return valid;
	}

	@Override
	public void execute() {
				
	}



	

}
