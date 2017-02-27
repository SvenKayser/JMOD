package com.jeffpeng.jmod.primitives;

import java.util.HashMap;
import java.util.Map;

import javax.script.Bindings;




import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.actions.SharedRoutines;
import com.jeffpeng.jmod.interfaces.IExecutableObject;
import com.jeffpeng.jmod.interfaces.ISettingsReceiver;
import com.jeffpeng.jmod.interfaces.IStagedObject;
import com.jeffpeng.jmod.validator.Validator;

public class BasicAction extends OwnedObject implements IExecutableObject, ISettingsReceiver {
	
	protected boolean valid = false;
	protected SharedRoutines routines;
	private Map<String,Object> settingObjects = new HashMap<>();
	
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
	
	@Override	
	public BasicAction set(Bindings o){
		for(String key : o.keySet()) settingObjects.put(key, o.get(key));
		
		return this;
	}
	
	public BasicAction set(String name, Object o){
		settingObjects.put(name, o);
		return this;
	}
	
	public Map<String,Object> getSettingObjects(){
		return settingObjects;
	}

	@Override
	public int compareTo(IStagedObject arg0) {
		// TODO Auto-generated method stub
		return 0;
	}



	

}
