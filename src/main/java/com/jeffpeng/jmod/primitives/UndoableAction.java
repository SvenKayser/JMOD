package com.jeffpeng.jmod.primitives;

import com.jeffpeng.jmod.JMODRepresentation;

public abstract class UndoableAction extends BasicAction {

	public UndoableAction(JMODRepresentation owner) {
		super(owner);
	}
	
	public abstract void undo();
	
}
