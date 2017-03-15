package com.jeffpeng.jmod.primitives;

import java.util.HashMap;
import java.util.Map;

import javax.script.Bindings;

import com.jeffpeng.jmod.JMODRepresentation;
import com.jeffpeng.jmod.actions.SharedRoutines;
import com.jeffpeng.jmod.interfaces.IExecutableObject;
import com.jeffpeng.jmod.interfaces.IStagedObject;
import com.jeffpeng.jmod.validator.Validator;

public class BasicAction extends OwnedObject implements IExecutableObject {
	
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
	
	public BasicAction set(Bindings o){
		for(String key : o.keySet()) settingObjects.put(key, o.get(key));
		
		return this;
	}
	
	public BasicAction set(String name, Object o){
		settingObjects.put(name.toLowerCase(), o);
		return this;
	}
	
	public Object getObject(String name){
		return settingObjects.get(name.toLowerCase());
	}
	
	public String getString(String name){
		Object o = settingObjects.get(name.toLowerCase());
		if(o instanceof String){
			return (String) o;
		}
		return o.toString();
	}
	
	public Float getFloat(String name){
		Object o = settingObjects.get(name.toLowerCase());
		if(o instanceof Double){
			return ((Double)o).floatValue();
		}
		if(o instanceof Float){
			return ((Float)o);
		}
		if(o instanceof String){
			return Float.parseFloat((String)o);
		}
		return null;
	}
	
	public Double getDouble(String name){
		Object o = settingObjects.get(name.toLowerCase());
		if(o instanceof Double){
			return ((Double)o);
		}
		if(o instanceof Float){
			return ((Float)o).doubleValue();
		}
		if(o instanceof String){
			return Double.parseDouble((String)o);
		}
		return null;
	}
	
	public Integer getInt(String name){
		Object o = settingObjects.get(name.toLowerCase());
		if(o instanceof Integer){
			return (Integer) o;
		}
		if(o instanceof String){
			return Integer.parseInt((String)o);
		}
		return null;
	}
	
	public Boolean getBoolean(String name){
		Object o = settingObjects.get(name.toLowerCase());
		if(o instanceof Boolean){
			return (Boolean) o;
		}
		if(o instanceof String){
			return Boolean.parseBoolean((String)o);
		}
		return null;
		
	}
	
	public boolean hasSetting(String name){
		return settingObjects.containsKey(name.toLowerCase());
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
