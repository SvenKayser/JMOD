package com.jeffpeng.jmod.primitives;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.actions.SharedRoutines;
import com.jeffpeng.jmod.interfaces.IExecutableObject;
import com.jeffpeng.jmod.validator.Validator;

public class BasicAction extends OwnedObject implements IExecutableObject {
	
	protected boolean valid = false;
	protected SharedRoutines routines;
	
	public BasicAction(JMODRepresentation owner){
		super(owner);
		routines = new SharedRoutines(owner);
		owner.registerStagedObject(this);
		Validator.msg(this.getClass().getSimpleName() + " OK.");
	}
	
	

	@Override
	public boolean isValid() {
		return valid;
	}

	@Override
	public void execute() {
				
	}



	

}
